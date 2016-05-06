package com.rc2s.common.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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

@Entity
@Table(name = "cube")
public class Cube implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String ip;
	
	@NotNull
	private String color;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "size")
	@NotNull
	private Size size;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	@JoinColumn(name = "synchronization")
	@NotNull
	private Synchronization synchronization;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "cubes")
	private List<Synchronization> synchronizations;
	
	private Date created;
	private Date updated;
	
	public Cube() {}

	public Cube(Integer id, String name, String ip, String color, Size size, Date created, Date updated)
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

	public Synchronization getSynchronization()
	{
		return synchronization;
	}

	public void setSynchronization(Synchronization synchronization)
	{
		this.synchronization = synchronization;
	}

	public List<Synchronization> getSynchronizations()
	{
		return synchronizations;
	}

	public void setSynchronizations(List<Synchronization> synchronizations)
	{
		this.synchronizations = synchronizations;
	}
	
	@Override
	public String toString()
	{
		return name + " (" + ip + ")";
	}
}
