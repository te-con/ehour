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

package net.rrm.ehour.ui.report.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.component.CurrencyLabel;
import net.rrm.ehour.ui.common.component.HoverPagingNavigator;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.report.ReportColumn;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.TreeReportDataProvider;
import net.rrm.ehour.ui.report.TreeReportElement;
import org.apache.log4j.Logger;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.value.ValueMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate report data panel
 **/

public class TreeReportDataPanel extends Panel
{
	private static final long serialVersionUID = -6757047600645464803L;
	private static final Logger	logger = Logger.getLogger(TreeReportDataPanel.class);
	
	private ReportConfig reportConfig;
	
	/**
	 * Default constructor 
	 * @param id
	 * @param report report data
	 */
	public TreeReportDataPanel(String id, 
								TreeReport report, 
								ReportConfig reportConfig, 
								String excelResourceName,
								int reportWidth)
	{
		super(id);
		
		this.reportConfig = reportConfig;
		
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		add(blueBorder);
		blueBorder.setOutputMarkupId(true);
		
		WebMarkupContainer header = new WebMarkupContainer("header");
		header.add(new SimpleAttributeModifier("style", "width: " + Integer.toString(reportWidth) + "px"));
		add(header);
		
		if (excelResourceName != null)
		{
			final String reportId = report.getCacheId();
			
			ResourceReference excelResource = new ResourceReference(excelResourceName);
			ValueMap params = new ValueMap();
			params.add("reportId", reportId);
			ResourceLink<Void> excelLink = new ResourceLink<Void>("excelLink", excelResource, params);
			header.add(excelLink);

			EhourConfig config = EhourWebSession.getSession().getEhourConfig();
			
			header.add(getReportHeaderLabel("reportHeader", report.getReportRange(), config));
		} else {
			header.add(HtmlUtil.getInvisibleLink("excelLink"));
			header.add(HtmlUtil.getInvisibleLabel("reportHeader"));
		}
		
		addHeaderColumns(blueBorder);
		addReportData(report, blueBorder);
		addGrandTotal(report, blueBorder);
		
		add(new StyleSheetReference("reportStyle", new CompressedResourceReference(TreeReportDataPanel.class, "style/reportStyle.css")));
	}
	
	/**
	 * Get report header label
	 * @param reportRange
	 * @param config
	 * @return
	 */
	protected Label getReportHeaderLabel(String id, DateRange reportRange, EhourConfig config)
	{
		Label reportHeaderLabel = new Label(id, new StringResourceModel("report.header", 
													this, null, 
													new Object[]{new DateModel(reportRange.getDateStart(), config),
												 	new DateModel(reportRange.getDateEnd(), config)}));
		reportHeaderLabel.setEscapeModelStrings(false);
		
		return reportHeaderLabel;
	}
	
	/**
	 * Grand total row
	 * @param report
	 * @param parent
	 */
	private void addGrandTotal(TreeReport report, WebMarkupContainer parent)
	{
		RepeatingView	totalView = new RepeatingView("cell");
		int				id = 0;
		boolean			totalLabelAdded = false;

		// add cells
		for (int column = 0; column < reportConfig.getReportColumns().length; column++, id++)
		{
			if (reportConfig.getReportColumns()[column].isVisible())
			{
				Label label;
				
				if (reportConfig.getReportColumns()[column].getColumnType() == ReportColumn.ColumnType.HOUR)
				{
					label = new Label(Integer.toString(id), new Model<Float>(report.getTotalHours()));
				}
				else if (reportConfig.getReportColumns()[column].getColumnType() == ReportColumn.ColumnType.TURNOVER)
				{
					label = new CurrencyLabel(Integer.toString(id), report.getTotalTurnover());
					label.setEscapeModelStrings(false);
				}
				else if (!totalLabelAdded)
				{
					label = new Label(Integer.toString(id), new ResourceModel("report.total"));
					totalLabelAdded = true;
				}
				else
				{
					label = HtmlUtil.getNbspLabel(Integer.toString(id));
				}
				
				addColumnTypeStyling(reportConfig.getReportColumns()[column].getColumnType(), label);
				totalView.add(label);
			}
		}
		
		parent.add(totalView);
	}
	
