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
import net.rrm.ehour.ui.page.report.ReportPage;
import net.rrm.ehour.ui.page.user.Overview;
import net.rrm.ehour.ui.page.user.report.UserReport;
import net.rrm.ehour.ui.util.AuthUtil;

import org.apache.wicket.Localizer;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.Model;

/**
 * Main navigation panel 
 **/

public class MainNavPanel extends Panel
{
	private static final long serialVersionUID = 854412484275829659L;

	/**
	 * 
	 * @param id
	 */
	public MainNavPanel(String id)
	{
		super(id);
		
		addLink(this, "overviewLink", Overview.class);
		addReportLink(this, "userReportLink");
		addLink(this, "adminLink", MainConfig.class);
		addLink(this, "logoffLink", Login.class);
		
		Localizer localizer = getLocalizer();
		add(new Label("loggedInUser", 
					localizer.getString("nav.loggedinas", this, new Model(AuthUtil.getUser()))));
		
		add(new StyleSheetReference("headerStyle", headerStyle()));
	}

	/**
	 * Add report link, global reporting when user has report role, user report when user is consultant
	 * @param parent
	 * @param id
	 */
	private void addReportLink(WebMarkupContainer parent, String id)
	{
		Class<? extends WebPage> 	linkPage;
		
		if (AuthUtil.userAuthorizedForPage(ReportPage.class))
		{
			linkPage = ReportPage.class;
		}
		else 
		{
			linkPage = UserReport.class;
		}
		
		addLink(parent, id, linkPage);
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