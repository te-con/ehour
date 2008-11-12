package net.rrm.ehour.ui.audit.panel;

import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.panel.AbstractAjaxPanel;

import org.apache.wicket.markup.html.border.Border;

public class AuditReportDataPanel extends AbstractAjaxPanel
{
	private static final long serialVersionUID = -2380789244030608920L;

	public AuditReportDataPanel(String id)
	{
		super(id);

		setOutputMarkupId(true);
		
		Border greyBorder = new GreyRoundedBorder("border", 450);
		add(greyBorder);
	}

}
