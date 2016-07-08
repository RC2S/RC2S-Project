package com.rc2s.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash
{
	public static String sha1(final String str)
	{
		String sha1 = null;
		
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());

			byte byteData[] = md.digest();

			//convert the byte to hex format method 1
			StringBuilder sb = new StringBuilder();
			for(int i = 0 ; i < byteData.length ; i++)
			{
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			
			sha1 = sb.toString();
		}
		catch(NoSuchAlgorithmException e)
		{
			System.err.println(e.getMessage());
		}
		
		return sha1;
	}
}
