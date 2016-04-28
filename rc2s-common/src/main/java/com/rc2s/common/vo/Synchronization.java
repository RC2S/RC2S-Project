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
@Table(name = "synchronization")
public class Synchronization implements Serializable
{
	@Id
	@GeneratedValue
	private int id;
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
	
	public Synchronization(int id, String name, Date created, Date updated)
	{
		this.id = id;
		this.name = name;
		this.created = created;
		this.updated = updated;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
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

	public List<Cube> getCubes()
	{
		return cubes;
	}

	public void setCubes(List<Cube> cubes)
	{
		this.cubes = cubes;
	}

	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
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
