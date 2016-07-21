package com.rc2s.realm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordHash
 * 
 * Class to encrypt password
 * Sha1 is used with salt + pepper
 * 
 * @author RC2S
 */
public class PasswordHash
{
    private final String HASH_SALT;
    private final String HASH_PEPPER;
    
    public PasswordHash(final String salt, final String pepper)
    {
        this.HASH_SALT      = salt;
        this.HASH_PEPPER    = pepper;
    }
    
    public boolean compareHash(String actualHash, String expectedHash)
    {
        return expectedHash.equals(createHash(actualHash));
    }
    
    private String createHash(String toHash)
    {
        return sha1(HASH_SALT + toHash + HASH_PEPPER);
    }
    
    private String sha1(final String str)
	{
		String sha1 = null;
		
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());

			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
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

    @Override
    public String toString()
    {
        return "PasswordHash{" + "HASH_SALT=" + HASH_SALT + ", HASH_PEPPER=" + HASH_PEPPER + '}';
    }
}