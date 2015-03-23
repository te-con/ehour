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

import net.rrm.ehour.ui.test.StrictWicketTester;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.junit.After;
import org.junit.Before;

import java.util.Locale;

/**
 * Base class for wicket unit tests
 */
public class BaseSpringWebAppTester extends AbstractSpringTester {
    public StrictWicketTester tester;
    public TestEhourWebApplication webApp;

    @SuppressWarnings("serial")
    @Before
    public final void setUp() throws Exception {
        setUp(null);
    }

    public final void setUp(Roles roles) throws Exception {
        webApp = new TestEhourWebApplication() {
            @Override
            protected void springInjection() {
                getComponentInstantiationListeners().add((new SpringComponentInjector(this, getMockContext(), true)));
            }
        };

        webApp.setAuthorizedRoles(roles);
        webApp.setEhourSystemConfig(getEhourSystemConfig());

        afterSetup();
    }

    protected void afterSetup() {
        startTester();
    }

    protected final void startTester() {
        tester = new StrictWicketTester(webApp);
        bypassStringResourceLoading();
    }

    @After
    public final void resetSpringContext() {
        clearMockContext();
    }

    protected final void bypassStringResourceLoading() {
        webApp.getResourceSettings().getStringResourceLoaders().add(new IStringResourceLoader() {
            @Override
            public String loadStringResource(Class<?> clazz, String key, Locale locale, String style, String variation) {
                return key;
            }

            @Override
            public String loadStringResource(Component component, String key, Locale locale, String style, String variation) {
                return key;
            }
        });
    }

    public StrictWicketTester getTester() {
        return tester;
    }

    protected TestEhourWebApplication getWebApp() {
        return webApp;
    }

    public static void startEmptyPanel() {
        try {
            BaseSpringWebAppTester webAppTester = new BaseSpringWebAppTester();
            webAppTester.setUp();
            webAppTester.tester.startComponentInPage(EmptyPanel.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
