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

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.component.HoverPagingNavigator;
import net.rrm.ehour.ui.common.model.CurrencyModel;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.FloatModel;
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
import org.apache.wicket.util.value.ValueMap;

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
			ResourceLink excelLink = new ResourceLink("excelLink", excelResource, params);
			header.add(excelLink);

			EhourConfig config = EhourWebSession.getSession().getEhourConfig();
			
			header.add(getReportHeaderLabel("reportHeader", report.getReportRange(), config));
		}
		else
		{
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

		EhourConfig config = EhourWebSession.getSession().getEhourConfig();
		
		// add cells
		for (int column = 0; column < reportConfig.getReportColumns().length; column++, id++)
		{
			if (reportConfig.getReportColumns()[column].isVisible())
			{
				Label label;
				
				if (reportConfig.getReportColumns()[column].getColumnType() == ReportColumn.ColumnType.HOUR)
				{
					label = new Label(Integer.toString(id), new FloatModel(report.getTotalHours() , config));
				}
				else if (reportConfig.getReportColumns()[column].getColumnType() == ReportColumn.ColumnType.TURNOVER)
				{
					label = new Label(Integer.toString(id), new CurrencyModel(report.getTotalTurnover(), config));
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
		
		DataView dataView = new TreeReportDataView("reportData", new TreeReportDataProvider(elements));
		dataView.setOutputMarkupId(true);
		dataView.setItemsPerPage(20);
		
		parent.add(new HoverPagingNavigator("navigator", dataView));
		parent.add(dataView);
	}

	/**
	 * Get a model instance based on the ReportColumn arguments
	 * @param columnHeader
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	@SuppressWarnings("unchecked")
	private IModel getModelInstance(ReportColumn columnHeader) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		IModel model = null;
		
		if (columnHeader.getConversionModelConstructorParams() == null)
		{
			model = columnHeader.getConversionModel().newInstance();
		}
		else
		{
			Constructor[]	constructors = columnHeader.getConversionModel().getConstructors();
			
			for (Constructor constructor : constructors)
			{
				// when no parameter type classes are defined, match only on parameter length,
				// otherwise check the supplied parameter types against the constructor's types
				if (constructor.getParameterTypes().length == columnHeader.getConversionModelConstructorParams().length
						&& (columnHeader.getConversionModelConstructorParamTypes() == null || 
								matchConstructorParamTypes(constructor.getParameterTypes(), columnHeader.getConversionModelConstructorParamTypes())))
				{
					model = (IModel)constructor.newInstance(columnHeader.getConversionModelConstructorParams());
					break;
				}
			}
		}
		
		return model;
	}

	/**
	 * Check if the constructor parameters match the defined parameters
	 * @param constructParamTypes
	 * @param definedParams
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean matchConstructorParamTypes(Class[] constructParamTypes, Class[] definedParams)
	{
		boolean match = true;
		
		for (int i = 0; i < constructParamTypes.length; i++)
		{
			match &= definedParams[i].getName().equals(constructParamTypes[i].getName());
		}
		
		return match;
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
			if (columnType != ReportColumn.ColumnType.OTHER)
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

	/**
	 * 
	 * @author Thies
	 *
	 */
	private class TreeReportDataView extends DataView
	{
		private static final long serialVersionUID = 1L;
		
		private int previousForPage = -1;
		private List<Serializable> previousCellValues;
		
		public TreeReportDataView(String id, IDataProvider dataProvider)
		{
			super(id, dataProvider);
		}	
		
		/*
		 * (non-Javadoc)
		 * @see org.apache.wicket.markup.repeater.RefreshingView#populateItem(org.apache.wicket.markup.repeater.Item)
		 */
		@Override
		protected void populateItem(Item item)
		{
			RepeatingView cells = new RepeatingView("cell");
			TreeReportElement row = (TreeReportElement)item.getModelObject();
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
						cellLabel = new Label(Integer.toString(i), new Model(""));
						newValueInPreviousColumn = false;
					}
					else if (reportConfig.getReportColumns()[i].getConversionModel() == null)
					{
						cellLabel = new Label(Integer.toString(i), new Model(cellValue));
						newValueInPreviousColumn = true;
					}
					else
					{
						newValueInPreviousColumn = true;
						
						IModel model;

						try
						{
							model = getModelInstance(reportConfig.getReportColumns()[i]);
							model.setObject(cellValue);
						} catch (Exception e)
						{
							logger.warn("Could not instantiate model for " + reportConfig.getReportColumns()[i], e);
							model = new Model(cellValue);
						}
						
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
		private void setCssStyle(Item item)
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
	};		
}
