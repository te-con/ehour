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

package net.rrm.ehour.mail.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.domain.MailLog;
import net.rrm.ehour.domain.MailLogAssignment;

/**
 * DAO for MailLog db operations 
 **/

public class MailLogDAOHibernateImpl extends GenericDAOHibernateImpl<MailLog, Integer>  implements MailLogDAO
{
	/**
	 * @todo fix this a bit better
	 */
	public MailLogDAOHibernateImpl()
	{
		super(MailLog.class);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.mail.dao.MailLogDAO#getMailLogAssignment(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public List<MailLogAssignment> findMailLogOnAssignmentIds(Integer[] projectAssignmentIds)
	{
		return getHibernateTemplate().findByNamedQueryAndNamedParam("MailLogAssignment.findOnAssignmentIds",
																		"assignmentIds", projectAssignmentIds);
	}

}
