/**
 * Created on Dec 29, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.panel.nav.report;

import net.rrm.ehour.ui.border.GreyNavBorder;
import net.rrm.ehour.ui.page.report.detail.DetailedReportPage;
import net.rrm.ehour.ui.page.report.global.AggregatedReportPage;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Navigation panel for subreports
 **/

public class ReportNavPanel extends Panel
{
	private static final long serialVersionUID = -7877416542663086633L;

	public ReportNavPanel(String id)
	{
		super(id);

		GreyNavBorder greyNavBorder = new GreyNavBorder("navBorder", new ResourceModel("report.nav.title"));

		add(greyNavBorder);
		
		greyNavBorder.add(new BookmarkablePageLink("aggregateReports", AggregatedReportPage.class));
		greyNavBorder.add(new BookmarkablePageLink("detailedReports", DetailedReportPage.class));
	}
}
