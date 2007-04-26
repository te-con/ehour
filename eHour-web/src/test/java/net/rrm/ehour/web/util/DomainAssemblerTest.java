/**
 * Created on Nov 24, 2006
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

package net.rrm.ehour.web.util;

import java.text.ParseException;
import java.util.Date;

import junit.framework.TestCase;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.timesheet.domain.TimesheetComment;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;
import net.rrm.ehour.web.admin.assignment.form.ProjectAssignmentForm;
import net.rrm.ehour.web.admin.customer.form.CustomerForm;
import net.rrm.ehour.web.admin.dept.form.UserDepartmentForm;
import net.rrm.ehour.web.admin.project.form.ProjectForm;
import net.rrm.ehour.web.admin.user.form.UserForm;
import net.rrm.ehour.web.timesheet.form.TimesheetForm;

/**
 * TODO 
 **/

public class DomainAssemblerTest extends TestCase
{

	protected void setUp() throws Exception
	{
		super.setUp();
	}

	public void testGetUserDepartment()
	{
		UserDepartmentForm udf = new UserDepartmentForm();
		udf.setDepartmentId(1);
		udf.setName("name");
		udf.setCode("code");
		
		UserDepartment ud = DomainAssembler.getUserDepartment(udf);
		
		assertEquals(new Integer(1), ud.getDepartmentId());
		assertEquals("name", ud.getName());
		assertEquals("code", ud.getCode());
	}
	
	public void testGetCustomer()
	{
		CustomerForm cf = new CustomerForm();
		cf.setCustomerId(1);
		cf.setName("name");
		cf.setCode("code");
		cf.setDescription("desc");
		cf.setActive(true);
		
		Customer cust = DomainAssembler.getCustomer(cf);
		
		assertEquals(new Integer(1), cust.getCustomerId());
		assertEquals("name", cust.getName());
		assertEquals("code", cust.getCode());
		assertEquals("desc", cust.getDescription());
		assertTrue(cust.isActive());
	}
	
	public void testGetUser()
	{
		UserForm uf = new UserForm();
		uf.setUserId(1);
		uf.setEmail("thies@rrm.net");
		uf.setFirstName("Thies");
		uf.setLastName("Edeling");
		uf.setPassword("password");
		uf.setDepartmentId(1);
		uf.setRoles(new String[]{"gustas"});
		uf.setUsername("thies");
		uf.setActive(false);
		
		User user = DomainAssembler.getUser(uf);

		assertEquals(new Integer(1), user.getUserId());
		assertEquals("thies@rrm.net", user.getEmail());
		assertEquals("Thies", user.getFirstName());
		assertEquals("Edeling", user.getLastName());
		assertEquals("password", user.getPassword());
		assertEquals(new Integer(1), user.getUserDepartment().getDepartmentId());
		assertEquals("thies", user.getUsername());
		assertFalse(user.isActive());
		assertEquals("gustas", ((UserRole)user.getUserRoles().iterator().next()).getRole());
	}
	
	public void testGetProject()
	{
		ProjectForm pf = new ProjectForm();
		pf.setActive(false);
		pf.setContact("contact");
		pf.setCustomerId(2);
		pf.setDefaultProject(true);
		pf.setDescription("description");
		pf.setName("name");
		pf.setProjectCode("projectcode");
		pf.setProjectId(2);
		pf.setProjectManagerId(-1);
		
		Project prj = DomainAssembler.getProject(pf);
		
		assertFalse(prj.isActive());
		assertEquals("contact", prj.getContact());
		assertEquals(new Integer(2), prj.getCustomer().getCustomerId());
		assertTrue(prj.isDefaultProject());
		assertEquals("description", prj.getDescription());
		assertEquals("name", prj.getName());
		assertEquals("projectcode", prj.getProjectCode());
		assertEquals(new Integer(2), prj.getProjectId());
	}
	
	public void testGetProjectAssignment() throws ParseException
	{
		ProjectAssignmentForm paf = new ProjectAssignmentForm();
		String strA = "2006-10-3";
		String strB = "2006-10-1";
		
		paf.setAssignmentId(1);
		paf.setDateEnd(strB);
		paf.setDateStart(strA);
		paf.setHourlyRate(90f);
		paf.setProjectId(2);
		paf.setUserId(3);
		paf.setRole("d");
		
		ProjectAssignment pa = DomainAssembler.getProjectAssignment(paf);
		
		assertEquals(new Integer(3), pa.getUser().getUserId());
		assertEquals(new Integer(2), pa.getProject().getProjectId());

		assertEquals(90f, pa.getHourlyRate());
		assertEquals(106, pa.getDateStart().getYear() );
		assertEquals(9, pa.getDateStart().getMonth() );
		assertEquals(3, pa.getDateStart().getDate());
		assertEquals(new Integer(1), pa.getAssignmentId());
		assertEquals("d", pa.getRole());
	}
	
	public void testGetTimesheetComment()
	{
		TimesheetForm	form;
		
		form = new TimesheetForm();
		form.setComment("comment");
		form.setSheetYear(2007);
		form.setSheetDay(1);
		form.setSheetMonth(9);
		
		TimesheetComment tc = DomainAssembler.getTimesheetComment(1, form);
		
		assertEquals("comment", tc.getComment());
		assertEquals(new Integer(1), tc.getCommentId().getUserId());
		assertEquals(new Date(2007 - 1900, 9 -1, 1, 0, 0, 0), tc.getCommentId().getCommentDate());
	}
	
	public void testGetTimesheetEntry()
	{
		Date x = new Date(2007 - 1900, 9 - 1, 1, 0, 0, 0);
		
		TimesheetEntry entry = DomainAssembler.getTimesheetEntry(new Integer(1), x, new Float(5.0f));
		
		assertEquals(new Integer(1), entry.getEntryId().getProjectAssignment().getAssignmentId());
		assertEquals(x, entry.getPK().getEntryDate());
		assertEquals(new Float(5.0f), entry.getHours());
	}
}
