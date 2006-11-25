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

import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.web.admin.dept.form.UserDepartmentForm;

/**
 * Various form to domain assemblers
 **/

public class DomainAssembler
{
	public static UserDepartment getUserDepartment(UserDepartmentForm udf)
	{
		UserDepartment ud = new UserDepartment();
		
		if (udf.getDepartmentId() > 0)
		{
			ud.setDepartmentId(udf.getDepartmentId());
		}
		
		ud.setName(udf.getName());
		ud.setCode(udf.getCode());
		return ud;
	}

}
