package net.rrm.ehour.user.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;
import net.rrm.ehour.util.EhourConstants;

@SuppressWarnings("unchecked")
public class UserDAOTest extends BaseDAOTest 
{
	private	UserDAO	dao;

	
	public void setUserDAO(UserDAO dao)
	{
		this.dao = dao;
	}
	
	public void testFindUsersByPatternAndUserRole()
	{
		List<User>	results;
		
		results = dao.findUsersByNameMatch(null, true, new UserRole(EhourConstants.ROLE_CONSULTANT));;
		assertEquals(4, results.size());		
	}
	
	public void testFindUsersByPattern()
	{
		List<User>	results;
		
		results = dao.findUsersByNameMatch("thies", true);
		assertEquals(1, results.size());
		
		results = dao.findUsersByNameMatch("ede", true);
		assertEquals(2, results.size());

		results = dao.findUsersByNameMatch("zed", false);
		assertEquals(3, results.size());

		results = dao.findUsersByNameMatch("in", false);
		assertEquals(2, results.size());

		results = dao.findUsersByNameMatch("zed", true);
		assertEquals(2, results.size());

		results = dao.findUsersByNameMatch(null, true);
		assertEquals(4, results.size());

	}
	
	public void testFindUsers()
	{
		List<User>	results;
		
		results = dao.findAllActiveUsers();
		assertEquals(4, results.size());
	}
	
	/**
	 * Test method for {@link net.rrm.ehour.user.service.UserServiceImpl#getUser(java.lang.String, java.lang.String)}.
	 */
	public void testFindById()
	{
		User user = dao.findById(new Integer(1));
		
		assertEquals("thies", user.getUsername());
	}
	
	public void testFindByUsername()
	{
		User user = dao.findByUsername("thies");
		
		assertEquals("thies", user.getUsername());
	}
	
	// FIXME
	// pwd needs encryption
//	public void testFindByUsernameAndPassword()
//	{
//		User user = dao.findByUsernameAndPassword("thies", "test");
//		
//		assertEquals("thies", user.getUsername());
//	}	


	public void testPersist()
	{
		UserDepartment org = new UserDepartment();
		org.setDepartmentId(new Integer(1));
		
		ProjectAssignment pa = new ProjectAssignment();
		pa.setAssignmentId(1);
		pa.setProject(new Project(1));
		pa.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));
		Set pas = new HashSet();
		pas.add(pa);
		
		User user = new User();
		pa.setUser(user);

		user.setUsername("freggle");
		user.setPassword("in the cave");
		user.setFirstName("ollie");
		user.setLastName("doerak chief");
		user.setUserDepartment(org);
		user.setProjectAssignments(pas);
		dao.persist(user);
		
		assertNotNull(user.getUserId());
	}
	
	public void testFindUsersForDepartments()
	{
		List ids = new ArrayList();
		ids.add(1);
		
		List results = dao.findUsersForDepartments("in", ids, false);
		
		assertEquals(2, results.size());
	}
	
	public void testFindAllActiveUsersWithEmailSet()
	{
		List results = dao.findAllActiveUsersWithEmailSet();
		
		assertEquals(2, results.size());
	}
	
	// FIXME 
	
//	public void testFindUsersWithPMRoleButNoProject()
//	{
//		List<User> results = dao.findUsersWithPMRoleButNoProject();
//
//		assertEquals(2, results.size());
//	}
//	
//	public void testFindUsersWhoDontHavePMRoleButArePM()
//	{
//		List<User> results = dao.findUsersWhoDontHavePMRoleButArePM();
//		
//		System.out.println(results.get(0).getUserId());
//		System.out.println(results.get(1).getUserId());
//		assertEquals(1, results.size());
//		assertEquals(3, results.get(0).getUserId().intValue());
//	}
	
	
}
