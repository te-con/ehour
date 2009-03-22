/**
 * Created on Sep 17, 2007
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

package net.rrm.ehour.ui.report.user.page;

import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.panel.user.UserReportPanel;

import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

/**
 * Printed version of the report
 **/

@AuthorizeInstantiation("ROLE_CONSULTANT")
public class UserReportPrint extends WebPage
{
	private static final long serialVersionUID = -8492629550038561271L;

	/**
	 * 
	 * @param parameters
	 */
	public UserReportPrint(String reportId)
	{
		super();
		
		StringResourceModel pageTitle = new StringResourceModel("userReport.printTitle", 
				this, null, new Object[]{new Model(((EhourWebSession)getSession()).getUser().getUser().getFullName())});

		
		add(new Label("pageTitle", pageTitle)); 
		
		EhourWebSession session = (EhourWebSession)Session.get();
		CustomerAggregateReport report = (CustomerAggregateReport)session.getReportCache().getObjectFromCache(reportId);

		add(new UserReportPanel("userReportPanel", report, false));
	}
}
