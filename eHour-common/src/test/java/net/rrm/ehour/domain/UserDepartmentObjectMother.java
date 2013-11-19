package net.rrm.ehour.domain;

import com.google.common.collect.Sets;

/**
 * Created on Feb 7, 2010 1:50:16 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class UserDepartmentObjectMother
{
	public static UserDepartment createUserDepartment()
	{
		UserDepartment department = new UserDepartment(1);
		department.setCode("aa");
		department.setName("bb");
		
		department.setUsers(Sets.newHashSet(UserObjectMother.createUsers(department)));
		return department;
	}

}
