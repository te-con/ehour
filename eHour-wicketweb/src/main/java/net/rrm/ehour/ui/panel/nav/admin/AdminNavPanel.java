/**
 * Created on Jul 17, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.nav.admin;

import net.rrm.ehour.ui.border.GreyNavBorder;
import net.rrm.ehour.ui.page.admin.customer.CustomerAdmin;
import net.rrm.ehour.ui.page.admin.department.DepartmentAdmin;
import net.rrm.ehour.ui.page.admin.mainconfig.MainConfig;
import net.rrm.ehour.ui.page.admin.project.ProjectAdmin;
import net.rrm.ehour.ui.page.admin.user.UserAdmin;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * TODO 
 **/

public class AdminNavPanel extends Panel
{
	private static final long serialVersionUID = -7877416542663086633L;

	public AdminNavPanel(String id)
	{
		super(id);

		GreyNavBorder greyNavBorder = new GreyNavBorder("navBorder", new ResourceModel("admin.nav.userAdmin"));

		add(greyNavBorder);
		
		greyNavBorder.add(new BookmarkablePageLink("mainConfig", MainConfig.class));
		greyNavBorder.add(new BookmarkablePageLink("userAdmin", UserAdmin.class));
		greyNavBorder.add(new BookmarkablePageLink("deptAdmin", DepartmentAdmin.class));
		greyNavBorder.add(new BookmarkablePageLink("customerAdmin", CustomerAdmin.class));
		greyNavBorder.add(new BookmarkablePageLink("projectAdmin", ProjectAdmin.class));
	}

}
