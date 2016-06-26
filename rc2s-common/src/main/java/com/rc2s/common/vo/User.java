package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "user")
public class User implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
	@javax.validation.constraints.Size(min = 3)
    private String username;
	
	@javax.validation.constraints.Size(min = 8, max = 250)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Track> tracks;
	
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

    public void setLocked(boolean locked)
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

    public List<Track> getTracks()
    {
        return tracks;
    }

    public void setTracks(List<Track> tracks)
    {
        this.tracks = tracks;
    }
	
	@Override
	public String toString()
	{
		return username;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 17 * hash + Objects.hashCode(this.id);
		hash = 17 * hash + Objects.hashCode(this.username);
		hash = 17 * hash + Objects.hashCode(this.password);
		hash = 17 * hash + (this.activated ? 1 : 0);
		hash = 17 * hash + (this.locked ? 1 : 0);
		hash = 17 * hash + Objects.hashCode(this.lastLogin);
		hash = 17 * hash + Objects.hashCode(this.created);
		hash = 17 * hash + Objects.hashCode(this.updated);
		return hash;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o != null && o instanceof User)
		{
			User u = (User)o;
			
			if(u.getId() != null && this.getId() != null)
				return Objects.equals(u.getId(), this.getId());
		}
		
		return false;
	}
}