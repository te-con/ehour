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

package net.rrm.ehour.ui.audit.page;

import net.rrm.ehour.data.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.easymock.EasyMock.*;

public class AuditReportPageTest extends AbstractSpringWebAppTester
{
	@Before
	public void before() throws Exception
	{
		expect(getAuditService().getAuditCount(isA(AuditReportRequest.class)))
			.andReturn(5)
			.anyTimes();

		expect(getAuditService().findAudits(isA(AuditReportRequest.class), isA(Integer.class), isA(Integer.class)))
			.andReturn(new ArrayList<Audit>())
			.anyTimes();
	}
	
	@After
	public void tearDown()
	{
		verify(getAuditService());
	}
	
	private void startPage()
	{
		tester.startPage(AuditReportPage.class);
		tester.assertRenderedPage(AuditReportPage.class);
	}
	
	@Test
	public void shouldSubmit()
	{
		replay(getAuditService());
		startPage();

        tester.debugComponentTrees();

        tester.executeAjaxEvent("frame:frame_body:reportCriteria:border:border_body:criteriaForm:submitButton", "onclick");
		
		tester.assertRenderedPage(AuditReportPage.class);
		
		tester.assertNoErrorMessage();
	}
	
	
}
