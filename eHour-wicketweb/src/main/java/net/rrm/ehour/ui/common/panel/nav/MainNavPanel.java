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

package net.rrm.ehour.ui.common.panel.nav;

import net.rrm.ehour.ui.admin.config.page.MainConfig;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.util.AuthUtil;
import net.rrm.ehour.ui.login.page.Login;
import net.rrm.ehour.ui.pm.page.ProjectManagement;
import net.rrm.ehour.ui.report.page.GlobalReportPage;
import net.rrm.ehour.ui.report.user.page.UserReport;
import net.rrm.ehour.ui.timesheet.export.ExportMonthSelectionPage;
import net.rrm.ehour.ui.timesheet.page.MonthOverviewPage;
import net.rrm.ehour.ui.userprefs.page.UserPreferencePage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;

/**
 * Main navigation panel 
 **/

public class MainNavPanel extends AbstractBasePanel<Void>
{
	private static final long serialVersionUID = 854412484275829659L;

	/**
	 * 
	 * @param id
	 */
	public MainNavPanel(String id)
	{
		super(id);
		
		addLinks();
		addLoggedInUser();
	}

	private void addLinks()
	{
		boolean wasAdded = false;
		
		wasAdded = addLink(this, "admin", MainConfig.class, false);
		wasAdded |= addReportLink(this, "userReport", wasAdded);
		wasAdded |= addLink(this, "pm", ProjectManagement.class, wasAdded);
		wasAdded |= addLink(this, "print", ExportMonthSelectionPage.class, wasAdded);
		wasAdded |= addLink(this, "overview", MonthOverviewPage.class, wasAdded);
		
		add(new BookmarkablePageLink<Login>("logoffLink", Login.class));
	}
	
	/**
	 * 
	 */
	private void addLoggedInUser()
	{
		BookmarkablePageLink<UserPreferencePage> link = new BookmarkablePageLink<UserPreferencePage>("prefsLink", UserPreferencePage.class);
		add(link);
		
		Label loggedInUserLabel = new Label("loggedInUser", new Model<String>(AuthUtil.getUser().getFullName()) );
		link.add(loggedInUserLabel);
	}
	
	/**
	 * Add report link, global reporting when user has report role, user report when user is consultant
	 * @param parent
	 * @param id
	 */
	private boolean addReportLink(WebMarkupContainer parent, String id, boolean inclSeperator)
	{
		Class<? extends WebPage> 	linkPage;
		
		if (AuthUtil.userAuthorizedForPage(GlobalReportPage.class))
		{
			linkPage = GlobalReportPage.class;
		}
		else 
		{
			linkPage = UserReport.class;
		}
		
		addLink(parent, id, linkPage, inclSeperator);
		
		return true;
	}
	
	/**
	 * Add link to hierarchy, visibility off if user is not authorized to view the page
	 * @param parent
	 * @param id
	 * @param linkPage
	 */
	private <L extends WebPage> boolean addLink(WebMarkupContainer parent, String id, Class<L> linkPage, boolean inclSeperator)
	{
		BookmarkablePageLink<L>	link;
		
		boolean isVisible = AuthUtil.userAuthorizedForPage(linkPage);
		
		link = new BookmarkablePageLink<L>(id + "Link", linkPage);
		link.setVisible(isVisible);
		parent.add(link);
		
		Label label = new Label(id + "Seperator", "&nbsp;&nbsp;|&nbsp;&nbsp;");
		label.setEscapeModelStrings(false);
		label.setVisible(inclSeperator && isVisible);
		parent.add(label);
		
		return isVisible;
	}	
}