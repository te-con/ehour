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

package net.rrm.ehour.ui.common.util;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.ui.report.panel.aggregate.ProjectReportPanelTest;

import org.junit.Test;

/**
 * Created on Mar 17, 2009, 7:04:03 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class CommonWebUtilTest extends ProjectReportPanelTest
{
	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.ui.common.util.CommonWebUtil#findComponent(org.apache.wicket.MarkupContainer, java.lang.Class)}.
	 */
	@Test
	public void testFindComponent()
	{
		setupExpectations();
		
		replay(getAggregateReportService());
		
		getTester().assertNoErrorMessage();
		
		verify(getAggregateReportService());
		
//		List<OpenFlashChart> components = CommonWebUtil.findComponent(panel, OpenFlashChart.class);
//		assertEquals(2, components.size());
	}
}
