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
		name = "link_user_group",
		joinColumns = @JoinColumn(name = "user", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "group", referencedColumnName = "id")
	)
	private List<Group> groups;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Track> tracks;
	
    private Date created;
    private Date updated;

    public User() {}
    
    public User(final String username, final String password)
    {
        this.username   = username;
        this.password   = password;
    }

    public User(final Integer id, final String username, final String password,
        final boolean activated, final boolean locked,
        final Date lastLogin, final Date created, final Date updated)
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

    public void setId(final Integer id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(final String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }
    
    public boolean isActivated()
    {
        return activated;
    }

    public void setActivated(final boolean activated)
    {
        this.activated = activated;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(final boolean locked)
    {
        this.locked = locked;
    }

    public Date getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin(final Date lastLogin)
    {
        this.lastLogin = lastLogin;
    }

    public Date getCreated()
    {
        return created;
    }

    public void setCreated(final Date created)
    {
        this.created = created;
    }
    
    public Date getUpdated()
    {
        return updated;
    }

    public void setUpdated(final Date updated)
    {
        this.updated = updated;
    }

	public List<Synchronization> getSynchronizations()
	{
		return synchronizations;
	}

	public void setSynchronizations(final List<Synchronization> synchronizations)
	{
		this.synchronizations = synchronizations;
	}

	public List<Group> getGroups()
	{
		return groups;
	}

	public void setGroups(final List<Group> groups)
	{
		this.groups = groups;
	}

    public List<Track> getTracks()
    {
        return tracks;
    }

    public void setTracks(final List<Track> tracks)
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
	public boolean equals(final Object o)
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