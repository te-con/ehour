/**
 * 
 */
package net.rrm.ehour.ui.audit.page;

import net.rrm.ehour.audit.service.dto.AuditReportRequest;
import net.rrm.ehour.ui.audit.model.AuditReportCriteriaModel;
import net.rrm.ehour.ui.audit.panel.AuditReportCriteriaPanel;
import net.rrm.ehour.ui.audit.panel.AuditReportDataPanel;
import net.rrm.ehour.ui.page.admin.BaseAdminPage;

import org.apache.wicket.model.ResourceModel;

/**
 * @author thies
 *
 */
public class AuditReportPage extends BaseAdminPage
{
	private AuditReportCriteriaPanel criteriaPanel;
	private AuditReportDataPanel dataPanel;
	
	public AuditReportPage()
	{
		super(new ResourceModel("audit.report.title"), 
					new AuditReportCriteriaModel(new AuditReportRequest()), "audit.help.header", "audit.help.body");
	
		criteriaPanel = new AuditReportCriteriaPanel("reportCriteria", (AuditReportCriteriaModel)getModel());
		add(criteriaPanel);
		
		dataPanel = new AuditReportDataPanel("reportData", (AuditReportCriteriaModel)getModel());
		add(dataPanel);
	}
}
