package com.rc2s.application.services.authentication;

import java.io.Serializable;
import java.security.Principal;
import java.util.Objects;

public class UserPrincipal implements Principal, Serializable
{
    private final String username;
    
    public UserPrincipal(final String username)
    {
        this.username = username;
    }
    
    @Override
    public String getName()
    {
        return username;
    }

    @Override
    public String toString()
    {
        return "UserPrincipal{" + "username=" + username + '}';
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final UserPrincipal other = (UserPrincipal) obj;
        if (!Objects.equals(this.username, other.username))
        {
            return false;
        }
        return true;
    }   
}
