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

package net.rrm.ehour.ui.admin.config;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.ui.admin.AbstractAdminPage;
import net.rrm.ehour.ui.admin.config.panel.ConfigTabPanel;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Main config page
 */
public class MainConfigPage extends AbstractAdminPage<Void> {
    private static final CssResourceReference CSS = new CssResourceReference(MainConfigPage.class, "config.css");

    @SpringBean
    private ConfigurationService configService;

    public MainConfigPage() {
        super(new ResourceModel("admin.config.title"));

        setUpPage();
    }

    private void setUpPage() {
        MainConfigBackingBean configBackingBean = new MainConfigBackingBean(getDbConfig());
        add(new SystemInfoPanel("sysinfo"));
        add(new ConfigTabPanel("configTabs", new CompoundPropertyModel<>(configBackingBean)));
    }

    private EhourConfigStub getDbConfig() {
        return configService.getConfiguration();
    }

    @Override
    protected void onRenderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(CSS));
    }
}
