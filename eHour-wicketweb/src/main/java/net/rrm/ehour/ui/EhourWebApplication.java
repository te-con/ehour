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

import net.rrm.ehour.appconfig.EhourHomeUtil;
import net.rrm.ehour.ui.admin.assignment.page.AssignmentAdmin;
import net.rrm.ehour.ui.admin.backup.BackupDbPage;
import net.rrm.ehour.ui.admin.config.page.MainConfigPage;
import net.rrm.ehour.ui.admin.customer.CustomerAdminPage;
import net.rrm.ehour.ui.admin.department.DepartmentAdminPage;
import net.rrm.ehour.ui.admin.project.ProjectAdmin;
import net.rrm.ehour.ui.admin.user.page.UserAdminPage;
import net.rrm.ehour.ui.audit.page.AuditReportPage;
import net.rrm.ehour.ui.common.converter.FloatConverter;
import net.rrm.ehour.ui.common.i18n.EhourHomeResourceLoader;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.login.page.Login;
import net.rrm.ehour.ui.login.page.Logout;
import net.rrm.ehour.ui.login.page.SessionExpiredPage;
import net.rrm.ehour.ui.pm.page.ProjectManagement;
import net.rrm.ehour.ui.report.page.GlobalReportPage;
import net.rrm.ehour.ui.report.summary.ProjectSummaryPage;
import net.rrm.ehour.ui.timesheet.export.TimesheetExportPage;
import net.rrm.ehour.ui.timesheet.page.MonthOverviewPage;
import net.rrm.ehour.ui.userprefs.page.UserPreferencePage;
import org.apache.log4j.Logger;
import org.apache.wicket.*;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.markup.head.ResourceAggregator;
import org.apache.wicket.markup.head.StringHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.cycle.PageRequestHandlerTracker;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Comparator;

/**
 * Base config for wicket eHour webapp
 */

public class EhourWebApplication extends AuthenticatedWebApplication {
    private static final Logger LOGGER = Logger.getLogger(EhourWebApplication.class);

    private AuthenticationManager authenticationManager;
    private String version;
    private boolean initialized;

    @Value("${ehour.configurationType}")
    private String configurationType;

    @Value("${EHOUR_HOME}")
    private String eHourHome;

    @Value("${ehour.translations}")
    private String translationsDir;

    public void init() {
        if (!initialized) {
            super.init();
            springInjection();

            setUACHeaderPriority();

            getMarkupSettings().setStripWicketTags(true);
            mountPages();
            getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
            setupSecurity();

            registerEhourHomeResourceLoader();

            getRequestCycleListeners().add(new PageRequestHandlerTracker());

            initialized = true;
        }

        if (isInTestMode()) {
            getDebugSettings().setOutputComponentPath(true);

        }

    }

    private boolean isInTestMode() {
        return Boolean.parseBoolean(System.getProperty("EHOUR_TEST", "false"));
    }


    private void setUACHeaderPriority() {
        final Comparator<? super ResourceAggregator.RecordedHeaderItem> defaultHeaderComparator = getResourceSettings().getHeaderItemComparator();

        getResourceSettings().setHeaderItemComparator(new Comparator<ResourceAggregator.RecordedHeaderItem>() {
            @Override
            public int compare(ResourceAggregator.RecordedHeaderItem o1, ResourceAggregator.RecordedHeaderItem o2) {
                if (o1.getItem() instanceof StringHeaderItem && isIEHeader(o1))
                    return -1;
                else if (o2.getItem() instanceof StringHeaderItem && isIEHeader(o2))
                    return 1;
                else
                    return defaultHeaderComparator.compare(o1, o2);
            }

            private boolean isIEHeader(ResourceAggregator.RecordedHeaderItem o1) {
                StringHeaderItem headerItem = (StringHeaderItem) o1.getItem();

                return headerItem.getString().toString().contains("X-UA-Compatible");
            }
        });
    }

    protected void registerEhourHomeResourceLoader() {
        String absoluteTranslationsPath = EhourHomeUtil.getTranslationsDir(eHourHome, translationsDir);
        EhourHomeResourceLoader resourceLoader = new EhourHomeResourceLoader(absoluteTranslationsPath);

        getResourceSettings().getResourceFinders().add(resourceLoader);
    }

    @Override
    public RuntimeConfigurationType getConfigurationType() {
        String development = RuntimeConfigurationType.DEVELOPMENT.name();
        String deployment = RuntimeConfigurationType.DEPLOYMENT.name();

        if (configurationType == null || (!configurationType.equalsIgnoreCase(deployment) &&
                !configurationType.equalsIgnoreCase(development))) {
            LOGGER.warn("Invalid configuration type defined in ehour.properties. Valid values are " + deployment + " or " + development);
            return RuntimeConfigurationType.DEVELOPMENT;
        }

        return RuntimeConfigurationType.valueOf(configurationType.toUpperCase());
    }

    private void mountPages() {
        mountPage("/login", Login.class);
        mountPage("/logout", Logout.class);

        mountPage("/admin", MainConfigPage.class);
        mountPage("/admin/employee", UserAdminPage.class);
        mountPage("/admin/department", DepartmentAdminPage.class);
        mountPage("/admin/customer", CustomerAdminPage.class);
        mountPage("/admin/project", ProjectAdmin.class);
        mountPage("/admin/assignment", AssignmentAdmin.class);

        mountPage("/consultant/overview", MonthOverviewPage.class);

        mountPage("/consultant/exportmonth", TimesheetExportPage.class);

        mountPage("/report", GlobalReportPage.class);
        mountPage("/report/summary/project", ProjectSummaryPage.class);

        mountPage("/audit", AuditReportPage.class);

        mountPage("/pm", ProjectManagement.class);

        mountPage("/prefs", UserPreferencePage.class);

        mountPage("/backup", BackupDbPage.class);
    }

    protected void springInjection() {
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    }

    protected void setupSecurity() {
        getApplicationSettings().setPageExpiredErrorPage(SessionExpiredPage.class);

        getSecuritySettings().setAuthorizationStrategy(new RoleAuthorizationStrategy(this));

        getSecuritySettings().setUnauthorizedComponentInstantiationListener(new IUnauthorizedComponentInstantiationListener() {
            public void onUnauthorizedInstantiation(final Component component) {
                if (component instanceof Page) {
                    throw new RestartResponseAtInterceptPageException(Login.class);
                } else {
                    throw new UnauthorizedInstantiationException(component.getClass());
                }
            }
        });
    }

    @Override
    protected IConverterLocator newConverterLocator() {
        ConverterLocator converterLocator = new ConverterLocator();
        converterLocator.set(Float.class, new FloatConverter());
        return converterLocator;
    }

    /**
     * Set the homepage
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return MonthOverviewPage.class;
    }

    /**
     * The login page for unauthenticated clients
     */
    protected Class<? extends WebPage> getSignInPageClass() {
        return Login.class;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    /*
      * (non-Javadoc)
      * @see org.apache.wicket.authentication.AuthenticatedWebApplication#getWebSessionClass()
      */
    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return EhourWebSession.class;
    }

    /**
     * @param authenticationManager the authenticationManager to set
     */
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


    public static EhourWebApplication get() {
        return (EhourWebApplication) WebApplication.get();
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
