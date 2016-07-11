package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "synchronization")
public class Synchronization implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "link_cube_synchronization",
		joinColumns = @JoinColumn(name = "synchronization", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "cube", referencedColumnName = "id")
	)
	private List<Cube> cubes;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "link_user_synchronization",
		joinColumns = @JoinColumn(name = "synchronization", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "user", referencedColumnName = "id")
	)
	private List<User> users;
	
	private Date created;
	private Date updated;

	public Synchronization() {}
	
	public Synchronization(final Integer id, final String name, 
            final Date created, final Date updated)
	{
		this.id = id;
		this.name = name;
		this.created = created;
		this.updated = updated;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(final Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public List<Cube> getCubes()
	{
		return cubes;
	}

	public void setCubes(final List<Cube> cubes)
	{
		this.cubes = cubes;
	}

	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(final List<User> users)
	{
		this.users = users;
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
	
	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public int hashCode()
	{
		int hash = 3;
		hash = 71 * hash + Objects.hashCode(this.id);
		hash = 71 * hash + Objects.hashCode(this.name);
		hash = 71 * hash + Objects.hashCode(this.created);
		hash = 71 * hash + Objects.hashCode(this.updated);
		return hash;
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if(o != null && o instanceof Synchronization)
		{
			Synchronization s = (Synchronization)o;
			
			if(s.getId() != null && this.getId() != null)
				return Objects.equals(s.getId(), this.getId());
		}
		
		return false;
	}
}