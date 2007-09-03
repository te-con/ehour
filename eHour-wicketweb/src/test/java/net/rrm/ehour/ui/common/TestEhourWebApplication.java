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

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.page.login.SessionExpiredPage;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.injection.annot.test.AnnotApplicationContextMock;

/**
 * TODO 
 **/

public class TestEhourWebApplication extends EhourWebApplication
{
	private AnnotApplicationContextMock	mockContext;


	/**
	 * When not authorized, just let it pass
	 */
	@Override
	protected void setupSecurity()
	{
		getApplicationSettings().setPageExpiredErrorPage(SessionExpiredPage.class);

		getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(this));

		getSecuritySettings().setUnauthorizedComponentInstantiationListener(new IUnauthorizedComponentInstantiationListener()
        {
            public void onUnauthorizedInstantiation(final Component component)
            {
            }
        });		
	}

	/**
	 * Override to provide our mock injector
	 */
	@Override
	protected void springInjection()
	{
		mockContext = new AnnotApplicationContextMock();
		mockContext.putBean("EhourConfig", new EhourConfigStub());		
		
		addComponentInstantiationListener(new SpringComponentInjector(this, mockContext));
	}

	public AnnotApplicationContextMock getMockContext()
	{
		return mockContext;
	}
}
