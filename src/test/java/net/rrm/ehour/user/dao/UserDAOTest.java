package net.rrm.ehour.user.dao;

import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
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
		
		results = dao.findUsersByNameMatch("thies");
		assertEquals(1, results.size());
		
		results = dao.findUsersByNameMatch("ede");
		assertEquals(2, results.size());

		results = dao.findUsersByNameMatch("zed");
		assertEquals(2, results.size());
	}
	
	public void testFindUsers()
	{
		List<User>	results;
		
		results = dao.findUsers();
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
		
		User user = new User();
		user.setUsername("freggle");
		user.setPassword("in the cave");
		user.setFirstName("ollie");
		user.setLastName("doerak chief");
		user.setDepartment(org);
		dao.persist(user);
		
		assertNotNull(user.getUserId());
	}
}
