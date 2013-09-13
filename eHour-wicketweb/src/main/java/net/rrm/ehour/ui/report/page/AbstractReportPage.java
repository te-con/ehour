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
import net.rrm.ehour.ui.common.page.AbstractBasePage;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;
import org.apache.log4j.Logger;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Base page for report criteria
 **/

public abstract class AbstractReportPage<T> extends AbstractBasePage<T>
{
	@SpringBean
	private ReportCriteriaService	reportCriteriaService;

	protected static final Logger logger = Logger.getLogger(AbstractReportPage.class);

	public AbstractReportPage(ResourceModel pageTitle)
	{
		super(pageTitle, null);
	}

	protected final ReportCriteria getReportCriteria()
	{
		UserCriteria userCriteria = EhourWebSession.getSession().getUserCriteria();
		
		if (userCriteria == null)
		{
			userCriteria = initUserCriteria();
		}

        setSingleUserCriteria(userCriteria);
		
		AvailableCriteria availableCriteria = newAvailableCriteria();

		ReportCriteria criteria = new ReportCriteria(availableCriteria, userCriteria);
		
		return reportCriteriaService.syncUserReportCriteria(criteria, ReportCriteriaUpdateType.UPDATE_ALL);
	}
	
	/**
	 * Get a fresh new shiny AvailableCriteria obj
	 * @return
	 */
	protected AvailableCriteria newAvailableCriteria()
	{
		return new AvailableCriteria();
	}
	
	/**
	 * Initialize user criteria 
	 */
	private UserCriteria initUserCriteria()
	{
        UserCriteria userCriteria = new UserCriteria();

		userCriteria.setReportRange(DateUtil.getDateRangeForMonth(DateUtil.getCalendar(EhourWebSession.getSession().getEhourConfig())));
        EhourWebSession.getSession().setUserCriteria(userCriteria);

		return userCriteria;
	}

	private void setSingleUserCriteria(UserCriteria userCriteria)
	{
        boolean singleReportUser = isReportForSingleUser();

		userCriteria.setSingleUser(singleReportUser);
		
		if (singleReportUser) {
			userCriteria.setUser(EhourWebSession.getSession().getUser().getUser());
		}
	}

    protected boolean isReportForSingleUser() {
        return !EhourWebSession.getSession().isWithReportRole();
    }
}
