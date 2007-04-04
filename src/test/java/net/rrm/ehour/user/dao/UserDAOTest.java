package net.rrm.ehour.user.dao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;

public class UserDAOTest extends BaseDAOTest 
{
	private	UserDAO	dao;

	
	public void setUserDAO(UserDAO dao)
	{
		this.dao = dao;
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
		assertEquals(3, results.size());

	}
	
	public void testFindUsers()
	{
		List<User>	results;
		
		results = dao.findAllActiveUsers();
		assertEquals(3, results.size());
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


	public void testPersist()
	{
		UserDepartment org = new UserDepartment();
		org.setDepartmentId(new Integer(1));
		
		ProjectAssignment pa = new ProjectAssignment();
		pa.setAssignmentId(1);
		Set pas = new HashSet();
		pas.add(pa);
		
		User user = new User();
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
		List results = dao.findUsersForDepartments("in", new Integer[]{1}, false);
		
		assertEquals(2, results.size());
	}
	
	public void testFindAllActiveUsersWithEmailSet()
	{
		List results = dao.findAllActiveUsersWithEmailSet();
		
		assertEquals(2, results.size());
	}
}
