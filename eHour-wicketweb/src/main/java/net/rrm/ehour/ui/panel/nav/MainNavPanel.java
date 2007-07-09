/**
 * Created on May 8, 2007
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

package net.rrm.ehour.ui.panel.nav;

import net.rrm.ehour.ui.page.admin.assignment.AssignmentPage;
import net.rrm.ehour.ui.page.login.Login;
import net.rrm.ehour.ui.page.user.Overview;
import net.rrm.ehour.ui.page.user.report.UserReport;
import net.rrm.ehour.ui.util.AuthUtil;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;

/**
 * Main navigation panel 
 **/

public class MainNavPanel extends Panel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 854412484275829659L;

	public MainNavPanel(String id)
	{
		super(id);
		
		addLink(this, "overviewLink", Overview.class);
		addLink(this, "userReportLink", UserReport.class);
		addLink(this, "assignmentLink", AssignmentPage.class);
		addLink(this, "logoffLink", Login.class);
		add(new Label("loggedInUser", AuthUtil.getUser().getFullName()));
		
		add(new StyleSheetReference("headerStyle", headerStyle()));
	}

	/**
	 * Add link to hierarchy, visibility off if user is not authorized to view the page
	 * @param parent
	 * @param id
	 * @param linkPage
	 */
	private void addLink(WebMarkupContainer parent, String id, Class<? extends WebPage> linkPage)
	{
		BookmarkablePageLink	link;
		
		link = new BookmarkablePageLink(id, linkPage);
		link.setVisible(AuthUtil.userAuthorizedForPage(linkPage));
	
		parent.add(link);
	}
	
	/**
	 * Create a style
	 * 
	 * @return a style
	 */
	public final ResourceReference headerStyle()
	{
		return new CompressedResourceReference(MainNavPanel.class, "style/header.css");
	}	
}