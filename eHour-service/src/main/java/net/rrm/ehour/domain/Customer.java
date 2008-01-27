package net.rrm.ehour.domain;

import java.util.HashSet;
import java.util.Set;


import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Customer extends DomainObject<Integer, Customer>
{

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7179070624535327915L;

	private Integer customerId;

	private String code;

	private String name;

	private String description;
	
	private	boolean	active;

	private	Set<Project>		projects;
	
	private boolean deletable;
	
	// Constructors

	@Override
	public String toString()
	{
		return new ToStringBuilder(this)
			.append("customerId", customerId)
			.append("code", code)
			.append("name", name)
			.append("active", active)
			.toString();		
	}
	
	/** default constructor */
	public Customer()
	{
	}
	
	public Customer(Integer customerId)
	{
		this.customerId = customerId;
	}

	/** full constructor */
	public Customer(String code, String name, String description, boolean active)
	{
		this.code = code;
		this.name = name;
		this.description = description;
		this.active = active;
	}

	/** full constructor */
	public Customer(Customer customer)
	{
		this.customerId = customer.getCustomerId();
		this.code = customer.getCode();
		this.name = customer.getName();
		this.description = customer.getDescription();
		this.active = customer.isActive();
	}	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#getFullName()
	 */
	@Override
	public String getFullName()
	{
		return (getCode() != null) ?  getCode() + " - " + getName() : getName();
	}
	
	// Property accessors
	public Integer getCustomerId()
	{
		return this.customerId;
	}

	public void setCustomerId(Integer customerId)
	{
		this.customerId = customerId;
	}

	public String getCode()
	{
		return this.code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the projects
	 */
	public Set<Project> getProjects()
	{
		return projects;
	}
	
	/**
	 * Get only active projects
	 * @return
	 */
	public Set<Project> getActiveProjects()
	{
		Set<Project> activeProjects = new HashSet<Project>();
		
		for (Project project : getProjects())
		{
			if (project.isActive())
			{
				activeProjects.add(project);
			}
		}
		
		return activeProjects;
		
	}

	/**
	 * @param projects the projects to set
	 */
	public void setProjects(Set<Project> projects)
	{
		this.projects = projects;
	}

	/**
	 * @return the active
	 */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active)
	{
		this.active = active;
	}

	
	@Override
	public boolean equals(Object other)
	{
		Customer 	castOther;
		
		if (other instanceof Customer)
		{
			castOther = (Customer)other;
			return new EqualsBuilder().append(this.getCustomerId(), castOther.getCustomerId()).isEquals();
		}
		else
		{
			return false;
		}
	}	
	
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getCustomerId()).toHashCode();
	}

	@Override
	public Integer getPK()
	{
		return customerId;
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Customer object)
	{
		return new CompareToBuilder()
		.append(this.name, object.name)
			.append(this.customerId, object.customerId)
			.append(this.code, object.code).toComparison();
	}

	/**
	 * @return the deletable
	 */
	public boolean isDeletable()
	{
		return deletable;
	}

	/**
	 * @param deletable the deletable to set
	 */
	public void setDeletable(boolean deletable)
	{
		this.deletable = deletable;
	}	

}
