package net.rrm.ehour.project.domain;

import java.util.Date;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.user.domain.User;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;

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
	
	private boolean active;
	
	// @todo move to a VO - someday..
	private	boolean deletable;

	// Constructors

	/** default constructor */
	public ProjectAssignment()
	{
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

	/** minimal constructor */
	public ProjectAssignment(User user, Project project, DateRange dateRange)
	{
		this.user = user;
		this.project = project;
		
		this.dateStart = dateRange.getDateStart();
		this.dateEnd = dateRange.getDateEnd();
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

	/**
	 * get start & end date as range
	 * @return
	 */
	public DateRange getDateRange()
	{
		return new DateRange(dateStart, dateEnd);
	}
	
	public void setDateRange(DateRange dateRange)
	{
		setDateStart(dateRange.getDateStart());
		setDateEnd(dateRange.getDateEnd());
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
										.append("user", getUser()).toString();
	}	

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
		
		return new EqualsBuilder().append(this.getAssignmentId(), castOther.getAssignmentId()).isEquals();
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
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(ProjectAssignment object)
	{
		return new CompareToBuilder()
			.append(this.project, object.project)
			.append(this.dateEnd, object.dateEnd)
			.append(this.dateStart, object.dateStart)
			.append(this.user, object.user)
			.append(this.assignmentId, object.assignmentId).toComparison();
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

}
