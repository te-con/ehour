package net.rrm.ehour.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on Feb 7, 2010 2:25:08 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class UserMother
{
	public static User createUser()
	{
		return createUser(new UserDepartment(1));
	}
	
	public static User createUser(UserDepartment department)
	{
		User user = new User();
		
		user.setActive(true);
		user.setEmail("thies@te-con.nl");
		user.setUsername("testmetoo");
		user.setFirstName("Dummy");
		user.setLastName("TestUser");
		user.setPassword("abc");
		
		user.setUserDepartment(department);
		
		Set<UserRole> roles = new HashSet<UserRole>();
		roles.add(UserRoleMother.createUserRole());
		user.setUserRoles(roles);
		
		return user;
	}

	public static List<User> createUsers()
	{
		return MotherUtil.createMultiple(createUser(new UserDepartment(1)));
	}

	
	public static List<User> createUsers(UserDepartment department)
	{
		return MotherUtil.createMultiple(createUser(department));
	}
}
