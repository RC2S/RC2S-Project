package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	private List<User> users;
	
	public Role() {}

	public Role(String name)
	{
		this.name = name;
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}
	
	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 17 * hash + Objects.hashCode(this.id);
		hash = 17 * hash + Objects.hashCode(this.name);
		return hash;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o != null && o instanceof Role)
		{
			Role r = (Role)o;
			
			if(r.getId() != null && this.getId() != null)
				return Objects.equals(r.getId(), this.getId());
		}
		
		return false;
	}
}