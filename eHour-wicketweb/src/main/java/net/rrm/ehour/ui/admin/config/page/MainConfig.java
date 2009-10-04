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
import net.rrm.ehour.ui.admin.AbstractAdminPage;
import net.rrm.ehour.ui.admin.config.dto.MainConfigBackingBean;
import net.rrm.ehour.ui.admin.config.panel.ConfigTabPanel;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Main config page
 **/
public class MainConfig extends AbstractAdminPage
{
	private static final long serialVersionUID = 8613594529875207988L;
	
	@SpringBean
	private ConfigurationService	configService;
	
	public MainConfig()
	{
		super(new ResourceModel("admin.config.title"), null,
						"admin.config.help.header",
						"admin.config.help.body");
		
		setUpPage();
	}

	private void setUpPage()
	{
//		GreyRoundedBorder greyBorder = new GreyRoundedBorder("configFrame", new ResourceModel("admin.config.title"));
//		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueBorder");
//		greyBorder.add(blueBorder);
//		add(greyBorder);

//		WebMarkupContainer container = new WebMarkupContainer("configFrame");
//		add(container);
		
		MainConfigBackingBean configBackingBean = new MainConfigBackingBean(getDbConfig());
		add(new ConfigTabPanel("configTabs", new CompoundPropertyModel(configBackingBean)));
	}

	private EhourConfigStub getDbConfig()
	{
		return configService.getConfiguration();
	}
}
