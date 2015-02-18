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
import net.rrm.ehour.appconfig.EhourSystemConfig;
import net.rrm.ehour.ui.admin.audit.AuditReportPage;
import net.rrm.ehour.ui.admin.backup.BackupDbPage;
import net.rrm.ehour.ui.admin.config.MainConfigPage;
import net.rrm.ehour.ui.common.converter.FloatConverter;
import net.rrm.ehour.ui.common.i18n.EhourHomeResourceLoader;
import net.rrm.ehour.ui.common.session.DevelopmentWebSession;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.login.page.Login;
import net.rrm.ehour.ui.login.page.Logout;
import net.rrm.ehour.ui.login.page.SessionExpiredPage;
import net.rrm.ehour.ui.manage.assignment.AssignmentManagePage;
import net.rrm.ehour.ui.manage.customer.CustomerManagePage;
import net.rrm.ehour.ui.manage.department.DepartmentManagePage;
import net.rrm.ehour.ui.manage.lock.LockManagePage;
import net.rrm.ehour.ui.manage.project.ProjectManagePage;
import net.rrm.ehour.ui.manage.user.ImpersonateUserPage;
import net.rrm.ehour.ui.manage.user.UserManagePage;
import net.rrm.ehour.ui.pm.ProjectManagerPage;
import net.rrm.ehour.ui.report.detailed.DetailedReportRESTResource;
import net.rrm.ehour.ui.report.detailed.DetailedReportRESTResource$;
import net.rrm.ehour.ui.report.page.ReportPage;
import net.rrm.ehour.ui.timesheet.export.TimesheetExportPage;
import net.rrm.ehour.ui.timesheet.page.MonthOverviewPage;
import net.rrm.ehour.ui.userprefs.page.UserPreferencePage;
import org.apache.log4j.Logger;
import org.apache.wicket.*;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.markup.head.ResourceAggregator;
import org.apache.wicket.markup.head.StringHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.PageRequestHandlerTracker;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.IResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.IStaticCacheableResource;
import org.apache.wicket.request.resource.caching.version.IResourceVersion;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.time.Duration;
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

    private EhourSystemConfig ehourSystemConfig;

    private String build;
    private IAuthorizationStrategy authorizationStrategy;

    @Override
    public void init() {
        if (!initialized) {
            super.init();
            springInjection();

            setUACHeaderPriority();

            getMarkupSettings().setStripWicketTags(true);
            mountPages();
            mountResources();

            getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
            setupSecurity();

            registerEhourHomeResourceLoader();

            getRequestCycleListeners().add(new PageRequestHandlerTracker());

            cacheStrategy();

            initialized = true;
        }

        if (isInTestMode()) {
            LOGGER.info("*** Running in test mode ***");
            getDebugSettings().setOutputComponentPath(true);
            getResourceSettings().setResourcePollFrequency(Duration.ONE_SECOND);
        }

        if (version != null) { // dont spoil the log during junit tests
            LOGGER.info(String.format("*** %s version %s started!", getAppName(), version));
        }
    }

    private void cacheStrategy() {
        IResourceCachingStrategy strategy = new FilenameWithVersionResourceCachingStrategy(new IResourceVersion() {
            @Override
            public String getVersion(IStaticCacheableResource resource) {
                return version;
            }
        });
        this.getResourceSettings().setCachingStrategy(strategy);
    }

    protected String getAppName() {
        return "eHour";
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
        String absoluteTranslationsPath = EhourHomeUtil.getTranslationsDir(ehourSystemConfig.getEhourHome(), ehourSystemConfig.getTranslationsDir());
        EhourHomeResourceLoader resourceLoader = new EhourHomeResourceLoader(absoluteTranslationsPath);

        getResourceSettings().getResourceFinders().add(resourceLoader);
    }

    @Override
    public RuntimeConfigurationType getConfigurationType() {
        String development = RuntimeConfigurationType.DEVELOPMENT.name();
        String deployment = RuntimeConfigurationType.DEPLOYMENT.name();

        String configurationType = ehourSystemConfig.getConfigurationType();

        if (configurationType == null || (!configurationType.equalsIgnoreCase(deployment) &&
                !configurationType.equalsIgnoreCase(development))) {
            LOGGER.warn("Invalid configuration type defined in ehour.properties. Valid values are " + deployment + " or " + development);
            return RuntimeConfigurationType.DEVELOPMENT;
        }

        return RuntimeConfigurationType.valueOf(configurationType.toUpperCase());
    }

    protected void mountPages() {
        mountPage("/login", Login.class);
        mountPage("/logout", Logout.class);

        mountPage("/admin", MainConfigPage.class);
        mountPage("/admin/employee", UserManagePage.class);
        mountPage("/admin/department", DepartmentManagePage.class);
        mountPage("/admin/client", CustomerManagePage.class);
        mountPage("/admin/project", ProjectManagePage.class);
        mountPage("/admin/assignment", AssignmentManagePage.class);

        mountPage("/consultant/overview", MonthOverviewPage.class);

        mountPage("/consultant/exportmonth", TimesheetExportPage.class);

        mountPage("/report", ReportPage.class);

        mountPage("/audit", AuditReportPage.class);

        mountPage("/pm", ProjectManagerPage.class);

        mountPage("/prefs", UserPreferencePage.class);

        mountPage("/backup", BackupDbPage.class);

        mountPage("/op/lock", LockManagePage.class);

        mountPage("/admin/impersonate", ImpersonateUserPage.class);
    }

    private void mountResources() {
        mountResource("/rest/report/detailed", new ResourceReference("restReference") {

            DetailedReportRESTResource resource = DetailedReportRESTResource$.MODULE$.apply();

            @Override
            public IResource getResource() {
                return resource;
            }
        });
    }

    protected void springInjection() {
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
    }

    protected void setupSecurity() {
        getApplicationSettings().setPageExpiredErrorPage(SessionExpiredPage.class);

        authorizationStrategy = getAuthorizationStrategy();
        getSecuritySettings().setAuthorizationStrategy(authorizationStrategy);

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

    public IAuthorizationStrategy getAuthorizationStrategy() {
        return new RoleAuthorizationStrategy(this);
    }

    @Override
    public Session newSession(Request request, Response response) {
        if (ehourSystemConfig.isDisableAuth()) {
            System.err.println("*** eHour is configured to disable any authentication");
            System.err.println("*** Enable by setting ehour.disableAuth=false in your ehour.properties");
            System.err.println("*** DO NOT RUN IN PRODUCTION WITH THIS CONFIGURATION");
            return new DevelopmentWebSession(request);
        } else {
            return super.newSession(request, response);
        }
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

    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return EhourWebSession.class;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setEhourSystemConfig(EhourSystemConfig ehourSystemConfig) {
        this.ehourSystemConfig = ehourSystemConfig;
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

    public String geteHourHome() {
        return ehourSystemConfig.getEhourHome();
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getBuild() {
        return build;
    }

    public Boolean isBookWholeWeekEnabled() {
        return ehourSystemConfig.isBookWholeWeekEnabled();
    }
}
