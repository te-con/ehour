/**
 * Created on Apr 29, 2007
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

import java.util.Collections;

import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.web.sort.CustomerComparator;
import net.rrm.ehour.web.sort.ProjectComparator;
import net.rrm.ehour.web.sort.UserComparator;
import net.rrm.ehour.web.sort.UserDepartmentComparator;

/**
 * Utility methods for the UI side of the reports 
 **/

public class WebReportUtil
{
	/**
	 * Sort the available criteria
	 * @param availableCriteria
	 */
	public static void sortAvailableCriteria(AvailableCriteria availableCriteria)
	{
		if (availableCriteria.getCustomers() != null)
		{
			Collections.sort(availableCriteria.getCustomers(), new CustomerComparator());
		}
		
		if (availableCriteria.getProjects() != null)
		{
			Collections.sort(availableCriteria.getProjects(), new ProjectComparator());
		}
		
		if (availableCriteria.getUserDepartments() != null)
		{
			Collections.sort(availableCriteria.getUserDepartments(), new UserDepartmentComparator());
		}
		
		if (availableCriteria.getUsers() != null)
		{
			Collections.sort(availableCriteria.getUsers(), new UserComparator(false));
		}		
	}
}
