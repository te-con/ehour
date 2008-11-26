/**
 * 
 */
package net.rrm.ehour.ui.audit.page;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.ui.admin.BaseAdminPage;
import net.rrm.ehour.ui.audit.panel.AuditReportCriteriaForm;
import net.rrm.ehour.ui.audit.panel.AuditReportCriteriaPanel;
import net.rrm.ehour.ui.audit.panel.AuditReportDataPanel;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

/**
 * @author thies
 *
 */
public class AuditReportPage extends BaseAdminPage 
{
	private final static String PATH_CRITERIA = "reportCriteria";
	private final static String PATH_DATA = "reportData";
	
	public AuditReportPage()
	{
		super(new ResourceModel("audit.report.title"), 
					new CompoundPropertyModel(new AuditReportRequest()), "audit.help.header", "audit.help.body");
	
		AuditReportCriteriaPanel criteriaPanel = new AuditReportCriteriaPanel(PATH_CRITERIA, getModel());
		add(criteriaPanel);
		
		AuditReportDataPanel dataPanel = new AuditReportDataPanel(PATH_DATA, getModel());
		add(dataPanel);
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
			ajaxEvent.getTarget().addComponent(get(PATH_DATA));
			
			return false;
		}
		return true;
	}

}
