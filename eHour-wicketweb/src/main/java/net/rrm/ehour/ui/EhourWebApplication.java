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

import net.rrm.ehour.ui.page.admin.assignment.AssignmentAdmin;
import net.rrm.ehour.ui.page.admin.customer.CustomerAdmin;
import net.rrm.ehour.ui.page.admin.department.DepartmentAdmin;
import net.rrm.ehour.ui.page.admin.mainconfig.MainConfig;
import net.rrm.ehour.ui.page.admin.project.ProjectAdmin;
import net.rrm.ehour.ui.page.admin.user.UserAdmin;
import net.rrm.ehour.ui.page.login.Login;
import net.rrm.ehour.ui.page.login.SessionExpiredPage;
import net.rrm.ehour.ui.page.report.ReportPage;
import net.rrm.ehour.ui.page.user.Overview;
import net.rrm.ehour.ui.page.user.print.PrintMonthSelection;
import net.rrm.ehour.ui.page.user.report.UserReport;
import net.rrm.ehour.ui.panel.report.type.CustomerReportExcel;
import net.rrm.ehour.ui.panel.report.type.EmployeeReportExcel;
import net.rrm.ehour.ui.panel.report.type.ProjectReportExcel;
import net.rrm.ehour.ui.panel.report.user.UserReportExcel;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.acegisecurity.AuthenticationManager;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.request.urlcompressing.UrlCompressingWebRequestProcessor;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.PackageName;

/**
 * Base config for wicket eHour webapp
 **/

public class EhourWebApplication extends AuthenticatedWebApplication
{
	private AuthenticationManager authenticationManager;

	public void init()
	{
		super.init();

		getMarkupSettings().setStripWicketTags(true);

		mount("/login", PackageName.forClass(Login.class));
		mount("/admin", PackageName.forClass(MainConfig.class));
		mount("/admin/employee", PackageName.forClass(UserAdmin.class));
		mount("/admin/department", PackageName.forClass(DepartmentAdmin.class));
		mount("/admin/customer", PackageName.forClass(CustomerAdmin.class));
		mount("/admin/project", PackageName.forClass(ProjectAdmin.class));
		mount("/admin/assignment", PackageName.forClass(AssignmentAdmin.class));
		mount("/consultant", PackageName.forPackage(Overview.class.getPackage()));
		mount("/consultant/report", PackageName.forPackage(UserReport.class.getPackage()));
		mount("/consultant/print", PackageName.forPackage(PrintMonthSelection.class.getPackage()));
		mount("/report", PackageName.forPackage(ReportPage.class.getPackage()));
		getRequestCycleSettings().setResponseRequestEncoding("UTF-8");

		springInjection();

		setupSecurity();
		
		// register excel report resources
		getSharedResources().add("userReportExcel", new UserReportExcel());
		getSharedResources().add("customerReportExcel", new CustomerReportExcel());
		getSharedResources().add("employeeReportExcel", new EmployeeReportExcel());
		getSharedResources().add("projectReportExcel", new ProjectReportExcel());
	}

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

		getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(this));

		getSecuritySettings().setUnauthorizedComponentInstantiationListener(new IUnauthorizedComponentInstantiationListener()
		{
			public void onUnauthorizedInstantiation(final Component component)
			{
				if (component instanceof Page)
				{
					throw new RestartResponseAtInterceptPageException(Login.class);
				} else
				{
					throw new UnauthorizedInstantiationException(component.getClass());
				}
			}
		});
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
		return Login.class;
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
	
	@Override
	protected IRequestCycleProcessor newRequestCycleProcessor() 
	{ 
	    return new UrlCompressingWebRequestProcessor();
	}	
}
