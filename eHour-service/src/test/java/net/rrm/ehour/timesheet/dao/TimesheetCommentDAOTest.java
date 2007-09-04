/**
 * Created on Dec 28, 2006
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

package net.rrm.ehour.timesheet.dao;

import java.util.Date;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.timesheet.domain.TimesheetComment;
import net.rrm.ehour.timesheet.domain.TimesheetCommentId;

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
		TimesheetComment	comment;
		
		comment = dao.findById(new TimesheetCommentId(1, new Date(2007 - 1900, 1 - 1, 7)));
		
		assertNotNull(comment);
	}
	
	public void testDeleteOnUser()
	{
		int rowCount = dao.deleteCommentsForUser(1);
		assertEquals(2, rowCount);
	}
}
