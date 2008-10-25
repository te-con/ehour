/**
 * Created on Nov 15, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.user.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
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
public class UserDepartmentDAOTest extends BaseDAOTest 
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
