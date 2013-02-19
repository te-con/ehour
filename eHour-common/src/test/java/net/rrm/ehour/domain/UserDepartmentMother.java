package net.rrm.ehour.domain;

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
		
		department.setUsers(MotherUtil.asSet(UserObjectMother.createUsers(department)));
		return department;
	}

}
