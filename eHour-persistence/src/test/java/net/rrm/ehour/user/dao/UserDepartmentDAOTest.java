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

package net.rrm.ehour.user.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import net.rrm.ehour.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.domain.UserDepartment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *  
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings("unchecked")
public class UserDepartmentDAOTest extends AbstractAnnotationDaoTest 
{	
	@Autowired
	private	UserDepartmentDAO	userDepartmentDAO;

	@Test
	public void testDelete()
	{
		UserDepartment dept;

		dept = userDepartmentDAO.findById(new Integer(2));
		assertNotNull(dept);
		
		userDepartmentDAO.delete(dept);
		
		dept = userDepartmentDAO.findById(new Integer(2));
		assertNull(dept);
	}

	/**
	 * 
	 *
	 */
	@Test
	public void testFindById()
	{
		UserDepartment dept = userDepartmentDAO.findById(new Integer(2));
		assertEquals("DUMMY DEPT", dept.getName());
	}

	/**
	 * 
	 *
	 */
	@Test
	public void testGetAllDepartments()
	{
		List depts = userDepartmentDAO.findAll();
		assertEquals(2, depts.size());
	}

	@Test
	public void testPersist()
	{
		UserDepartment dept = new UserDepartment();
//		dept.setDepartmentId(new Integer(3));
		dept.setName("test dept");
		dept.setCode("code");
		userDepartmentDAO.persist(dept);
		
		UserDepartment dept2 =userDepartmentDAO.findById(dept.getDepartmentId());
		
		assertEquals("test dept", dept2.getName());
	}
	
	@Test
	public void testFindByNameAndCode()
	{
		assertNotNull(userDepartmentDAO.findOnNameAndCode("TE-CON", "TEC"));
	}
}
