/**
 * Created on Feb 17, 2007
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

package net.rrm.ehour.web.report.util;

import java.text.ParseException;
import java.util.Date;

import junit.framework.TestCase;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.web.report.form.ReportCriteriaForm;

/**
 * TODO 
 **/

public class UserCriteriaAssemblerTest extends TestCase
{

	protected void setUp() throws Exception
	{
		super.setUp();
	}

	public void testGetUserCriteria() throws ParseException
	{
		ReportCriteriaForm	rcForm = new ReportCriteriaForm();
		
		rcForm.setDateStart("2006-9-5");
		rcForm.setDateEnd("1974-09-01");
		rcForm.setProjectId(new Integer[]{1, 2, -1});
		rcForm.setCustomerId(new Integer[]{1,2,3});
		rcForm.setOnlyActiveCustomers(false);
		rcForm.setOnlyActiveProjects(true);
		
		UserCriteria uc = UserCriteriaAssembler.getUserCriteria(rcForm);
		
		assertEquals(new Date(2006 - 1900, 9 - 1, 5), uc.getReportRange().getDateStart());
		assertEquals(new Date(1974 - 1900, 9 - 1, 1, 23, 59, 59), uc.getReportRange().getDateEnd());
		
		assertNull(uc.getProjectIds());
		assertEquals(3, uc.getCustomerIds().length);
		assertFalse(uc.isOnlyActiveCustomers());
		assertTrue(uc.isOnlyActiveProjects());
	}

}
