package net.rrm.ehour.project.domain;

import java.util.Set;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.domain.DomainObject;

public class Project extends DomainObject
{

	// Fields    

	/**
	 * 
	 */
	private static final long serialVersionUID = 6553709211219335091L;

	private Integer projectId;

	private String projectCode;

	private String contact;

	private String description;

	private String name;

	private Boolean defaultProject;
	private	boolean	active;
	private Customer customer;
	private	Set		projectAssignments;
	// Constructors

	/** default constructor */
	public Project()
	{
	}

	
	public String getFullname()
	{
		if (projectCode != null && 
			!projectCode.equals(""))
		{
			return projectCode + " - " + name;
		}
		else
		{
			return name;
		}
			
	}	
	
	// Property accessors
	public Integer getProjectId()
	{
		return this.projectId;
	}

	public void setProjectId(Integer projectId)
	{
		this.projectId = projectId;
	}

	public String getProjectCode()
	{
		return this.projectCode;
	}

	public void setProjectCode(String projectCode)
	{
		this.projectCode = projectCode;
	}

	public String getContact()
	{
		return this.contact;
	}

	public void setContact(String contact)
	{
		this.contact = contact;
	}

	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Boolean getDefaultProject()
	{
		return this.defaultProject;
	}

	public void setDefaultProject(Boolean defaultProject)
	{
		this.defaultProject = defaultProject;
	}

	public Customer getCustomer()
	{
		return this.customer;
	}

	public void setCustomer(Customer customer)
	{
		this.customer = customer;
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

	/**
	 * @return the projectAssignments
	 */
	public Set getProjectAssignments()
	{
		return projectAssignments;
	}

	/**
	 * @param projectAssignments the projectAssignments to set
	 */
	public void setProjectAssignments(Set projectAssignments)
	{
		this.projectAssignments = projectAssignments;
	}

}
