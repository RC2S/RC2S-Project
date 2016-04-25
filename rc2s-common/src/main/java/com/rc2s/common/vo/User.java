package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable
{
    @Id
    @GeneratedValue
    private int id;
    
    private String username;
    private String password;
    private String token;
    private boolean activated;
    private boolean locked;
    private String lastIp;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
	private List<Synchronization> synchronizations;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "user_role",
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	)
	private List<Role> roles;
	
    private Date created;
    private Date updated;

    public User() {}
    
    public User(String username, String password)
    {
        this.username   = username;
        this.password   = password;
    }

    public User(int id, String username, String password,
        String token, boolean activated, boolean locked,
        String lastIp, Date created, Date updated)
    {
        this.id         = id;
        this.username   = username;
        this.password   = password;
        this.token      = token;
        this.activated  = activated;
        this.locked     = locked;
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

    public boolean isLocked()
    {
        return locked;
    }

    public void setLockrd(boolean locked)
    {
        this.locked = locked;
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
