package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "size")
public class Size implements Serializable
{
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	private int x;
	private int y;
	private int z;
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<Cube> cubes;
	
	private Date created;
	private Date updated;
	
	public Size() {}

	public Size(int id, String name, int x, int y, int z, Date created, Date updated)
	{
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
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

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getZ()
	{
		return z;
	}

	public void setZ(int z)
	{
		this.z = z;
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

	public void setUpdated(Date upated)
	{
		this.updated = upated;
	}

	public List<Cube> getCubes()
	{
		return cubes;
	}

	public void setCubes(List<Cube> cubes)
	{
		this.cubes = cubes;
	}
}
