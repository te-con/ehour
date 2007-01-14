package net.rrm.ehour.user.domain;

import java.util.HashSet;
import java.util.Set;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.project.domain.ProjectAssignment;

/**
 * @author  Thies
 */
public class User  extends DomainObject implements Comparable
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

	
    public int compareTo(Object o)
    {
        User other;

        if (o instanceof User)
        {
            other = (User)o;
            // just use last name for sorting
            return other.getLastName().compareTo(this.getLastName());
        }
        else
        {
            return -1;
        }
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
}
