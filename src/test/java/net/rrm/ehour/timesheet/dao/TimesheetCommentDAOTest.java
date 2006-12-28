/**
 * Created on Dec 28, 2006
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

package net.rrm.ehour.timesheet.dao;

import java.util.Date;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.timesheet.domain.TimesheetComment;

/**
 * TODO 
 **/

public class TimesheetCommentDAOTest extends BaseDAOTest
{ 
	private TimesheetCommentDAO	dao;

	/**
	 * @param dao the dao to set
	 */
	public void setDao(TimesheetCommentDAO dao)
	{
		this.dao = dao;
	}
	
	/**
	 * 
	 *
	 */
	public void testGetTimesheetEntriesInRange()
	{
		DateRange	range;
		List<TimesheetComment> comments;
		
		range = new DateRange(new Date(2006 - 1900, 10 - 1, 1),
							  new Date(2006 - 1900, 10 - 1, 8));
		
		comments = dao.findForUserInRage(1, range);
		
		assertEquals(2, comments.size());
	}
}
