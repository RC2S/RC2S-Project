package com.rc2s.common.client.utils;

import com.rc2s.common.exceptions.RC2SException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Classe utilitaire permettant l'exécution de requêtes HTTP.
 */
public class HttpRequest
{
	private final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
	
    protected final URL url;
    protected final String data;
	protected final Map<String, String> headers;
    protected final boolean returnError;
	
    protected Proxy proxy = null;
    protected HostnameVerifier sslHostnameVerifier = null;
    protected SSLContext sslContext = null;

	public HttpRequest(URL url) throws RC2SException
	{
		this(url, "", null, false, null);
	}
	
	/**
	 * @see HttpRequest#HttpRequest(java.net.URL, java.lang.String, java.util.Map, boolean, java.net.Authenticator) 
	 * @param url
	 * @param data
	 * @param headers
	 * @param returnError
	 * @throws RC2SException 
	 */
	public HttpRequest(URL url, Map<String, String> data, Map<String, String> headers, boolean returnError) throws RC2SException
	{
		this(url, formatParams(data), headers, returnError, null);
	}
	
    /**
	 * @see HttpRequest#HttpRequest(java.net.URL, java.lang.String, java.util.Map, boolean, java.net.Authenticator) 
	 * @param url
	 * @param data
	 * @param headers
	 * @param returnError 
	 */
	public HttpRequest(URL url, String data, Map<String, String> headers, boolean returnError)
    {
        this(url, data, headers, returnError, null);
    }
	
	/**
	 * @see HttpRequest#HttpRequest(java.net.URL, java.lang.String, java.util.Map, boolean, java.net.Authenticator) 
	 * @param url
	 * @param data
	 * @param headers
	 * @param returnError
	 * @param authenticator
	 * @throws RC2SException 
	 */
	public HttpRequest(URL url, Map<String, String> data, Map<String, String> headers, boolean returnError, Authenticator authenticator) throws RC2SException
	{
		this(url, formatParams(data), headers, returnError, authenticator);
	}
	
	/**
     * Crée une nouvelle instance de type HttpRequest.
     * Initialisation de l'url, des données, des headers HTTP, ainsi que du 
     * booléen permettant le retour d'erreurs par les requêtes.
	 * Un authentificateur peut de plus permettre de connecter l'utilisateur sur
	 * le service distant.
     * @param url
     * @param data
     * @param headers
     * @param returnError 
	 * @param authenticator 
     */
	public HttpRequest(URL url, String data, Map<String, String> headers, boolean returnError, Authenticator authenticator)
    {
        this.url = url;
        this.data = data;
		this.headers = headers;
        this.returnError = returnError;
		
		Authenticator.setDefault(authenticator);
    }
    
