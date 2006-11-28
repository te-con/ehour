package net.rrm.ehour.user.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author  Thies
 */
public class User implements Serializable, Comparable
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

	private Set userRoles = new HashSet(0);

	private UserDepartment userDepartment;

	// Constructors

	/** default constructor */
	public User()
	{
	}

	/** minimal constructor */
	public User(UserDepartment userDepartment)
	{
		this.userDepartment = userDepartment;
	}

	public User(String username, String password)
	{
		this.username = username;
		this.password = password;
	}
	
	/** full constructor */
	public User(String username, String password, String firstName, String lastName, String email, Set userRoles, UserDepartment userDepartment)
	{
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
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

	public Set getUserRoles()
	{
		return this.userRoles;
	}

	public void setUserRoles(Set userRoles)
	{
		this.userRoles = userRoles;
	}

	public UserDepartment getDepartment()
	{
		return this.userDepartment;
	}

	public void setDepartment(UserDepartment userDepartment)
	{
		this.userDepartment = userDepartment;
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
}
