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

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetCommentId;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * CRUD on timesheetComment domain obj 
 **/

public class TimesheetCommentDAOHibernateImpl 
			extends GenericDAOHibernateImpl<TimesheetComment, TimesheetCommentId>
			implements TimesheetCommentDAO
{
	/**
	 * @todo fix this a bit better
	 */
	public TimesheetCommentDAOHibernateImpl()
	{
		super(TimesheetComment.class);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.dao.TimesheetCommentDAO#deleteCommentsForUser(net.rrm.ehour.user.domain.User)
	 */
	public int deleteCommentsForUser(Integer userId)
	{
		Session session = getSession();
		Query	query = session.getNamedQuery("TimesheetComment.deleteUserId");
		query.setParameter("userId", userId);
		
		int rowCount = query.executeUpdate();
		
		return rowCount;
	}	

}