	/**
	 * Get root node rows & cells
	 * @param reportNode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void addReportData(TreeReport report, WebMarkupContainer parent)
	{
		List<TreeReportElement> elements = (List<TreeReportElement>)report.getReportData().getReportElements();
		
		DataView<TreeReportElement> dataView = new TreeReportDataView("reportData", new TreeReportDataProvider(elements));
		dataView.setOutputMarkupId(true);
		dataView.setItemsPerPage(20);
		
		parent.add(new HoverPagingNavigator("navigator", dataView));
		parent.add(dataView);
	}
	
	
	/**
	 * Add header columns to parent
	 * @param parent
	 */
	private void addHeaderColumns(WebMarkupContainer parent)
	{
		RepeatingView	columnHeaders = new RepeatingView("columnHeaders");
		int				i = 0;
		
		for (ReportColumn reportColumn : reportConfig.getReportColumns())
		{
			Label columnHeader = new Label(Integer.toString(i++), new ResourceModel(reportColumn.getColumnHeaderResourceKey()));
			columnHeader.setVisible(reportColumn.isVisible());
			columnHeaders.add(columnHeader);
			addColumnTypeStyling(reportColumn.getColumnType(), columnHeader);
			
			logger.debug("Adding report columnheader " + reportColumn.getColumnHeaderResourceKey() + ", visible: " +  columnHeader.isVisible());
		}
		
		parent.add(columnHeaders);
	}
	
	/**
	 * Add column type specific styling
	 * @param columnType
	 * @param label
	 */
	private void addColumnTypeStyling(ReportColumn.ColumnType columnType, Label label)
	{
		StringBuilder style = new StringBuilder();
		
		if (label != null)
		{
			if (columnType != ReportColumn.ColumnType.STRING)
			{
				style.append("text-align: right;");
			}
			
			if (columnType == ReportColumn.ColumnType.HOUR)
			{
				style.append("width: 40px;");
			}
			
			if (columnType == ReportColumn.ColumnType.TURNOVER)
			{
				style.append("width: 70px;");
			}
			
			if (columnType == ReportColumn.ColumnType.COMMENT)
			{
				style.append("width: 300px;");
			}			
			
			if (style.toString() != null && !(style.toString().trim().equals("")))
			{
				label.add(new SimpleAttributeModifier("style", style.toString()));
			}
		}
	}

	private class TreeReportDataView extends DataView<TreeReportElement>
	{
		private static final long serialVersionUID = 1L;
		
		private int previousForPage = -1;
		private List<Serializable> previousCellValues;
		
		public TreeReportDataView(String id, IDataProvider<TreeReportElement> dataProvider)
		{
			super(id, dataProvider);
		}	
		
		/*
		 * (non-Javadoc)
		 * @see org.apache.wicket.markup.repeater.RefreshingView#populateItem(org.apache.wicket.markup.repeater.Item)
		 */
		@Override
		protected void populateItem(Item<TreeReportElement> item)
		{
			RepeatingView cells = new RepeatingView("cell");
			TreeReportElement row = item.getModelObject();
			int i = 0;
			
			List<Serializable> thisCellValues = new ArrayList<Serializable>();
			
			boolean newValueInPreviousColumn = false;
			
			// add cells for a row
			for (Serializable cellValue : row.getRow())
			{
				thisCellValues.add(cellValue);

				if (reportConfig.getReportColumns()[i].isVisible())
				{
					Label cellLabel;
					
					if (isDuplicate(i, cellValue) && !newValueInPreviousColumn)
					{
						cellLabel = new Label(Integer.toString(i), new Model<String>(""));
						newValueInPreviousColumn = false;
					}
					else if (reportConfig.getReportColumns()[i].getConverter()  != null)
					{
                        final IConverter converter = reportConfig.getReportColumns()[i].getConverter();

                        cellLabel = new Label(Integer.toString(i), new Model<Serializable>(cellValue))
                        {
                            @Override
                            public IConverter getConverter(Class<?> type)
                            {
                                return converter;
                            }
                        };
						
						newValueInPreviousColumn = true;
					}
					else
					{
						newValueInPreviousColumn = true;
						
						IModel<Serializable> model = new Model<Serializable>(cellValue);
						
						cellLabel = new Label(Integer.toString(i), model);
						addColumnTypeStyling(reportConfig.getReportColumns()[i].getColumnType(), cellLabel);
					}
					
					cells.add(cellLabel);
				}
				
				i++;
			}
			
			item.add(cells);
			
			setCssStyle(item);
			previousForPage = getCurrentPage();
			previousCellValues = thisCellValues;
		}
		
		/**
		 * Set css style
		 * @param item
		 */
		private void setCssStyle(Item<?> item)
		{
			if (item.getIndex() % 2 == 1)
			{
				item.add(new SimpleAttributeModifier("style", "background-color: #fefeff"));
			}
		}
		
		/**
		 * Is cellvalue a duplicate
		 * @param i
		 * @param cellValue
		 * @return
		 */
		private boolean isDuplicate(int i, Serializable cellValue)
		{
			return (!reportConfig.getReportColumns()[i].isAllowDuplicates()
					&& previousCellValues != null
					&& previousForPage == getCurrentPage()
					&& previousCellValues.get(i) != null
					&& previousCellValues.get(i).equals(cellValue));			
		}
	}
}
