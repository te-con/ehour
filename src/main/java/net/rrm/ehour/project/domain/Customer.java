package net.rrm.ehour.project.domain;

import java.util.Set;

public class Customer implements java.io.Serializable
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

	private	Set		projects;
	
	// Constructors

	/** default constructor */
	public Customer()
	{
	}

	/** full constructor */
	public Customer(String code, String name, String description)
	{
		this.code = code;
		this.name = name;
		this.description = description;
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

}
