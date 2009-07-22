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
		else
		{
			setSingleUserCriteria(singleUser, userCriteria);
		}
		
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
		
		setSingleUserCriteria(singleUser, userCriteria);
		
		userCriteria.setReportRange(DateUtil.getDateRangeForMonth(DateUtil.getCalendar(EhourWebSession.getSession().getEhourConfig())));
		
		return userCriteria;
	}

	/**
	 * @param singleUser
	 * @param userCriteria
	 */
	private void setSingleUserCriteria(boolean singleUser, UserCriteria userCriteria)
	{
		userCriteria.setSingleUser(singleUser);
		
		if (singleUser)
		{
			userCriteria.setUser(EhourWebSession.getSession().getUser().getUser());
		}
	}	
}
