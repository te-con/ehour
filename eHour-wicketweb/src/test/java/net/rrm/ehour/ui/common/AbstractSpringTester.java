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

import com.google.common.collect.Lists;
import net.rrm.ehour.appconfig.EhourSystemConfig;
import net.rrm.ehour.audit.service.AuditService;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.ui.common.util.AuthUtil;
import net.rrm.ehour.update.UpdateService;
import org.apache.wicket.spring.test.ApplicationContextMock;

import java.util.Calendar;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created on Mar 17, 2009, 5:31:37 AM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public abstract class AbstractSpringTester {
    private ApplicationContextMock mockContext;
    private EhourConfigStub config;

    private AuditService auditService;
    private EhourSystemConfig ehourSystemConfig = new EhourSystemConfig();

    private void createContextSetup() {
        mockContext = new ApplicationContextMock();

        mockContext.putBean("ehourSystemConfig", ehourSystemConfig);

        config = createConfig();
        mockContext.putBean("EhourConfig", config);

        auditService = mock(AuditService.class);
        mockContext.putBean("auditService", auditService);

        mockContext.putBean("authUtil", buildAuthUtil());

        UpdateService updateService = mock(UpdateService.class);
        mockContext.putBean(updateService);

        mockContext.putBean("menuDefinition", Lists.newArrayList());

        when(updateService.isLatestVersion()).thenReturn(Boolean.TRUE);
    }

    protected EhourConfigStub createConfig() {
        EhourConfigStub config = new EhourConfigStub();
        config.setFirstDayOfWeek(Calendar.SUNDAY);
        return config;
    }

    public final ApplicationContextMock getMockContext() {
        if (mockContext == null) {
            createContextSetup();
        }

        return mockContext;
    }

    protected AuthUtil buildAuthUtil() {
        return new AuthUtil();
    }

    public final void clearMockContext() {
        mockContext = null;
    }

    public final EhourConfigStub getConfig() {
        return config;
    }

    public final AuditService getAuditService() {
        return auditService;
    }

    public EhourSystemConfig getEhourSystemConfig() {
        return ehourSystemConfig;
    }
}
