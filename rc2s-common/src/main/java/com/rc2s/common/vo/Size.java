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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "size")
public class Size implements Serializable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private String name;
	
	@Min(1)
	private Integer x;
	
	@Min(1)
	private Integer y;
	
	@Min(1)
	private Integer z;
	
	@OneToMany(fetch = FetchType.LAZY)
	private List<Cube> cubes;
	
	private Date created;
	private Date updated;
	
	public Size() {}

	public Size(Integer id, String name, Integer x, Integer y, Integer z, Date created, Date updated)
	{
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
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

	public Integer getX()
	{
		return x;
	}

	public void setX(Integer x)
	{
		this.x = x;
	}

	public Integer getY()
	{
		return y;
	}

	public void setY(Integer y)
	{
		this.y = y;
	}

	public Integer getZ()
	{
		return z;
	}

	public void setZ(Integer z)
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
	
	@Override
	public String toString()
	{
		return new StringBuilder(name).append(" (")
									  .append(x)
									  .append("x")
									  .append(y)
									  .append("x")
									  .append(z)
									  .append(")")
									  .toString();
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 37 * hash + Objects.hashCode(this.id);
		hash = 37 * hash + Objects.hashCode(this.name);
		hash = 37 * hash + Objects.hashCode(this.x);
		hash = 37 * hash + Objects.hashCode(this.y);
		hash = 37 * hash + Objects.hashCode(this.z);
		hash = 37 * hash + Objects.hashCode(this.created);
		hash = 37 * hash + Objects.hashCode(this.updated);
		return hash;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o != null && o instanceof Size)
		{
			Size s = (Size)o;
			
			if(s.getId() != null && this.getId() != null)
				return Objects.equals(s.getId(), this.getId());
		}
		
		return false;
	}
}