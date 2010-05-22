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
import org.springframework.beans.factory.annotation.Autowired;

public class UserDepartmentDaoTest extends AbstractAnnotationDaoTest 
{	
	@Autowired
	private	UserDepartmentDao	userDepartmentDao;

	@Test
	public void shouldDelete()
	{
		UserDepartment dept;

		dept = userDepartmentDao.findById(new Integer(2));
		assertNotNull(dept);
		
		userDepartmentDao.delete(dept);
		
		dept = userDepartmentDao.findById(new Integer(2));
		assertNull(dept);
	}

	@Test
	public void shouldFindById()
	{
		UserDepartment dept = userDepartmentDao.findById(new Integer(2));
		assertEquals("DUMMY DEPT", dept.getName());
	}

	@Test
	public void shouldGetAllDepartments()
	{
		List<UserDepartment> depts = userDepartmentDao.findAll();
		assertEquals(2, depts.size());
	}

	@Test
	public void shouldPersist()
	{
		UserDepartment dept = new UserDepartment();
		dept.setName("test dept");
		dept.setCode("code");
		userDepartmentDao.persist(dept);
		
		UserDepartment dept2 =userDepartmentDao.findById(dept.getDepartmentId());
		
		assertEquals("test dept", dept2.getName());
	}
	
	@Test
	public void shouldFindByNameAndCode()
	{
		assertNotNull(userDepartmentDao.findOnNameAndCode("TE-CON", "TEC"));
	}
}
