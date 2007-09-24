package net.rrm.ehour.user.domain;

import java.util.HashSet;
import java.util.Set;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.project.domain.ProjectAssignment;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @author  Thies
 */
public class User extends DomainObject<Integer, User>
{

	// Fields    

	/**
	 * 
	 */
	private static final long serialVersionUID = 2546435367535412269L;

	private Integer userId;

	private String username;

	private String password;

	private String firstName;

	private String lastName;

	private String email;
	
	private	boolean	active;

	private Set<UserRole> userRoles = new HashSet<UserRole>();

	private UserDepartment userDepartment;
	
	private	Set<ProjectAssignment>	projectAssignments;
	private	Set<ProjectAssignment>	inactiveProjectAssignments;
	
	private boolean	deletable;

	// Constructors

	/** default constructor */
	public User()
	{
	}

	/** minimal constructors */
	public User(UserDepartment userDepartment)
	{
		this.userDepartment = userDepartment;
	}
	
	public User(Integer userId)
	{
		this.userId = userId;
	}

	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
	public User(Integer userId, String firstName, String lastName)
	{
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	/** full constructor */
	public User(String username, String password, String firstName, String lastName, String email, boolean active, Set<UserRole> userRoles, UserDepartment userDepartment)
	{
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.active = active;
		this.userRoles = userRoles;
		this.userDepartment = userDepartment;
	}

	public void addUserRole(UserRole role)
	{
		if (userRoles == null)
		{
			userRoles = new HashSet<UserRole>();
		}
		userRoles.add(role);
	}
	
	/**
	 * Get fullname
	 * @return
	 */
	public String getFullName()
	{
		return (lastName != null ? lastName + ", ": "") 
					+ (firstName != null ? firstName : "");
	}
	
	// Property accessors
	public Integer getUserId()
	{
		return this.userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getFirstName()
	{
		return this.firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return this.lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Set<UserRole> getUserRoles()
	{
		return this.userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles)
	{
		this.userRoles = userRoles;
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
	 * @return the userDepartment
	 */
	public UserDepartment getUserDepartment()
	{
		return userDepartment;
	}

	/**
	 * @param userDepartment the userDepartment to set
	 */
	public void setUserDepartment(UserDepartment userDepartment)
	{
		this.userDepartment = userDepartment;
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
	 * @return the inactiveProjectAssignments
	 */
	public Set<ProjectAssignment> getInactiveProjectAssignments()
	{
		return inactiveProjectAssignments;
	}
	
	public void addProjectAssignment(ProjectAssignment projectAssignment)
	{
		if (projectAssignments == null)
		{
			projectAssignments = new HashSet<ProjectAssignment>();
		}
		
		projectAssignments.add(projectAssignment);
	}

	/**
	 * @param inactiveProjectAssignments the inactiveProjectAssignments to set
	 */
	public void setInactiveProjectAssignments(Set<ProjectAssignment> inactiveProjectAssignments)
	{
		this.inactiveProjectAssignments = inactiveProjectAssignments;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof User))
		{
			return false;
		}
		User rhs = (User) object;
		return new EqualsBuilder()
					.append(this.userId, rhs.userId)
					.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder(-2038170721, -475387721)
				.append(this.userId)
				.toHashCode();
	}

	@Override
	public Integer getPK()
	{
		return userId;
	}
	
	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("userId", userId)
						.append("username", username)
						.append("lastName", lastName)
						.append("firstName", firstName)
						.toString();
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(User object)
	{
		return new CompareToBuilder()
			.append(this.lastName, object.lastName)
			.append(this.firstName, object.firstName)
			.append(this.userDepartment, object.userDepartment)
			.append(this.userId, object.userId).toComparison();
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
