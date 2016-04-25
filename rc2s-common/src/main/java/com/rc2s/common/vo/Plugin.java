package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "plugin")
public class Plugin implements Serializable
{
	@Id
	@GeneratedValue
	private int id;
	
	private String name;
	private String version;
	private String author;
	private String status;
	private boolean activated;
	private String access;
	
	private Date created;
	private Date updated;
	
	public Plugin() {}

	public Plugin(int id, String name, String version, String author, String status, boolean activated, String access, Date created, Date updated)
	{
		this.id = id;
		this.name = name;
		this.version = version;
		this.author = author;
		this.status = status;
		this.activated = activated;
		this.access = access;
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

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public boolean isActivated()
	{
		return activated;
	}

	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}

	public String getAccess()
	{
		return access;
	}

	public void setAccess(String access)
	{
		this.access = access;
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
