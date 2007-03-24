/**
 * Created on 26-feb-2007
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

package net.rrm.ehour.web.timesheet.util;

import java.util.Comparator;

import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.util.ProjectAssignmentUtil;
import net.rrm.ehour.web.timesheet.dto.TimesheetRow;
import net.rrm.ehour.web.timesheet.util.TimesheetRowComparator;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO 
 **/

public class TimesheetRowComparatorTest
{
	private	TimesheetRow	row1, row2;
	private ProjectAssignment pag1, pag2;
	private Comparator<TimesheetRow> comp;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		row1 = new TimesheetRow();
		row2 = new TimesheetRow();
		
		pag1 = DummyDataGenerator.getProjectAssignment(1, 2, 3);
		pag2 = DummyDataGenerator.getProjectAssignment(2, 3, 4);
		
		row1.setProjectAssignment(pag1);
		row2.setProjectAssignment(pag2);
		
		comp = new TimesheetRowComparator();
	}

	/**
	 * Test method for {@link net.rrm.ehour.web.timesheet.util.TimesheetRowComparator#compare(net.rrm.ehour.web.timesheet.dto.TimesheetRow, net.rrm.ehour.web.timesheet.dto.TimesheetRow)}.
	 */
	@Test
	public void testCompareDefaultFirst()
	{
		pag1.setAssignmentType(ProjectAssignmentUtil.TYPE_DEFAULT_ASSIGNMENT);
		pag2.setAssignmentType(ProjectAssignmentUtil.TYPE_START_END_DATE);
		
		assertEquals(-1, comp.compare(row1, row2));
	}

	/**
	 * Test method for {@link net.rrm.ehour.web.timesheet.util.TimesheetRowComparator#compare(net.rrm.ehour.web.timesheet.dto.TimesheetRow, net.rrm.ehour.web.timesheet.dto.TimesheetRow)}.
	 */
	@Test
	public void testCompareDefaultBothNaming()
	{
		pag1.setAssignmentType(ProjectAssignmentUtil.TYPE_DEFAULT_ASSIGNMENT);
		pag2.setAssignmentType(ProjectAssignmentUtil.TYPE_DEFAULT_ASSIGNMENT);
		
		pag1.getProject().setName("bb");
		pag2.getProject().setName("aa");

		assertEquals(1, comp.compare(row1, row2));
	}

	/**
	 * Test method for {@link net.rrm.ehour.web.timesheet.util.TimesheetRowComparator#compare(net.rrm.ehour.web.timesheet.dto.TimesheetRow, net.rrm.ehour.web.timesheet.dto.TimesheetRow)}.
	 */
	@Test
	public void testCompareDefaultNoneNaming()
	{
		pag1.setAssignmentType(ProjectAssignmentUtil.TYPE_START_END_DATE);
		pag2.setAssignmentType(ProjectAssignmentUtil.TYPE_START_END_DATE);
		
		pag1.getProject().setName("BABAB");
		pag2.getProject().setName("BABAA");
		
		assertEquals(1, comp.compare(row1, row2));
	}

}
