package net.rrm.ehour.project.domain;

import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.domain.DomainObject;

public class Project extends DomainObject implements Comparable<Project>
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

	private boolean defaultProject;
	private	boolean	active;
	private Customer customer;
	private	Set		projectAssignments;
	// Constructors

	/** default constructor */
	public Project()
	{
	}

	public Project(Integer projectId)
	{
		this.projectId = projectId;
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

	public boolean isDefaultProject()
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

	
	/**
	 * Satisfy the Comparable contract
	 */
	public int compareTo(Project o)
	{
		int	customerCompare = customer.compareTo(o.getCustomer());
		
		if (customerCompare == 0)
		{
			return name.compareTo(o.getName());
		}
		
		return customerCompare;
	}
	
	/**
	 * Equals
	 */
	@Override
	public boolean equals(Object other)
	{
		if ((this == other))
		{
			return true;
		}
		
		if (!(other instanceof Project))
		{
			return false;
		}
		
		Project castOther = (Project) other;
		
		if (castOther.getProjectId() == null && this.getProjectId() != null)
		{
			return false;
		}
		
		return new EqualsBuilder().append(this.getProjectId(), castOther.getProjectId()).isEquals();
	}

	/**
	 * 
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getProjectId()).toHashCode();
	}	

}
