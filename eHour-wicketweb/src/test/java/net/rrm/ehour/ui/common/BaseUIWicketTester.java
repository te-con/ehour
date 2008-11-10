/**
 * Created on Jul 17, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.common;

import static org.easymock.EasyMock.createMock;

import java.util.Calendar;
import java.util.Locale;

import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.config.EhourConfigStub;

import org.apache.wicket.Component;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.settings.Settings;
import org.apache.wicket.spring.injection.annot.test.AnnotApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;

/**
 * Base class for wicket unit tests 
 **/
@SuppressWarnings("unchecked")
public abstract class BaseUIWicketTester
{
	protected WicketTester	tester;
	protected AnnotApplicationContextMock	mockContext;
	protected EhourConfigStub	config;
	protected TestEhourWebApplication webapp;
	protected AuditService auditService;
	
	@Before
	public void setUp() throws Exception
	{
		mockContext = new AnnotApplicationContextMock();
		
		
		config = new EhourConfigStub();
		config.setFirstDayOfWeek(Calendar.SUNDAY);

		mockContext.putBean("EhourConfig", config);

		auditService = createMock(AuditService.class);
		mockContext.putBean("auditService", auditService);

		webapp =  new TestEhourWebApplication(mockContext);
		tester = new WicketTester(webapp);
		
		((Settings)webapp.getApplicationSettings()).addStringResourceLoader(new IStringResourceLoader()
		{

			public String loadStringResource(Component component, String key)
			{
				return key;
			}

			public String loadStringResource(Class clazz, String key, Locale locale, String style)
			{
				return key;
			}
			
		});
	}
}
