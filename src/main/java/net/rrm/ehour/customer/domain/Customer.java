package net.rrm.ehour.customer.domain;

import java.util.Set;

import net.rrm.ehour.domain.DomainObject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Customer extends DomainObject implements Comparable<Customer>
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

	private	Set		projects;
	
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
	public Set getProjects()
	{
		return projects;
	}

	/**
	 * @param projects the projects to set
	 */
	public void setProjects(Set projects)
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

	public int compareTo(Customer o)
	{
		return name.compareTo(o.getName());
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

}
