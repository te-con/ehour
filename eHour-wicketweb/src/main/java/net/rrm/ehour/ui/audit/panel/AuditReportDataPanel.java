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

package net.rrm.ehour.ui.audit.panel;

import java.util.Date;

import net.rrm.ehour.audit.dao.AuditReportRequest;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Audit;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.audit.model.AuditReportDataProvider;
import net.rrm.ehour.ui.audit.report.AuditReport;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.component.datatable.AjaxDataTable;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.value.ValueMap;

public class AuditReportDataPanel extends AbstractAjaxPanel<ReportCriteria>
{
	private static final long serialVersionUID = -2380789244030608920L;

	public AuditReportDataPanel(String id, IModel<ReportCriteria> model)
	{
		super(id, model);

		setOutputMarkupId(true);
		
		addComponents(model);
	}
	
	/**
	 * Add components to the page
	 */
	private void addComponents(IModel<ReportCriteria> model)
	{
		Border greyBorder = new GreyBlueRoundedBorder("border");
		add(greyBorder);
		
		greyBorder.add(getPagingDataView(model));
		
		add(new StyleSheetReference("auditStyle", new CompressedResourceReference(AuditReportDataPanel.class, "style/auditStyle.css")));
		
		addExcelLink();
	}

	private void addExcelLink()
	{
		ResourceReference excelResource = new ResourceReference("auditReportExcel");
		ValueMap params = new ValueMap();
		
		ReportCriteria criteria = (ReportCriteria)AuditReportDataPanel.this.getDefaultModelObject();
		
		AuditReport auditReport = new AuditReport(criteria);
		final String reportId = getEhourWebSession().getObjectCache().addObjectToCache(auditReport);
		params.add("reportId", reportId);
		
		Link<?> excelLink = new ResourceLink<Void>("excelLink", excelResource, params);
		add(excelLink);
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private WebMarkupContainer getPagingDataView(IModel<ReportCriteria> model)
	{
		final WebMarkupContainer dataContainer = new WebMarkupContainer("dataContainer");
		dataContainer.setOutputMarkupId(true);
		final EhourConfig config = EhourWebSession.getSession().getEhourConfig();
        
		IColumn<Audit>[] columns = new IColumn[4];
        columns[0] = new DateColumn(new ResourceModel("audit.report.column.date"), config);
        columns[1] = new PropertyColumn<Audit>(new ResourceModel("audit.report.column.lastName"), "userFullName");
        columns[2] = new PropertyColumn<Audit>(new ResourceModel("audit.report.column.action"), "action");
        columns[3] = new PropertyColumn<Audit>(new ResourceModel("audit.report.column.type"), "auditActionType.value");

        AjaxDataTable<Audit> table = new AjaxDataTable<Audit>("data", columns, new AuditReportDataProvider(getReportRequest(model)), 20);
		dataContainer.add(table);
		
		return dataContainer;
	}
	
	private AuditReportRequest getReportRequest(IModel<ReportCriteria> model)
	{
		ReportCriteria criteria = model.getObject();
		
		return (AuditReportRequest)criteria.getUserCriteria();
	}
	
	private class DateColumn extends AbstractColumn<Audit>
	{
		private static final long serialVersionUID = -5517077439980001335L;
		private EhourConfig config;
		
		public DateColumn(IModel<String> displayModel, EhourConfig config)
		{
			super(displayModel);
				
			this.config = config;
		}

		/*
		 * (non-Javadoc)
		 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item, java.lang.String, org.apache.wicket.model.IModel)
		 */
		public void populateItem(Item<ICellPopulator<Audit>> item, String componentId, IModel<Audit> model)
		{
			Date date = model.getObject().getDate();
			item.add(new Label(componentId, new DateModel(date, config, DateModel.DATESTYLE_DATE_TIME)));
		}
	}
}
