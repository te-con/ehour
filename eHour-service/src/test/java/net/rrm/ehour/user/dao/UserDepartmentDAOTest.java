/**
 * Created on Nov 15, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.user.dao;

import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.user.domain.UserDepartment;

/**
 *  
 **/

public class UserDepartmentDAOTest extends BaseDAOTest 
{
	private	UserDepartmentDAO	dao;

	
	public void setUserDepartmentDAO(UserDepartmentDAO dao)
	{
		this.dao = dao;
	}
	

	public void testDelete()
	{
		UserDepartment dept;

		dept = dao.findById(new Integer(2));
		assertNotNull(dept);
		
		dao.delete(dept);
		
		dept = dao.findById(new Integer(2));
		assertNull(dept);
	}

	/**
	 * 
	 *
	 */
	public void testFindById()
	{
		UserDepartment dept = dao.findById(new Integer(2));
		assertEquals("DUMMY DEPT", dept.getName());
	}

	/**
	 * 
	 *
	 */
	public void testGetAllDepartments()
	{
		List depts = dao.findAll();
		assertEquals(2, depts.size());
	}

	public void testPersist()
	{
		UserDepartment dept = new UserDepartment();
		dept.setDepartmentId(new Integer(3));
		dept.setName("test dept");
		dept.setCode("code");
		dao.persist(dept);
		
		dept =dao.findById(new Integer(3));
		
		assertEquals("test dept", dept.getName());
	}
	
	public void testFindByNameAndCode()
	{
		assertNotNull(dao.findOnNameAndCode("TE-CON", "TEC"));
	}
}
