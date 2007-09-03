/**
 * Created on Jul 17, 2007
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

package net.rrm.ehour.ui.panel.nav.admin;

import net.rrm.ehour.ui.border.GreyNavBorder;
import net.rrm.ehour.ui.page.admin.assignment.AssignmentAdmin;
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
		greyNavBorder.add(new BookmarkablePageLink("assignmentAdmin", AssignmentAdmin.class));
	}

}
