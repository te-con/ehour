/**
 * Created on Sep 19, 2007
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

package net.rrm.ehour.ui.page.report;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;

/**
 * Base page for report criteria
 **/

public abstract class BaseReportPage extends BasePage
{
	@SpringBean
	private ReportCriteriaService	reportCriteriaService;

	/**
	 * 
	 * @param pageTitle
	 * @param model
	 */
	public BaseReportPage(ResourceModel pageTitle, IModel model)
	{
		super(pageTitle, model);
	}

	/**
	 * Get report criteria
	 * @return
	 */
	protected ReportCriteria getReportCriteria()
	{
		UserCriteria userCriteria = EhourWebSession.getSession().getUserCriteria();
		
		if (userCriteria == null)
		{
			userCriteria = initUserCriteria();
			EhourWebSession.getSession().setUserCriteria(userCriteria);
		}
		
		userCriteria.setSingleUser(true);
		
		return reportCriteriaService.getReportCriteria(userCriteria);
	}
	
	/**
	 * Initialize user criteria 
	 */
	private UserCriteria initUserCriteria()
	{
		UserCriteria userCriteria = new UserCriteria();
		userCriteria.setSingleUser(true);
		userCriteria.setUser(EhourWebSession.getSession().getUser().getUser());
		
		userCriteria.setReportRange(DateUtil.getDateRangeForMonth(DateUtil.getCalendar(EhourWebSession.getSession().getEhourConfig())));
		
		return userCriteria;
	}	
	
}
