package com.rc2s.common.client.utils;

import com.rc2s.common.utils.EJB;
import com.rc2s.common.vo.User;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.regex.Pattern;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public class Tools
{
	private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
	
    private static final Pattern IP_PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    
    private static User user;
    
    public static boolean matchIP(final String ip)
	{
        return IP_PATTERN.matcher(ip).matches();
    }
	
	public static <T extends Serializable> Set<ConstraintViolation<T>> validate(final T vo)
	{
		return VALIDATOR.validate(vo);
	}

    public static String formatDate(final Date date)
    {
        if (date == null)
            return "";
        
        return new SimpleDateFormat("MM-dd-YYYY hh:mm").format(date);
    }
    
    public static String replaceFile(final String toBeReplace)
    {
		String str = null;
        
        if (toBeReplace != null)
		{
            str = new String(toBeReplace);
            
			if(str.startsWith("file:\\"))
				str = str.replace("file:\\", "");
			else if(str.startsWith("file://"))
				str = str.replace("file://", "");
			else if(str.startsWith("file:/"))
				str = str.replace("file:", "");
		}
		
		return str;
	}
    
    public static String getIPAdress() throws UnknownHostException
    {
        return InetAddress.getLocalHost().getHostAddress();
    }
    
    public static boolean isLoopBackAdress() throws UnknownHostException
    {
        return InetAddress.getLocalHost().isLoopbackAddress();
    }

    public static User getAuthenticatedUser()
    {
        return Tools.user;
    }

    public static void setAuthenticatedUser(final User user)
    {
        Tools.user = user;
    }
}
