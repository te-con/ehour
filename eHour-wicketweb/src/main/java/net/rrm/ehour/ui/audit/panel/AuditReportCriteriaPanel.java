/**
 * 
 */
package net.rrm.ehour.ui.audit.panel;

import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.panel.AbstractFormSubmittingPanel;

import org.apache.wicket.markup.html.border.Border;

/**
 * @author thies
 *
 */
public class AuditReportCriteriaPanel extends AbstractFormSubmittingPanel
{
	private static final long serialVersionUID = -5442954150653475254L;

	public AuditReportCriteriaPanel(String id)
	{
		super(id);
		
		addComponents();
	}
	
	private void addComponents()
	{
		Border greyBorder = new GreyBlueRoundedBorder("border");
		add(greyBorder);
		
		AuditReportCriteriaForm form = new AuditReportCriteriaForm("criteriaForm");
		greyBorder.add(form);
	}
}
