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

package net.rrm.ehour.persistence.mail.dao;

import java.util.List;

import net.rrm.ehour.domain.MailLog;
import net.rrm.ehour.domain.MailLogAssignment;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateImpl;

import org.springframework.stereotype.Repository;

/**
 * DAO for MailLog db operations 
 **/
@Repository("mailLogDao")
public class MailLogDaoHibernateImpl extends AbstractGenericDaoHibernateImpl<MailLog, Integer>  implements MailLogDao
{
	public MailLogDaoHibernateImpl()
	{
		super(MailLog.class);
	}

	@SuppressWarnings("unchecked")
	public List<MailLogAssignment> findMailLogOnAssignmentIds(Integer[] projectAssignmentIds)
	{
		return getHibernateTemplate().findByNamedQueryAndNamedParam("MailLogAssignment.findOnAssignmentIds",
																		"assignmentIds", projectAssignmentIds);
	}

}
