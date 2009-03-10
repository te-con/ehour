/**
 * 
 */
package net.rrm.ehour.ui.audit.panel;

import net.rrm.ehour.ui.audit.AuditConstants;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;

import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.IModel;

/**
 * @author thies
 *
 */
public class AuditReportCriteriaPanel extends AbstractFormSubmittingPanel
{
	private static final long serialVersionUID = -5442954150653475254L;

	public AuditReportCriteriaPanel(String id, IModel model)
	{
		super(id, model);
		
		addComponents(model);
	}
	
	private void addComponents(IModel model)
	{
		Border greyBorder = new GreyBlueRoundedBorder(AuditConstants.PATH_FORM_BORDER);
		add(greyBorder);
		
		AuditReportCriteriaForm form = new AuditReportCriteriaForm(AuditConstants.ID_FORM, model);
		greyBorder.add(form);
	}
}
