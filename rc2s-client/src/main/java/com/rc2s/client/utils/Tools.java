package com.rc2s.client.utils;

import java.util.regex.Pattern;

public class Tools
{
    private static final Pattern IP_PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    
    public static boolean matchIP(String ip) {
        return IP_PATTERN.matcher(ip).matches();
    }
}
