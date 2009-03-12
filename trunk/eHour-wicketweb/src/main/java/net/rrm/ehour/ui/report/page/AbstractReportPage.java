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

package net.rrm.ehour.ui.report.page;

import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.ui.common.page.BasePage;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Base page for report criteria
 **/

public abstract class AbstractReportPage extends BasePage
{
	@SpringBean
	private ReportCriteriaService	reportCriteriaService;

	protected final static Logger logger = Logger.getLogger(AbstractReportPage.class);

	/**
	 * 
	 * @param pageTitle
	 * @param model
	 */
	public AbstractReportPage(ResourceModel pageTitle)
	{
		super(pageTitle, null);
	}

	/**
	 * Get report criteria
	 * @return
	 */
	protected ReportCriteria getReportCriteria(boolean singleUser)
	{
		UserCriteria userCriteria = EhourWebSession.getSession().getUserCriteria();
		
		if (userCriteria == null)
		{
			userCriteria = initUserCriteria(singleUser);
			EhourWebSession.getSession().setUserCriteria(userCriteria);
		}
		
		userCriteria.setSingleUser(singleUser);
		
		AvailableCriteria availableCriteria = getAvailableCriteria();
		
		ReportCriteria criteria = new ReportCriteria(availableCriteria, userCriteria);
		
		return reportCriteriaService.syncUserReportCriteria(criteria, ReportCriteriaUpdateType.UPDATE_ALL);
	}
	
	/**
	 * Get a fresh new shiny AvailableCriteria obj
	 * @return
	 */
	protected AvailableCriteria getAvailableCriteria()
	{
		return new AvailableCriteria();
	}
	
	/**
	 * Initialize user criteria 
	 */
	private UserCriteria initUserCriteria(boolean singleUser)
	{
		UserCriteria userCriteria = new UserCriteria();
		
		userCriteria.setSingleUser(singleUser);
		
		if (singleUser)
		{
			userCriteria.setUser(EhourWebSession.getSession().getUser().getUser());
		}
		
		userCriteria.setReportRange(DateUtil.getDateRangeForMonth(DateUtil.getCalendar(EhourWebSession.getSession().getEhourConfig())));
		
		return userCriteria;
	}	
}
