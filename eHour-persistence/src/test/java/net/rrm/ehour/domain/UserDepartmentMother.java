package net.rrm.ehour.domain;

import java.util.List;

/**
 * Created on Feb 7, 2010 1:50:16 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class UserDepartmentMother
{
	public static UserDepartment createUserDepartment()
	{
		UserDepartment department = new UserDepartment(1);
		department.setCode("aa");
		department.setName("bb");
		
		department.setUsers(MotherUtil.asSet(UserMother.createUsers(department)));
		return department;
	}
	
	public static List<UserDepartment> createUserDepartments()
	{
		return MotherUtil.createMultiple(createUserDepartment());
	}
}
