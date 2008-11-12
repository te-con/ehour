/**
 * 
 */
package net.rrm.ehour.ui.audit.page;

import net.rrm.ehour.ui.audit.model.AuditReportCriteriaModel;
import net.rrm.ehour.ui.audit.panel.AuditReportCriteriaPanel;
import net.rrm.ehour.ui.audit.panel.AuditReportDataPanel;
import net.rrm.ehour.ui.page.BasePage;

import org.apache.wicket.model.ResourceModel;

/**
 * @author thies
 *
 */
public class AuditReportPage extends BasePage
{
	private AuditReportCriteriaPanel criteriaPanel;
	private AuditReportDataPanel dataPanel;
	
	public AuditReportPage()
	{
		super(new ResourceModel("audit.report.title"));
	
		AuditReportCriteriaModel model = new AuditReportCriteriaModel();
		
		criteriaPanel = new AuditReportCriteriaPanel("reportCriteria", model);
		add(criteriaPanel);
		
		dataPanel = new AuditReportDataPanel("reportData", model);
		add(dataPanel);
	}
}
