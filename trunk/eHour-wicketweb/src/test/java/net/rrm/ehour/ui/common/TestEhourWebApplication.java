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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.common.authorization.AuthUser;
import net.rrm.ehour.ui.common.config.PageConfig;
import net.rrm.ehour.ui.common.config.PageConfigImpl;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.login.page.SessionExpiredPage;

import org.apache.wicket.Component;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.spring.injection.annot.test.AnnotApplicationContextMock;
import org.apache.wicket.spring.test.ApplicationContextMock;

/**
 * 
 **/

public class TestEhourWebApplication extends EhourWebApplication implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7336200909844170964L;
	private transient ApplicationContextMock	mockContext;
	private EhourWebSession				session;

	/**
	 * 
	 * @param context
	 */
	public TestEhourWebApplication(ApplicationContextMock context)
	{
		this.mockContext = context;
	}
	
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
		addComponentInstantiationListener(new SpringComponentInjector(this, mockContext));
	}

	/**
	 * 
	 * @return
	 */
	public ApplicationContextMock getMockContext()
	{
		return mockContext;
	}

	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.EhourWebApplication#newRequestCycleProcessor()
	 */
	@Override
	protected IRequestCycleProcessor newRequestCycleProcessor() 
	{ 
	    return new WebRequestCycleProcessor();
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.authentication.AuthenticatedWebApplication#newSession(org.apache.wicket.Request, org.apache.wicket.Response)
	 */
	@Override
	public Session newSession(final Request request, final Response response)
	{
		session = new EhourWebSession(request)
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = -430393231818258496L;

			public AuthUser getUser()
			{
				User user = new User(1);
				user.setUsername("thies");
				user.setPassword("secret");
				
				Set<UserRole> userRoles = new HashSet<UserRole>();
				userRoles.add(new UserRole(CommonWebUtil.ROLE_CONSULTANT));
				userRoles.add(new UserRole(CommonWebUtil.ROLE_ADMIN));
				userRoles.add(new UserRole(CommonWebUtil.ROLE_REPORT));
				userRoles.add(new UserRole(CommonWebUtil.ROLE_PM));
				user.setUserRoles(userRoles);
				
				AuthUser authUser = new AuthUser(user);
				return authUser;
			}
			
			public Roles getRoles()
			{
				Roles roles = new Roles();
				roles.add(CommonWebUtil.ROLE_PM);
				roles.add(CommonWebUtil.ROLE_CONSULTANT);
				roles.add(CommonWebUtil.ROLE_ADMIN);
				roles.add(CommonWebUtil.ROLE_REPORT);
				
				return roles;
			}
			
			@Override
			public boolean authenticate(String username, String password)
			{
				return true;
			}
		};
		
		return session;
	}

	public EhourWebSession getSession()
	{
		return session;
	}
	
	@Override
	public PageConfig getPageConfig()
	{
		return new PageConfigImpl();
	}
	
	protected ISessionStore newSessionStore()
	{
		return new HttpSessionStore(this);
	}
	
	protected WebResponse newWebResponse(final HttpServletResponse servletResponse)
	{
		return new WebResponse(servletResponse);
	}
}
