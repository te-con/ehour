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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.ui.audit.AuditConstants;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.apache.wicket.Component;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuditReportPageTest extends AbstractSpringWebAppTester
{
	@Before
	public void before() throws Exception
	{
		expect(getAuditService().getAuditCount(isA(AuditReportRequest.class)))
			.andReturn(5)
			.anyTimes();

		expect(getAuditService().getAudit(isA(AuditReportRequest.class)))
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
		getTester().startPage(AuditReportPage.class);
		getTester().assertRenderedPage(AuditReportPage.class);
	}
	
	@Test
	public void shouldSubmit()
	{
		replay(getAuditService());
		startPage();

		String formPath = AuditConstants.PATH_FRAME + ":" + 
						AuditConstants.PATH_CRITERIA + ":" + 
						AuditConstants.PATH_FORM_BORDER + ":" +
						AuditConstants.ID_FORM;
		
		Component componentFromLastRenderedPage = tester.getComponentFromLastRenderedPage("frame:reportCriteria:border:criteriaForm:submitButton");
		System.out.println(componentFromLastRenderedPage);
		
		getTester().executeAjaxEvent(formPath + ":" +  
								AuditConstants.PATH_FORM_SUBMIT,
								"onclick");
		
//		FormTester formTester = getTester().newFormTester(formPath);
//		formTester.submit();
		
		getTester().assertRenderedPage(AuditReportPage.class);
		
		getTester().assertNoErrorMessage();
	}
	
	
}
