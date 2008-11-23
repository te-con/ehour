/**
 * 
 */
package net.rrm.ehour.ui.panel.timesheet.util;

import static org.junit.Assert.assertEquals;
import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.timesheet.dto.TimesheetRow;
import net.rrm.ehour.ui.timesheet.util.TimesheetRowComparator;

import org.junit.Test;

/**
 * @author thies
 *
 */
public class TimesheetRowComparatorTest
{

	/**
	 * Test method for {@link net.rrm.ehour.ui.timesheet.util.TimesheetRowComparator#compare(net.rrm.ehour.ui.timesheet.dto.TimesheetRow, net.rrm.ehour.ui.timesheet.dto.TimesheetRow)}.
	 */
	@Test
	public void testCompare()
	{
		ProjectAssignment pA = DummyDataGenerator.getProjectAssignment(1);
		pA.getProject().setName("A");
		TimesheetRow rowA = new TimesheetRow(new EhourConfigStub());
		rowA.setProjectAssignment(pA);
		
		ProjectAssignment pB = DummyDataGenerator.getProjectAssignment(1);
		pB.getProject().setName("b");
		TimesheetRow rowB = new TimesheetRow(new EhourConfigStub());
		rowB.setProjectAssignment(pB);
		
		assertEquals(-1, new TimesheetRowComparator().compare(rowA, rowB));
	}

}
