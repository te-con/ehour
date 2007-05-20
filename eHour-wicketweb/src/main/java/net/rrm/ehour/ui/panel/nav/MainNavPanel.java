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

import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.page.admin.assignment.AssignmentPage;
import net.rrm.ehour.ui.page.user.OverviewPage;
import net.rrm.ehour.ui.page.user.timesheet.Page2;
import wicket.ResourceReference;
import wicket.markup.html.link.BookmarkablePageLink;
import wicket.markup.html.panel.Panel;
import wicket.markup.html.resources.CompressedResourceReference;
import wicket.markup.html.resources.StyleSheetReference;

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
		
		add(new BookmarkablePageLink("overviewLink", OverviewPage.class));
		add(new BookmarkablePageLink("page2Link", Page2.class));
		add(new BookmarkablePageLink("assignmentLink", AssignmentPage.class));
		
		add(new StyleSheetReference("headerStyle", headerStyle()));
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