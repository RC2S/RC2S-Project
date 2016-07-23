package com.rc2s.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hash
 * 
 * Class used for hash process
 * Uses sha1
 * 
 * @author RC2S
 */
public class Hash
{
	private static final Logger log = LogManager.getLogger(Hash.class);
    
    /**
	 * sha1
	 * 
	 * Hashes a string in sha1
	 * 
	 * @param str
	 * @return String sha1 hash
	 */
	public static String sha1(final String str)
	{
		String sha1 = null;
		
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());

			byte byteData[] = md.digest();

			// Convert the byte to hex format method 1
			StringBuilder sb = new StringBuilder();
            
			for (int i = 0; i < byteData.length; i++)
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			
			sha1 = sb.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error(e);
		}
		
		return sha1;
	}
}
