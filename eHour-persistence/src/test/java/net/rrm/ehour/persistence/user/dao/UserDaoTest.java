/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.persistence.user.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.util.EhourConstants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserDaoTest extends AbstractAnnotationDaoTest 
{
	@Autowired
	private	UserDao	userDAO;
	
	@Test
	public void shouldFindUsersByPattern()
	{
		List<User>	results;
		
		results = userDAO.findUsersByNameMatch("thies", true);
		assertEquals(1, results.size());
		
		results = userDAO.findUsersByNameMatch("ede", true);
		assertEquals(2, results.size());

		results = userDAO.findUsersByNameMatch("zed", false);
		assertEquals(3, results.size());

		results = userDAO.findUsersByNameMatch("in", false);
		assertEquals(2, results.size());

		results = userDAO.findUsersByNameMatch("zed", true);
		assertEquals(2, results.size());

		results = userDAO.findUsersByNameMatch(null, true);
		assertEquals(4, results.size());

	}
	
	@Test
	public void shouldFindUsers()
	{
		List<User>	results;
		
		results = userDAO.findAllActiveUsers();
		assertEquals(4, results.size());
	}
	
	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.user.service.UserServiceImpl#getUser(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void shouldFindById()
	{
		User user = userDAO.findById(new Integer(1));
		
		assertEquals("thies", user.getUsername());
	}
	
	@Test
	public void shouldFindByUsername()
	{
		User user = userDAO.findByUsername("thies");
		
		assertEquals("thies", user.getUsername());
	}

	@Test
	public void shouldPersist()
	{
		UserDepartment org = new UserDepartment();
		org.setDepartmentId(new Integer(1));
		
		ProjectAssignment pa = new ProjectAssignment();
		pa.setAssignmentId(1);
		pa.setProject(new Project(1));
		pa.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));
		Set<ProjectAssignment> assignments = new HashSet<ProjectAssignment>();
		assignments.add(pa);
		
		User user = new User();
		pa.setUser(user);

		user.setUsername("freggle");
		user.setPassword("in the cave");
		user.setFirstName("ollie");
		user.setLastName("doerak chief");
		user.setUserDepartment(org);
		user.setProjectAssignments(assignments);
		userDAO.persist(user);
		
		assertNotNull(user.getUserId());
	}
	
	@Test
	public void shouldFindUsersForDepartments()
	{
		List<UserDepartment> ids = new ArrayList<UserDepartment>();
		ids.add(new UserDepartment(1));
		
		List<User> results = userDAO.findUsersForDepartments("in", ids, false);
		
		assertEquals(2, results.size());
	}
	
	@Test
	public void shouldFindAllActiveUsersWithEmailSet()
	{
		List<User> results = userDAO.findAllActiveUsersWithEmailSet();
		
		assertEquals(2, results.size());
	}
	
	@Test
	public void shouldDeletePmWithoutProject()
	{
		userDAO.deletePmWithoutProject();
	}	
}
