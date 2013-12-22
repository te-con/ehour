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

package net.rrm.ehour.ui.admin.config.page;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.admin.config.dto.MainConfigBackingBean;
import net.rrm.ehour.ui.admin.config.panel.ConfigTabPanel;
import net.rrm.ehour.ui.common.page.AbstractBasePage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Main config page
 */
@AuthorizeInstantiation(UserRole.ROLE_ADMIN)
public class MainConfigPage extends AbstractBasePage<Void> {
    private static final long serialVersionUID = 8613594529875207988L;

    @SpringBean
    private ConfigurationService configService;

    public MainConfigPage() {
        super(new ResourceModel("admin.config.title"));

        setUpPage();
    }

    private void setUpPage() {
        MainConfigBackingBean configBackingBean = new MainConfigBackingBean(getDbConfig());
        add(new SystemInfoPanel("sysinfo"));
        add(new ConfigTabPanel("configTabs", new CompoundPropertyModel<MainConfigBackingBean>(configBackingBean)));
    }

    private EhourConfigStub getDbConfig() {
        return configService.getConfiguration();
    }
}
