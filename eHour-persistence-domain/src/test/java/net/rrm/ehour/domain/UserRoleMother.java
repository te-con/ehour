package net.rrm.ehour.domain;

/**
 * Created on Feb 7, 2010 2:25:40 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class UserRoleMother
{
	public static UserRole createUserRole()
	{
		return new UserRole("ROLE_ADMIN");
	}
}
