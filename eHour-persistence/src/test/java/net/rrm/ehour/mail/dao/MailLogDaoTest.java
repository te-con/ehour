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

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.rrm.ehour.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.domain.MailLogAssignment;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MailLogDaoTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private MailLogDao mailLogDao;
	
	public MailLogDaoTest()
	{
		super("dataset-maillog.xml");
	}
	
	@Test
	public final void shouldFindMailLogOnAssignmentId()
	{
		List<MailLogAssignment> mla = mailLogDao.findMailLogOnAssignmentIds(new Integer[]{2});
		
		assertEquals(1, mla.size());
		assertEquals(9, mla.get(0).getMailLogId().intValue());
	}
}
