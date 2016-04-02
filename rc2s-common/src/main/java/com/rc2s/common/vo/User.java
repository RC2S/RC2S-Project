package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable
{
    private int id;
    private String username;
    private String password;
    private String role;
    private String token;
    private boolean activated;
    private boolean lock;
    private String lastIp;
    private Date created;
    private Date updated;

    public User() {}
    
    public User(String username, String password)
    {
        this.username   = username;
        this.password   = password;
    }

    public User(int id, String username, String password,
        String role, String token, boolean activated,
        boolean lock, String lastIp, Date created, Date updated)
    {
        this.id         = id;
        this.username   = username;
        this.password   = password;
        this.role       = role;
        this.token      = token;
        this.activated  = activated;
        this.lock       = lock;
        this.lastIp     = lastIp;
        this.created    = created;
        this.updated    = updated;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public boolean isActivated()
    {
        return activated;
    }

    public void setActivated(boolean activated)
    {
        this.activated = activated;
    }

    public boolean isLock()
    {
        return lock;
    }

    public void setLock(boolean lock)
    {
        this.lock = lock;
    }

    public String getLastIp()
    {
        return lastIp;
    }

    public void setLastIp(String lastIp)
    {
        this.lastIp = lastIp;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }
    
    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(Date updated)
    {
        this.updated = updated;
    }
}
