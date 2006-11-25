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

import junit.framework.TestCase;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.web.admin.dept.form.UserDepartmentForm;

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

}
