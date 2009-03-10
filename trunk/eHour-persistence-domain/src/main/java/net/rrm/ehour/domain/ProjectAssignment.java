package net.rrm.ehour.domain;

import java.util.Date;

import net.rrm.ehour.util.EhourConstants;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ProjectAssignment extends DomainObject<Integer, ProjectAssignment>
{

	// Fields    

	/**
	 * 
	 */
	private static final long serialVersionUID = -2396783805401137165L;

	private Integer assignmentId;

	private User user;

	private Project project;

	private Float hourlyRate;

	private Date dateStart;

	private Date dateEnd;

	private String role;
	
	private	ProjectAssignmentType assignmentType;
	
	private	Float	allottedHours;
	
	private Float 	allowedOverrun;
	
	private boolean	notifyPm = false;
	
	private boolean active;
	
	// @todo move to a VO - someday..
	private	boolean deletable;

	// Constructors

	/** default constructor */
	public ProjectAssignment()
	{
	}

	public ProjectAssignment(Integer assignmentId)
	{
		this.assignmentId = assignmentId;
	}
	
	/** minimal constructor */
	public ProjectAssignment(User user, Project project)
	{
		this.user = user;
		this.project = project;
	}
	
	
	/** minimal constructor */
	public ProjectAssignment(User user, Project project, Date dateStart, Date dateEnd)
	{
		this.user = user;
		this.project = project;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}
	
	/** full constructor */
	public ProjectAssignment(User user, Project project, Float hourlyRate, Date dateStart, Date dateEnd, String description)
	{
		this.user = user;
		this.project = project;
		this.hourlyRate = hourlyRate;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.role = description;
	}
	
	
	/**
	 * Create a project assignment with default values (date assignment, no start/end date, active)s
	 * @param project
	 * @param user
	 * @return
	 */
	public static ProjectAssignment createProjectAssignment(Project project, User user)
	{
		ProjectAssignment assignment = new ProjectAssignment();
		assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));
		assignment.setProject(project);
		assignment.setUser(user);
		assignment.setActive(true);
		
		return assignment;
		
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#getFullName()
	 */
	@Override
	public String getFullName()
	{
		return getProject().getFullName();
	}
	
	// Property accessors
	public Integer getAssignmentId()
	{
		return this.assignmentId;
	}

	public void setAssignmentId(Integer assignmentId)
	{
		this.assignmentId = assignmentId;
	}

	public User getUser()
	{
		return this.user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public Project getProject()
	{
		return this.project;
	}

	public void setProject(Project project)
	{
		this.project = project;
	}

	public Float getHourlyRate()
	{
		return this.hourlyRate;
	}

	public void setHourlyRate(Float hourlyRate)
	{
		this.hourlyRate = hourlyRate;
	}
	
	public Date getDateStart()
	{
		return this.dateStart;
	}
	

	public void setDateStart(Date dateStart)
	{
		this.dateStart = dateStart;
	}

	public Date getDateEnd()
	{
		return this.dateEnd;
	}


	public void setDateEnd(Date dateEnd)
	{
		this.dateEnd = dateEnd;
	}

	public String getRole()
	{
		return this.role;
	}

	public void setRole(String description)
	{
		this.role = description;
	}


	public String toString()
	{
		return new ToStringBuilder(this).append("assignmentId", getAssignmentId())
										.append("project", getProject())
										.append("user", getUser())
//										.append("type", getAssignmentType())		
										.append("dateStart", getDateStart())
										.append("dateEnd", getDateEnd())
										.toString();
	}	

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(ProjectAssignment object)
	{
		return new CompareToBuilder()
			.append(this.getProject(), object.getProject())
			.append(this.getDateEnd(), object.getDateEnd())
			.append(this.getDateStart(), object.getDateStart())
			.append(this.getUser(), object.getUser())
			.append(this.getAssignmentId(), object.getAssignmentId())
			.toComparison();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#equals(java.lang.Object)
	 */
	public boolean equals(Object other)
	{
		if ((this == other))
			return true;
		if (!(other instanceof ProjectAssignment))
			return false;
		ProjectAssignment castOther = (ProjectAssignment) other;
		
		if (castOther.getAssignmentId() == null)
		{
			return false;
		}
		
		if (this.getAssignmentId() == null)
		{
			return false;
		}
		
		return new EqualsBuilder()
			.append(this.getAssignmentId(), castOther.getAssignmentId())
			.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getAssignmentId()).toHashCode();
	}

	public boolean isDeletable()
	{
		return deletable;
	}

	public void setDeletable(boolean deletable)
	{
		this.deletable = deletable;
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
	public Integer getPK()
	{
		return assignmentId;
	}

	/**
	 * @return the allottedHours
	 */
	public Float getAllottedHours()
	{
		return allottedHours;
	}

	/**
	 * @param allottedHours the allottedHours to set
	 */
	public void setAllottedHours(Float allottedHours)
	{
		this.allottedHours = allottedHours;
	}

	/**
	 * @return the assignmentType
	 */
	public ProjectAssignmentType getAssignmentType()
	{
		return assignmentType;
	}

	/**
	 * @param assignmentType the assignmentType to set
	 */
	public void setAssignmentType(ProjectAssignmentType assignmentType)
	{
		this.assignmentType = assignmentType;
	}

	/**
	 * @return the allowedOverrun
	 */
	public Float getAllowedOverrun()
	{
		return allowedOverrun;
	}

	/**
	 * @param allowedOverrun the allowedOverrun to set
	 */
	public void setAllowedOverrun(Float allowedOverrun)
	{
		this.allowedOverrun = allowedOverrun;
	}

	/**
	 * @return the notifyPm
	 */
	public boolean isNotifyPm()
	{
		return notifyPm;
	}

	/**
	 * @param notifyPm the notifyPm to set
	 */
	public void setNotifyPm(boolean notifyPm)
	{
		this.notifyPm = notifyPm;
	}

}
