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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import net.rrm.ehour.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetCommentId;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings({"deprecation"})
public class TimesheetCommentDAOTest extends AbstractAnnotationDaoTest
{ 
	@Autowired
	private TimesheetCommentDAO	timesheetCommentDAO;

	@Test
	public void testGetTimesheetEntriesInRange()
	{
		TimesheetComment	comment;
		
		comment = timesheetCommentDAO.findById(new TimesheetCommentId(1, new Date(2007 - 1900, 1 - 1, 7)));
		
		assertNotNull(comment);
	}
	
	@Test
	public void testDeleteOnUser()
	{
		int rowCount = timesheetCommentDAO.deleteCommentsForUser(1);
		assertEquals(2, rowCount);
	}
}