    /**
     * Concatène l'URL et ses paramètres, puis effectue la connexion avec l'URL
     * donnée.
     * @return
     * @throws RC2SException 
     */
    public String get() throws RC2SException
    {
        String line;
        BufferedReader reader;
        StringBuilder response = new StringBuilder();
        URL getURL = (!data.isEmpty() ? concatenateURL() : url);
        HttpURLConnection connection;
		
        try
        {
			//S'il s'agit d'une connexion HTTPS
            if(url.getProtocol().equalsIgnoreCase("HTTPS"))
            {
				setUnsafeSSL();
				
                //On ouvre une nouvelle connexion HTTPS, avec ou sans proxy
                connection = (proxy == null) ? (HttpsURLConnection)getURL.openConnection() : (HttpsURLConnection)getURL.openConnection(proxy);
                
                //Définit la classe de vérification des certificats SSL
                if(this.sslHostnameVerifier != null)
                    ((HttpsURLConnection)connection).setHostnameVerifier(sslHostnameVerifier);
                if(this.sslContext != null)
                    ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            //Sinon connexion HTTP
            else
                connection = (proxy == null) ? (HttpURLConnection)getURL.openConnection() : (HttpURLConnection)getURL.openConnection(proxy);
            
            connection.setRequestMethod("GET");
			
			if(headers != null)
			{
				for(Map.Entry<String, String> header : headers.entrySet())
					connection.setRequestProperty(header.getKey(), header.getValue());
			}
			
			if(connection.getRequestProperty("Content-Type") == null)
				connection.setRequestProperty("Content-Type", DEFAULT_CONTENT_TYPE);
			
            //Si la code de retour du serveur Web est un code 2**
            if(connection.getResponseCode()/100 == 2)
            {
                try
                {
                    //On initialise le flux de lecture des données
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }
                catch(IOException e)
                {
                    //Si on souhaite renvoyer les erreur, on essaie de définir le flux d'erreur comme reader
                    if(returnError)
                    {
                        InputStream stream = connection.getErrorStream();

                        if(stream != null)
                            reader = new BufferedReader(new InputStreamReader(stream));
                        else
                            throw new RC2SException("An error occured while connecting to error stream", e);
                    }
                    else
                        throw new RC2SException("An error occured while connecting to data stream", e);
                }

                //On lit les données en provenance du serveur Web
                while((line = reader.readLine()) != null)
                {
                    response.append(line);
                    response.append("\r\n");
                }

                //On ferme le flux et on renvoie la chaîne
                reader.close();
                return response.toString();
            }
            else
            {
                StringBuilder error = new StringBuilder("Bad response code received: HTTP ");
				error.append(connection.getResponseCode()).append("/").append(connection.getResponseMessage());
				
                if(returnError)
                    return error.toString();
                else
                    throw new RC2SException(error.toString());
            }
        }
        catch(IOException e)
        {
            throw new RC2SException(e.getMessage(), e);
        }
    }
    
    /**
     * Effectue la connexion avec l'URL donnée, envoie les données en POST et
     * renvoie le contenu de la page.
     * @return
     * @throws RC2SException 
     */
    public String post() throws RC2SException
    {
        String line;
        BufferedReader reader;
        DataOutputStream writer;
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection;
        byte[] byteParams;
        
        try
        {
            //On transforme les données en tableau de bytes
            byteParams = data.getBytes(StandardCharsets.UTF_8);
			
			//S'il s'agit d'une connexion HTTPS
            if(url.getProtocol().equalsIgnoreCase("HTTPS"))
            {
				setUnsafeSSL();
				
                //On ouvre une nouvelle connexion HTTPS, avec ou sans proxy
                connection = (proxy == null) ? (HttpsURLConnection)url.openConnection() : (HttpsURLConnection)url.openConnection(proxy);
                
                //Définit la classe de vérification des certificats SSL
                if(this.sslHostnameVerifier != null)
                    ((HttpsURLConnection)connection).setHostnameVerifier(sslHostnameVerifier);
                if(this.sslContext != null)
                    ((HttpsURLConnection)connection).setSSLSocketFactory(sslContext.getSocketFactory());
            }
            //Sinon, connexion HTTP
            else
                connection = (proxy == null) ? (HttpURLConnection)url.openConnection() : (HttpURLConnection)url.openConnection(proxy);


            //On définit les temps de timeout de la connexion
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            //Requête POST + définition des header de type et de taille de contenu
            connection.setRequestMethod("POST");
			
			if(headers != null)
			{
				for(Map.Entry<String, String> header : headers.entrySet())
					connection.setRequestProperty(header.getKey(), header.getValue());
			}
			
			if(connection.getRequestProperty("Content-Type") == null)
				connection.setRequestProperty("Content-Type", DEFAULT_CONTENT_TYPE);
			
            connection.setFixedLengthStreamingMode(byteParams.length);

            //Nous n'utilisons pas de cache, et faisons de la lecture et de l'écriture
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //On écrit les données dans le flux sortant vers le serveur Web
            writer = new DataOutputStream(connection.getOutputStream());
            writer.write(byteParams);
            writer.flush();
            writer.close();
                
            //Si le code de retour est un code 2**
            if(connection.getResponseCode()/100 == 2)
            {
                try
                {
                    //On récupère le flux entrant en provenance du serveur Web
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                }
                catch(IOException e)
                {
                    //Si on souhaite récupérer les erreurs
                    if(returnError)
                    {
                        //On définit le flux d'erreur comme flux entrant
                        InputStream stream = connection.getErrorStream();

                        if(stream != null)
                            reader = new BufferedReader(new InputStreamReader(stream));
                        else
                            throw new RC2SException("An error occured while connecting to error stream", e);
                    }
                    else
                        throw new RC2SException("An error occured while connecting to data stream", e);
                }

                //On lit les données envoyées par le serveur Web
                while((line = reader.readLine()) != null)
                {
                    response.append(line);
                    response.append("\r\n");
                }

                //On ferme le reader et on renvoie la chaîne
                reader.close();
                return response.toString();
            }
            else
            {
                StringBuilder error = new StringBuilder("Bad response code received: HTTP ");
				error.append(connection.getResponseCode()).append("/").append(connection.getResponseMessage());
				error.append(": ");
				
				if(connection.getResponseCode() == 401)
					error.append(connection.getHeaderField("WWW-Authenticate"));
				else if(connection.getResponseCode()/100 == 3)
				{
					for(Map.Entry<String, List<String>> header : connection.getHeaderFields().entrySet())
					{
						error.append("\n");
						error.append(header.getKey());
						error.append(": ");
						error.append(header.getValue().get(0));
					}
				}
				else
					error.append("Unknown reason");
                
                if(returnError)
                    return error.toString();
                else
                    throw new RC2SException(error.toString());
            }
        }
        catch(IOException e)
        {
            throw new RC2SException(e.getMessage(), e);
        }
    }

    /**
     * Formate les paramètres depuis une liste mappée et renvoie une chaîne.
     * @param params
     * @return
     * @throws RC2SException 
     */
    public static final String formatParams(Map<String, String> params) throws RC2SException
    {
        StringBuilder builder = new StringBuilder();

        for(Map.Entry<String, String> entry : params.entrySet())
        {
            if(builder.length() > 0) 
                builder.append('&');
            
            try
            {
                builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                
                if(entry.getValue() != null)
                {
                    builder.append('=');
                    builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
            }
            catch(UnsupportedEncodingException e)
            {
                throw new RC2SException(e.getMessage(), e);
            }
        }

        return builder.toString();
    }
    
    /**
     * Renvoie une URL complète contenant le FQN, le chemin d'accès et les données
     * GET spécifiées.
     * Exemple: domaine.loc/page?data1=0&data2
     * @return
     * @throws RC2SException 
     */
    public final URL concatenateURL() throws RC2SException
    {
        try
        {
            return new URL(url.toString() + ((!url.toString().contains("?")) ? "?" : "&") + data); 
        }
        catch(MalformedURLException e)
        {
            throw new RC2SException(e.getMessage(), e);
        }
    }
	
	/**
     * Définit le proxy à utiliser pour la requête.
     * @param proxy 
     */
    public final void setProxy(Proxy proxy)
    {
        this.proxy = proxy;
    }
    
    /**
     * Définit le nouveau contexte de vérification des noms d'hôte pour validation
     * des certificats SSL.
     * @param hostnameVerifier 
	 * @throws com.rc2s.common.exceptions.RC2SException 
     */
    public final void setHostnameVerifier(HostnameVerifier hostnameVerifier) throws RC2SException
    {
        if(url.getProtocol().equalsIgnoreCase("HTTPS"))
            this.sslHostnameVerifier = hostnameVerifier;
        else
            throw new RC2SException("Trying to set a new HostnameVerifier for a HTTP request");
    }
    
    /**
     * Définit le nouveau contexte SLL de la requête.
     * @param sslContext 
	 * @throws com.rc2s.common.exceptions.RC2SException 
     */
    public final void setSSLContext(SSLContext sslContext) throws RC2SException
    {
        if(url.getProtocol().equalsIgnoreCase("HTTPS"))
            this.sslContext = sslContext;
        else
            throw new RC2SException("Trying to set a new SSLContext for a HTTP request");
    }
    
    /**
     * Définit un HostnameVerifier et un SSLContext sans vérification pour
     * la requête.
     * Tous les certificats SSL sont alors acceptés sans être vérifiés.
	 * @throws com.rc2s.common.exceptions.RC2SException
     */
    public void setUnsafeSSL() throws RC2SException
    {
        if(url.getProtocol().equalsIgnoreCase("HTTPS"))
        {
            SSLContext context;
            TrustManager[] trustManager = new TrustManager[]
			{
                new X509TrustManager()
                {
                    @Override
                    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {}

                    @Override
                    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {}

                    @Override
                    public X509Certificate[] getAcceptedIssuers()
                    {
                        return null;
                    }
                }
            };
			HostnameVerifier hostnameVerifier = new HostnameVerifier()
			{
				@Override
				public boolean verify(String string, SSLSession ssls)
				{
					return true;
				}
			};

            try
            {
                context = SSLContext.getInstance("SSL");
                context.init(null, trustManager, new SecureRandom());
            }
            catch(KeyManagementException | NoSuchAlgorithmException e)
            {
                throw new RC2SException(e.getMessage(), e);
            }

            setHostnameVerifier(hostnameVerifier);
            setSSLContext(context);
        }
        else
            throw new RC2SException("Trying to set an unsafe SSL context for a HTTP request");
    }
    
    /**
     * Renvoie le protcole utilisé par la requête.
     * @return 
     */
    public final String getProtocol()
    {
        return url.getProtocol();
    }

    /**
     * Renvoie l'URL de la requête.
     * @return 
     */
    public final URL getUrl()
    {
        return url;
    }

    /**
     * Renvoie les données de la requête.
     * @return 
     */
    public final String getData()
    {
        return data;
    }

    /**
     * Renvoie le type de données de la requête.
     * @return 
     */
    public final Map<String, String> getHeaders()
    {
        return headers;
    }

    /**
     * Renvoie le booléen d'erreur de la requête.
     * @return 
     */
    public final boolean doReturnError()
    {
        return returnError;
    }
}
