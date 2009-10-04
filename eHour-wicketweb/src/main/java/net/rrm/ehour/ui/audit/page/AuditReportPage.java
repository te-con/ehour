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

package net.rrm.ehour.ui.audit.page;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.admin.AbstractAdminPage;
import net.rrm.ehour.ui.audit.AuditConstants;
import net.rrm.ehour.ui.audit.panel.AuditReportCriteriaForm;
import net.rrm.ehour.ui.audit.panel.AuditReportCriteriaPanel;
import net.rrm.ehour.ui.audit.panel.AuditReportDataPanel;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

/**
 * @author thies
 *
 */
public class AuditReportPage extends AbstractAdminPage 
{
	
	public AuditReportPage()
	{
		super(new ResourceModel("audit.report.title"), new Model(), "audit.help.header", "audit.help.body");
		setModel(getReportCriteriaModel());
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder(AuditConstants.PATH_FRAME, new ResourceModel("audit.report.title"));
		add(greyBorder);
		
		AuditReportCriteriaPanel criteriaPanel = new AuditReportCriteriaPanel(AuditConstants.PATH_CRITERIA, getModel());
		greyBorder.add(criteriaPanel);
		
		AuditReportDataPanel dataPanel = new AuditReportDataPanel(AuditConstants.PATH_DATA, getModel());
		greyBorder.add(dataPanel);
	}
	
	private IModel getReportCriteriaModel()
	{
		AuditReportRequest auditReportRequest = new AuditReportRequest();
		ReportCriteria criteria = new ReportCriteria(auditReportRequest);
		
		return new CompoundPropertyModel(criteria);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.page.BasePage#ajaxEventReceived(net.rrm.ehour.ui.common.ajax.AjaxEvent)
	 */
	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == AuditReportCriteriaForm.Events.FORM_SUBMIT)
		{
			ajaxEvent.getTarget().addComponent(get(AuditConstants.PATH_FRAME + ":" + AuditConstants.PATH_DATA));
			
			return false;
		}
		
		return true;
	}
}
