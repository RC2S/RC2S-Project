package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "cube")
public class Cube implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@javax.validation.constraints.Size(min = 1, message = "You must specify a name")
	private String name;
	
	@Pattern(regexp = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$", message = "Invalid IP address")
	private String ip;
	
	@NotNull
	private String color;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "size")
	@NotNull
	private Size size;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn(name = "synchronization")
	@NotNull
	private Synchronization synchronization;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "cubes")
	private List<Synchronization> synchronizations;
	
	private Date created;
	private Date updated;
	
	public Cube() {}

	public Cube(final Integer id, final String name, final String ip, 
            final String color, final Size size, final Date created, final Date updated)
	{
		this.id = id;
		this.name = name;
		this.ip = ip;
		this.color = color;
		this.size = size;
		this.created = created;
		this.updated = updated;
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

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(final String ip)
	{
		this.ip = ip;
	}

	public String getColor()
	{
		return color;
	}

	public void setColor(final String color)
	{
		this.color = color;
	}

	public Size getSize()
	{
		return size;
	}

	public void setSize(final Size size)
	{
		this.size = size;
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

	public Synchronization getSynchronization()
	{
		return synchronization;
	}

	public void setSynchronization(final Synchronization synchronization)
	{
		this.synchronization = synchronization;
	}

	public List<Synchronization> getSynchronizations()
	{
		return synchronizations;
	}

	public void setSynchronizations(final List<Synchronization> synchronizations)
	{
		this.synchronizations = synchronizations;
	}
	
	@Override
	public String toString()
	{
		return name + " (" + ip + ")";
	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 11 * hash + Objects.hashCode(this.id);
		hash = 11 * hash + Objects.hashCode(this.name);
		hash = 11 * hash + Objects.hashCode(this.ip);
		hash = 11 * hash + Objects.hashCode(this.color);
		hash = 11 * hash + Objects.hashCode(this.size);
		hash = 11 * hash + Objects.hashCode(this.created);
		hash = 11 * hash + Objects.hashCode(this.updated);
		return hash;
	}
	
	@Override
	public boolean equals(final Object o)
	{
		if(o != null && o instanceof Cube)
		{
			Cube c = (Cube)o;
			
			if(c.getId() != null && this.getId() != null)
				return Objects.equals(c.getId(), this.getId());
		}
		
		return false;
	}
}