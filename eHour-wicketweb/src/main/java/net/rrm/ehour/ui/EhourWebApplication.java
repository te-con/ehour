/**
 * Created on May 8, 2007
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

package net.rrm.ehour.ui;

import net.rrm.ehour.ui.authorization.AuthorizationStrategyFactory;
import net.rrm.ehour.ui.config.PageConfig;
import net.rrm.ehour.ui.page.admin.assignment.AssignmentAdmin;
import net.rrm.ehour.ui.page.admin.customer.CustomerAdmin;
import net.rrm.ehour.ui.page.admin.department.DepartmentAdmin;
import net.rrm.ehour.ui.page.admin.mainconfig.MainConfig;
import net.rrm.ehour.ui.page.admin.project.ProjectAdmin;
import net.rrm.ehour.ui.page.admin.user.UserAdmin;
import net.rrm.ehour.ui.page.login.Login;
import net.rrm.ehour.ui.page.login.SessionExpiredPage;
import net.rrm.ehour.ui.page.pm.ProjectManagement;
import net.rrm.ehour.ui.page.report.global.GlobalReportPage;
import net.rrm.ehour.ui.page.user.Overview;
import net.rrm.ehour.ui.page.user.print.PrintMonth;
import net.rrm.ehour.ui.page.user.print.PrintMonthSelection;
import net.rrm.ehour.ui.page.user.report.UserReport;
import net.rrm.ehour.ui.panel.report.aggregate.CustomerReportExcel;
import net.rrm.ehour.ui.panel.report.aggregate.EmployeeReportExcel;
import net.rrm.ehour.ui.panel.report.aggregate.ProjectReportExcel;
import net.rrm.ehour.ui.panel.report.detail.DetailedReportExcel;
import net.rrm.ehour.ui.panel.report.user.UserReportExcel;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.acegisecurity.AuthenticationManager;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.request.urlcompressing.UrlCompressingWebRequestProcessor;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.request.target.coding.HybridUrlCodingStrategy;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.PackageName;

/**
 * Base config for wicket eHour webapp
 **/

public class EhourWebApplication extends AuthenticatedWebApplication
{
	private AuthenticationManager authenticationManager;
	protected Class<? extends WebPage>	login = Login.class;
	private String version;
	private PageConfig pageConfig;
	private AuthorizationStrategyFactory authorizationStrategyFactory;
	
	public EhourWebApplication()
	{
		
	}

	public EhourWebApplication(Class<? extends WebPage> loginClass)
	{
		this.login = loginClass;
	}

	public void init()
	{
		super.init();
		springInjection();

		getMarkupSettings().setStripWicketTags(true);
		mountPages();
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
		setupSecurity();
		registerSharedResources();
	}


	/**
	 * 
	 */
	protected void registerSharedResources()
	{
		getSharedResources().add("userReportExcel", new UserReportExcel());
		mountSharedResource("/userReportExcel", new ResourceReference("userReportExcel").getSharedResourceKey());
		
		getSharedResources().add("customerReportExcel", new CustomerReportExcel());
		mountSharedResource("/customerReportExcel", new ResourceReference("customerReportExcel").getSharedResourceKey());
		
		getSharedResources().add("employeeReportExcel", new EmployeeReportExcel());
		mountSharedResource("/employeeReportExcel", new ResourceReference("employeeReportExcel").getSharedResourceKey());
		
		getSharedResources().add("projectReportExcel", new ProjectReportExcel());
		mountSharedResource("/projectReportExcel", new ResourceReference("projectReportExcel").getSharedResourceKey());
		
		getSharedResources().add("detailedReportExcel", new DetailedReportExcel());
		mountSharedResource("/detailedReportExcel", new ResourceReference("detailedReportExcel").getSharedResourceKey());
	}
	/**
	 * Mount pages
	 */
	protected void mountPages()
	{
		mount("/login", PackageName.forClass(login));

		mount(new HybridUrlCodingStrategy("/admin", MainConfig.class));
		mount(new HybridUrlCodingStrategy("/admin/employee", UserAdmin.class));
		mount(new HybridUrlCodingStrategy("/admin/department", DepartmentAdmin.class));
		mount(new HybridUrlCodingStrategy("/admin/customer", CustomerAdmin.class));
		mount(new HybridUrlCodingStrategy("/admin/project", ProjectAdmin.class));
		mount(new HybridUrlCodingStrategy("/admin/assignment", AssignmentAdmin.class));
		
		mount(new HybridUrlCodingStrategy("/consultant/overview", Overview.class));
		mount(new HybridUrlCodingStrategy("/consultant/report", UserReport.class));
		
		mount(new HybridUrlCodingStrategy("/consultant/printform", PrintMonthSelection.class));
		mount(new HybridUrlCodingStrategy("/consultant/print", PrintMonth.class));
		
		mount(new HybridUrlCodingStrategy("/report", GlobalReportPage.class));
		
		mount(new HybridUrlCodingStrategy("/pm", ProjectManagement.class));
	}
	
	/**
	 * 
	 */
	protected void springInjection()
	{
		addComponentInstantiationListener(new SpringComponentInjector(this));
	}

	/**
	 * 
	 */
	protected void setupSecurity()
	{
		getApplicationSettings().setPageExpiredErrorPage(SessionExpiredPage.class);
        getSecuritySettings().setAuthorizationStrategy(authorizationStrategyFactory.getAuthorizationStrategy(this));
		getSecuritySettings().setUnauthorizedComponentInstantiationListener(new IUnauthorizedComponentInstantiationListener()
		{
			public void onUnauthorizedInstantiation(final Component component)
			{
				if (component instanceof Page)
				{
					throw new RestartResponseAtInterceptPageException(login);
				} else
				{
					throw new UnauthorizedInstantiationException(component.getClass());
				}
			}
		});
	}

	/**
	 * 
	 * @param authorizationStrategyFactory
	 */
	public void setAuthorizationStrategyFactory(AuthorizationStrategyFactory authorizationStrategyFactory)
	{
		this.authorizationStrategyFactory = authorizationStrategyFactory;
	}

	/**
	 * Set the homepage
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return Overview.class;
	}

	/**
	 * The login page for unauthenticated clients
	 */
	protected Class<? extends WebPage> getSignInPageClass()
	{
		return login;
	}

	/*
	 * 
	 */
	public AuthenticationManager getAuthenticationManager()
	{
		return authenticationManager;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.authentication.AuthenticatedWebApplication#getWebSessionClass()
	 */
	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass()
	{
		return EhourWebSession.class;
	}

	/**
	 * @param authenticationManager the authenticationManager to set
	 */
	public void setAuthenticationManager(AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.protocol.http.WebApplication#newRequestCycleProcessor()
	 */
	@Override
	protected IRequestCycleProcessor newRequestCycleProcessor() 
	{ 
	    return new UrlCompressingWebRequestProcessor();
	}

	/**
	 * @return the version
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}

	/**
	 * @return the pageConfig
	 */
	public PageConfig getPageConfig()
	{
		return pageConfig;
	}

	/**
	 * @param pageConfig the pageConfig to set
	 */
	public void setPageConfig(PageConfig pageConfig)
	{
		this.pageConfig = pageConfig;
	}	
}
