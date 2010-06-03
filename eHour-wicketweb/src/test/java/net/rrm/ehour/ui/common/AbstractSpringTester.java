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

package net.rrm.ehour.ui.common;

import static org.easymock.EasyMock.createMock;

import java.util.Calendar;

import net.rrm.ehour.service.audit.service.AuditService;
import net.rrm.ehour.service.config.EhourConfigStub;

import org.apache.wicket.spring.injection.annot.test.AnnotApplicationContextMock;

/**
 * Created on Mar 17, 2009, 5:31:37 AM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public abstract class AbstractSpringTester
{
	protected AnnotApplicationContextMock mockContext;
	private EhourConfigStub config;
	private AuditService auditService;

	private void createContextSetup() 
	{
		mockContext = new AnnotApplicationContextMock();
		config = new EhourConfigStub();
		config.setFirstDayOfWeek(Calendar.SUNDAY);

		mockContext.putBean("EhourConfig", config);

		auditService = createMock(AuditService.class);
		mockContext.putBean("auditService", auditService);
	}
	
	public final AnnotApplicationContextMock getMockContext()
	{
		if (mockContext == null)
		{
			createContextSetup();
		}
		
		return mockContext;
	}
	
	public final EhourConfigStub getConfig()
	{
		return config;
	}
	
	public final AuditService getAuditService()
	{
		return auditService;
	}
}
