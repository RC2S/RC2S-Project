package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cube")
public class Cube implements Serializable
{
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	private String ip;
	private String color;
	
	@ManyToOne
	private Size size;
	private Date created;
	private Date updated;
	
	public Cube() {}

	public Cube(int id, String name, String ip, String color, Size size, Date created, Date updated)
	{
		this.id = id;
		this.name = name;
		this.ip = ip;
		this.color = color;
		this.size = size;
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

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(String color)
	{
		this.color = color;
	}

	public Size getSize()
	{
		return size;
	}

	public void setSize(Size size)
	{
		this.size = size;
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
