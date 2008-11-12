/**
 * Created on May 8, 2007
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

package net.rrm.ehour.ui.panel.nav;

import net.rrm.ehour.ui.page.admin.mainconfig.MainConfig;
import net.rrm.ehour.ui.page.login.Login;
import net.rrm.ehour.ui.page.pm.ProjectManagement;
import net.rrm.ehour.ui.page.report.global.GlobalReportPage;
import net.rrm.ehour.ui.page.user.Overview;
import net.rrm.ehour.ui.page.user.prefs.UserPreferencePage;
import net.rrm.ehour.ui.page.user.print.PrintMonthSelection;
import net.rrm.ehour.ui.page.user.report.UserReport;
import net.rrm.ehour.ui.panel.AbstractBasePanel;
import net.rrm.ehour.ui.util.AuthUtil;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;

/**
 * Main navigation panel 
 **/

public class MainNavPanel extends AbstractBasePanel
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
		wasAdded |= addLink(this, "print", PrintMonthSelection.class, wasAdded);
		wasAdded |= addLink(this, "overview", Overview.class, wasAdded);
		
		add(new BookmarkablePageLink("logoffLink", Login.class));
	}
	
	/**
	 * 
	 */
	private void addLoggedInUser()
	{
		BookmarkablePageLink link = new BookmarkablePageLink("prefsLink", UserPreferencePage.class);
		add(link);
		
		Label loggedInUserLabel = new Label("loggedInUser", new Model(AuthUtil.getUser().getFullName()) );
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
	private boolean addLink(WebMarkupContainer parent, String id, Class<? extends WebPage> linkPage, boolean inclSeperator)
	{
		BookmarkablePageLink	link;
		
		boolean isVisible = AuthUtil.userAuthorizedForPage(linkPage);
		
		link = new BookmarkablePageLink(id + "Link", linkPage);
		link.setVisible(isVisible);
		parent.add(link);
		
		Label label = new Label(id + "Seperator", "&nbsp;&nbsp;|&nbsp;&nbsp;");
		label.setEscapeModelStrings(false);
		label.setVisible(inclSeperator && isVisible);
		parent.add(label);
		
		return isVisible;
	}	
}