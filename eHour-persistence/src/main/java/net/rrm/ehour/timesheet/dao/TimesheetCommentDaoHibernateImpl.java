/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.timesheet.dao;

import net.rrm.ehour.dao.AbstractGenericDaoHibernateImpl;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetCommentId;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * CRUD on timesheetComment domain obj 
 **/
@Repository("timesheetCommentDao")
public class TimesheetCommentDaoHibernateImpl 
			extends AbstractGenericDaoHibernateImpl<TimesheetComment, TimesheetCommentId>
			implements TimesheetCommentDao
{
	public TimesheetCommentDaoHibernateImpl()
	{
		super(TimesheetComment.class);
	}

	public int deleteCommentsForUser(Integer userId)
	{
		Session session = getSession();
		Query query = session.getNamedQuery("TimesheetComment.deleteUserId");
		query.setParameter("userId", userId);
		
		return query.executeUpdate();
	}	
}
