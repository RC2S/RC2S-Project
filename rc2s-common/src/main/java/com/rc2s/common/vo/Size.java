package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	private Date created;
	private Date upated;
	
	public Size() {}

	public Size(int id, String name, int x, int y, int z, Date created, Date upated)
	{
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.created = created;
		this.upated = upated;
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

	public Date getUpated()
	{
		return upated;
	}

	public void setUpated(Date upated)
	{
		this.upated = upated;
	}
}
