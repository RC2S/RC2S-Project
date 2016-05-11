package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String username;
    private String password;
    private boolean activated;
    private boolean locked;
	
	@Column(name = "last_login")
    private Date lastLogin;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
	private List<Synchronization> synchronizations;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "link_user_role",
		joinColumns = @JoinColumn(name = "user", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "id")
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

    public User(Integer id, String username, String password,
        boolean activated, boolean locked,
        Date lastLogin, Date created, Date updated)
    {
        this.id         = id;
        this.username   = username;
        this.password   = password;
        this.activated  = activated;
        this.locked     = locked;
        this.lastLogin  = lastLogin;
        this.created    = created;
        this.updated    = updated;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id)
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

    public Date getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin)
    {
        this.lastLogin = lastLogin;
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

	public List<Synchronization> getSynchronizations()
	{
		return synchronizations;
	}

	public void setSynchronizations(List<Synchronization> synchronizations)
	{
		this.synchronizations = synchronizations;
	}

	public List<Role> getRoles()
	{
		return roles;
	}

	public void setRoles(List<Role> roles)
	{
		this.roles = roles;
	}
}
