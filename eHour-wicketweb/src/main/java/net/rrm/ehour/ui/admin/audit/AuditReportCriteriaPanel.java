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

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.IModel;

public class AuditReportCriteriaPanel extends AbstractFormSubmittingPanel<ReportCriteria> {
    private static final long serialVersionUID = -5442954150653475254L;

    public AuditReportCriteriaPanel(String id, IModel<ReportCriteria> model) {
        super(id, model);

        addComponents(model);
    }

    private void addComponents(IModel<ReportCriteria> model) {
        Border greyBorder = new GreyBlueRoundedBorder(AuditConstants.PATH_FORM_BORDER);
        add(greyBorder);

        AuditReportCriteriaForm form = new AuditReportCriteriaForm(AuditConstants.ID_FORM, model);
        greyBorder.add(form);
    }
}
