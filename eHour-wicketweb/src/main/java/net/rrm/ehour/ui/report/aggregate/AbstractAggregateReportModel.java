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

package net.rrm.ehour.ui.report.aggregate;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.ui.report.model.TreeReportModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created on Mar 12, 2009, 10:21:18 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public abstract class AbstractAggregateReportModel extends TreeReportModel {
    private static final long serialVersionUID = -7459442244875136235L;
    @SpringBean
    private AggregateReportService aggregateReportService;

    public AbstractAggregateReportModel(ReportCriteria reportCriteria, ReportConfig reportConfig) {
        super(reportCriteria, reportConfig);
    }

    @Override
    protected final ReportData fetchReportData(ReportCriteria reportCriteria) {
        return getAggregateReportService().getAggregateReportData(reportCriteria);
    }

    private AggregateReportService getAggregateReportService() {
        if (aggregateReportService == null) {
            WebUtils.springInjection(this);
        }

        return aggregateReportService;
    }
}
