package com.rc2s.client.utils;

import java.io.Serializable;
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
}
