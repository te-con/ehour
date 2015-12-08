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

package net.rrm.ehour.ui.admin.audit;

import net.rrm.ehour.data.AuditReportRequest;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.admin.AbstractAdminPage;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

public class AuditReportPage extends AbstractAdminPage<String> {

    public AuditReportPage() {
        super(new ResourceModel("audit.report.title"), new Model<String>());

        IModel<ReportCriteria> criteriaModel = getReportCriteriaModel();
        setDefaultModel(criteriaModel);

        GreyRoundedBorder greyBorder = new GreyRoundedBorder(AuditConstants.PATH_FRAME, new ResourceModel("audit.report.title"));
        add(greyBorder);

        AuditReportCriteriaPanel criteriaPanel = new AuditReportCriteriaPanel(AuditConstants.PATH_CRITERIA, criteriaModel);
        greyBorder.add(criteriaPanel);

        AuditReportDataPanel dataPanel = new AuditReportDataPanel(AuditConstants.PATH_DATA, criteriaModel);
        greyBorder.add(dataPanel);
    }

    private IModel<ReportCriteria> getReportCriteriaModel() {
        AuditReportRequest auditReportRequest = new AuditReportRequest();
        ReportCriteria criteria = new ReportCriteria(auditReportRequest);

        return new CompoundPropertyModel<>(criteria);
    }

    @Override
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        if (ajaxEvent.getEventType() == AuditReportCriteriaForm.Events.FORM_SUBMIT) {
            String path = AuditConstants.PATH_FRAME + ":frame_body:" + AuditConstants.PATH_DATA;

            ajaxEvent.getTarget().add(get(path));

            return false;
        }

        return true;
    }
}
