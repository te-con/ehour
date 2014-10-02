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
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.ui.common.page.AbstractBasePage;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;
import org.apache.log4j.Logger;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Base page for report criteria
 */

public abstract class AbstractReportPage<T> extends AbstractBasePage<T> {
    @SpringBean
    private ReportCriteriaService reportCriteriaService;

    protected static final Logger LOGGER = Logger.getLogger(AbstractReportPage.class);

    public AbstractReportPage(ResourceModel pageTitle) {
        super(pageTitle, null);
    }

    protected final ReportCriteria getReportCriteria() {
        UserSelectedCriteria userSelectedCriteria = getUserSelectedCriteria();

        AvailableCriteria availableCriteria = new AvailableCriteria();

        ReportCriteria criteria = new ReportCriteria(availableCriteria, userSelectedCriteria);

        return reportCriteriaService.syncUserReportCriteria(criteria, ReportCriteriaUpdateType.UPDATE_ALL);
    }

    protected UserSelectedCriteria getUserSelectedCriteria() {
        EhourWebSession session = getEhourWebSession();
        UserSelectedCriteria userSelectedCriteria = session.getUserSelectedCriteria();

        if (userSelectedCriteria == null) {
            userSelectedCriteria = initUserCriteria();
            session.setUserSelectedCriteria(userSelectedCriteria);
        }

        return userSelectedCriteria;
    }

    protected UserSelectedCriteria initUserCriteria() {
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();

        userSelectedCriteria.setReportRange(DateUtil.getDateRangeForMonth(DateUtil.getCalendar(EhourWebSession.getEhourConfig())));

        determineDefaultReportType(userSelectedCriteria);

        return userSelectedCriteria;
    }

    protected void determineDefaultReportType(UserSelectedCriteria userSelectedCriteria) {
        if (getEhourWebSession().isReporter()) {
            userSelectedCriteria.setReportTypeToGlobal();
        } else if (isReportForPm()) {
            userSelectedCriteria.setReportTypeToPM(EhourWebSession.getUser());
        } else {
            userSelectedCriteria.setReportTypeToIndividualUser(EhourWebSession.getUser());
        }
    }
    private boolean isReportForPm() {
        return getEhourWebSession().isProjectManager();
    }
}
