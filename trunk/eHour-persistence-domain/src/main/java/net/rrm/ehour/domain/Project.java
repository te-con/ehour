package net.rrm.ehour.domain;

import java.util.Set;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Project extends DomainObject<Integer, Project>
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
	private	Set<ProjectAssignment>		projectAssignments;
	private User	projectManager;
	private boolean	deletable;
	
	// Constructors

	/** default constructor */
	public Project()
	{
	}

	public Project(Integer projectId)
	{
		this.projectId = projectId;
	}
	
	public String getFullName()
	{
		return (StringUtils.isBlank(projectCode)) ? name : projectCode + " - " + name; 
	}	
	
	/**
	 * Get primary key
	 */
	public Integer getPK()
	{
		return projectId;
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

	public void setDefaultProject(boolean defaultProject)
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
	public Set<ProjectAssignment> getProjectAssignments()
	{
		return projectAssignments;
	}

	/**
	 * @param projectAssignments the projectAssignments to set
	 */
	public void setProjectAssignments(Set<ProjectAssignment> projectAssignments)
	{
		this.projectAssignments = projectAssignments;
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

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Project object)
	{
		return new CompareToBuilder()
			.append(this.getName(), object.getName())
			.append(this.getProjectCode(), object.getProjectCode())
			.append(this.getProjectId(), object.getProjectId())
			.append(this.getCustomer(), object.getCustomer()).toComparison();
	}

	/**
	 * @return the projectManager
	 */
	public User getProjectManager()
	{
		return projectManager;
	}

	/**
	 * @param projectManager the projectManager to set
	 */
	public void setProjectManager(User projectManager)
	{
		this.projectManager = projectManager;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new ToStringBuilder(this).append("active", this.active)
				.append("PK", this.getPK())
				.append("defaultProject", this.defaultProject)
				.append("fullname", this.getFullName())
				.append("projectCode", this.getProjectCode())
				.append("name", this.getName())
				.append("projectId", this.getProjectId())
				.toString();
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
