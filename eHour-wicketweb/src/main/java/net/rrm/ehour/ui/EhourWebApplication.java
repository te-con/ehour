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

package net.rrm.ehour.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import net.rrm.ehour.ui.admin.assignment.page.AssignmentAdmin;
import net.rrm.ehour.ui.admin.config.page.MainConfig;
import net.rrm.ehour.ui.admin.customer.page.CustomerAdmin;
import net.rrm.ehour.ui.admin.department.page.DepartmentAdmin;
import net.rrm.ehour.ui.admin.project.page.ProjectAdmin;
import net.rrm.ehour.ui.admin.user.page.UserAdmin;
import net.rrm.ehour.ui.audit.page.AuditReportPage;
import net.rrm.ehour.ui.audit.panel.AuditReportExcel;
import net.rrm.ehour.ui.common.component.AbstractExcelResource;
import net.rrm.ehour.ui.common.config.PageConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.login.page.Login;
import net.rrm.ehour.ui.login.page.SessionExpiredPage;
import net.rrm.ehour.ui.pm.page.ProjectManagement;
import net.rrm.ehour.ui.report.page.GlobalReportPage;
import net.rrm.ehour.ui.report.panel.aggregate.CustomerReportExcel;
import net.rrm.ehour.ui.report.panel.aggregate.EmployeeReportExcel;
import net.rrm.ehour.ui.report.panel.aggregate.ProjectReportExcel;
import net.rrm.ehour.ui.report.panel.detail.DetailedReportExcel;
import net.rrm.ehour.ui.report.panel.user.UserReportExcel;
import net.rrm.ehour.ui.report.user.page.UserReport;
import net.rrm.ehour.ui.timesheet.export.ExportMonthSelectionPage;
import net.rrm.ehour.ui.timesheet.export.excel.ExportReportExcel;
import net.rrm.ehour.ui.timesheet.export.print.PrintMonth;
import net.rrm.ehour.ui.timesheet.page.Overview;
import net.rrm.ehour.ui.userprefs.page.UserPreferencePage;

import org.acegisecurity.AuthenticationManager;
import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authorization.strategies.role.RoleAuthorizationStrategy;
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
	private static final Logger LOGGER = Logger.getLogger(EhourWebApplication.class);
	
	private AuthenticationManager authenticationManager;
	protected Class<? extends WebPage>	login = Login.class;
	private String version;
	private PageConfig pageConfig;
	private boolean initialized;
	
	public EhourWebApplication()
	{
		
	}

	public EhourWebApplication(Class<? extends WebPage> loginClass)
	{
		this.login = loginClass;
	}

	public void init()
	{
		if (!initialized)
		{
			super.init();
			springInjection();
	
			getMarkupSettings().setStripWicketTags(true);
			mountPages();
			getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
			setupSecurity();
			registerSharedResources();
			
			initialized = true;
		}
	}

	private void registerSharedResources()
	{
		mountExcelReport(new UserReportExcel(), UserReportExcel.getId());
		mountExcelReport(new CustomerReportExcel(), CustomerReportExcel.getId());
		mountExcelReport(new EmployeeReportExcel(), EmployeeReportExcel.getId());
		mountExcelReport(new ProjectReportExcel(), ProjectReportExcel.getId());
		mountExcelReport(new DetailedReportExcel(), DetailedReportExcel.getId());
		mountExcelReport(new AuditReportExcel(), AuditReportExcel.getId());
		mountExcelReport(new ExportReportExcel(), ExportReportExcel.getId());
	}
	
	private void mountExcelReport(AbstractExcelResource excelReport, String id)
	{
		getSharedResources().add(id, excelReport);
		mountSharedResource("/" + id, new ResourceReference(id).getSharedResourceKey());
	}
	
	@Override
	public String getConfigurationType()
	{
		String configurationType;
		
		try
		{
			Properties props = getEhourProperties();
			
			configurationType = props.getProperty("configurationType");
			
			if (!configurationType.equalsIgnoreCase(Application.DEPLOYMENT) &&
					!configurationType.equalsIgnoreCase(Application.DEVELOPMENT))
			{
				LOGGER.warn("Invalid configuration type defined in ehour.properties. Valid values are " + Application.DEPLOYMENT + " or " + Application.DEVELOPMENT);
				configurationType = Application.DEVELOPMENT;
			}
			
		} catch (IOException e)
		{
			configurationType = Application.DEVELOPMENT;
		}
		
		return configurationType;
	}
	
	private Properties getEhourProperties() throws IOException
	{
		ClassLoader classLoader = EhourWebApplication.class.getClassLoader();
		
		URL resource = classLoader.getResource("ehour.properties");
		
		if (resource != null)
		{
			InputStream inputStream = resource.openStream();
			Properties props = new Properties();
			props.load(inputStream);
			
			return props;
		}
		else
		{
			throw new FileNotFoundException();
		}
	}
	
	private void mountPages()
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
		
		mount(new HybridUrlCodingStrategy("/consultant/printform", ExportMonthSelectionPage.class));
		mount(new HybridUrlCodingStrategy("/consultant/print", PrintMonth.class));
		
		mount(new HybridUrlCodingStrategy("/report", GlobalReportPage.class));
		
		mount(new HybridUrlCodingStrategy("/audit", AuditReportPage.class));
		
		mount(new HybridUrlCodingStrategy("/pm", ProjectManagement.class));
		
		mount(new HybridUrlCodingStrategy("/prefs", UserPreferencePage.class));
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

		getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(this));

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
