/**
 * 
 */
package net.rrm.ehour.ui.audit.page;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.ui.admin.BaseAdminPage;
import net.rrm.ehour.ui.audit.AuditConstants;
import net.rrm.ehour.ui.audit.panel.AuditReportCriteriaForm;
import net.rrm.ehour.ui.audit.panel.AuditReportCriteriaPanel;
import net.rrm.ehour.ui.audit.panel.AuditReportDataPanel;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

/**
 * @author thies
 *
 */
public class AuditReportPage extends BaseAdminPage 
{
	
	public AuditReportPage()
	{
		super(new ResourceModel("audit.report.title"), 
					new CompoundPropertyModel(new AuditReportRequest()), "audit.help.header", "audit.help.body");
	
		GreyRoundedBorder greyBorder = new GreyRoundedBorder(AuditConstants.PATH_FRAME, new ResourceModel("audit.report.title"));
		add(greyBorder);
		
		AuditReportCriteriaPanel criteriaPanel = new AuditReportCriteriaPanel(AuditConstants.PATH_CRITERIA, getModel());
		greyBorder.add(criteriaPanel);
		
		AuditReportDataPanel dataPanel = new AuditReportDataPanel(AuditConstants.PATH_DATA, getModel());
		greyBorder.add(dataPanel);
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
