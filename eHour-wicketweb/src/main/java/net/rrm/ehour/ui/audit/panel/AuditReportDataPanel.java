package net.rrm.ehour.ui.audit.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.audit.model.AuditReportCriteriaModel;
import net.rrm.ehour.ui.audit.model.AuditReportDataProvider;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.datatable.AjaxDataTable;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.Model;

public class AuditReportDataPanel extends AbstractAjaxPanel
{
	private static final long serialVersionUID = -2380789244030608920L;

	public AuditReportDataPanel(String id, AuditReportCriteriaModel model)
	{
		super(id, model);

		setOutputMarkupId(true);
		
		addComponents(model);
	}
	
	/**
	 * Add components to the page
	 */
	private void addComponents(AuditReportCriteriaModel model)
	{
		Border greyBorder = new GreyRoundedBorder("border", 450);
		add(greyBorder);
		
		greyBorder.add(getPagingDataView(model));
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	private WebMarkupContainer getPagingDataView(AuditReportCriteriaModel model)
	{
		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupId(true);
		final EhourConfig config = EhourWebSession.getSession().getEhourConfig();
        
		IColumn[] columns = new IColumn[4];
        columns[0] = new PropertyColumn(new Model("Date"), "date", "date");
        columns[1] = new PropertyColumn(new Model("Last Name"), "userFullName", "userFullName");
        columns[2] = new PropertyColumn(new Model("Action"), "action", "action");
        columns[3] = new PropertyColumn(new Model("Type"), "auditActionType.value", "auditActionType.value");

        AjaxDataTable table = new AjaxDataTable("data", columns, new AuditReportDataProvider(model.getRequest()), 10);
		dataContainer.add(table);
		
		return dataContainer;
	}
}
