/**
 * Created on Jul 9, 2007
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

package net.rrm.ehour.ui.page.user.report;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.page.user.report.criteria.UserReportCriteriaPanel;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Reporting for user
 **/

@AuthorizeInstantiation("ROLE_CONSULTANT")
public class UserReport extends BasePage
{
	private static final long serialVersionUID = -8867366237264687482L;
	
	@SpringBean
	private ReportCriteriaService	reportCriteriaService;
	private	UserCriteria			userCriteria;
	
	/**
	 * 
	 */
	public UserReport()
	{
		super("reporting", null);
		
		ReportCriteria reportCriteria = getReportCriteria();
		setModel(new CompoundPropertyModel(reportCriteria));
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));
		
		add(new UserReportCriteriaPanel("sidePanel", reportCriteria));
	}
	
	/**
	 * Get report criteria
	 * @return
	 */
	private ReportCriteria getReportCriteria()
	{
		userCriteria = EhourWebSession.getSession().getUserCriteria();
		
		if (userCriteria == null)
		{
			initUserCritieria();
		}
		
		userCriteria.setSingleUser(true);
		
		return reportCriteriaService.getReportCriteria(userCriteria);
	}
	
	private void initUserCritieria()
	{
		userCriteria = new UserCriteria();
		userCriteria.setSingleUser(true);
		userCriteria.setUser(EhourWebSession.getSession().getUser().getUser());
	}
}
