/**
 * Created on Sep 12, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.report;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.model.CurrencyModel;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.TreeReportDataProvider;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.HtmlUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
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
	public TreeReportDataPanel(String id, TreeReport report, ReportConfig reportConfig, String excelResourceName)
	{
		super(id);
		
		this.reportConfig = reportConfig;
		
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		add(blueBorder);
		
		if (excelResourceName != null)
		{
			final String reportId = report.getReportId();
			
			ResourceReference excelResource = new ResourceReference(excelResourceName);
			ValueMap params = new ValueMap();
			params.add("reportId", reportId);
			ResourceLink excelLink = new ResourceLink("excelLink", excelResource, params);
			add(excelLink);

			EhourConfig config = ((EhourWebSession)getSession()).getEhourConfig();
			
			add(new Label("reportHeader",new StringResourceModel("report.header", 
											this, null, 
													new Object[]{new DateModel(report.getReportRange().getDateStart(), config),
										 			new DateModel(report.getReportRange().getDateEnd(), config)})));		
		}
		else
		{
			add(HtmlUtil.getInvisibleLink("excelLink"));
			add(HtmlUtil.getInvisibleLabel("reportHeader"));
		}
		
		addHeaderColumns(blueBorder);
		addReportData(report, blueBorder);
		addGrandTotal(report, blueBorder);
		
		add(new StyleSheetReference("reportStyle", new CompressedResourceReference(this.getClass(), "style/reportStyle.css")));
	}
	
	/**
	 * Grand total row
	 * @param report
	 * @param parent
	 */
	private void addGrandTotal(TreeReport report, WebMarkupContainer parent)
	{
		RepeatingView	totalView = new RepeatingView("cell");
		int				i = 0;

		EhourConfig config = ((EhourWebSession)this.getSession()).getEhourConfig();
		
		// add cells
		totalView.add(new Label(Integer.toString(i++), new ResourceModel("report.total")));
		
		for (; i < reportConfig.getReportColumns().length; i++)
		{
			if (reportConfig.getReportColumns()[i].isVisible())
			{
				Label label = null;
				
				if (reportConfig.getReportColumns()[i].getColumnType() == TreeReportColumn.ColumnType.HOUR)
				{
					label = new Label(Integer.toString(i), new FloatModel(report.getTotalHours() , config));
				}
				else if (reportConfig.getReportColumns()[i].getColumnType() == TreeReportColumn.ColumnType.TURNOVER)
				{
					label = new Label(Integer.toString(i), new CurrencyModel(report.getTotalTurnover(), config));
					label.setEscapeModelStrings(false);
				}
				else
				{
					label = HtmlUtil.getNbspLabel(Integer.toString(i));
				}
				
				addColumnTypeStyling(reportConfig.getReportColumns()[i].getColumnType(), label);
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
	private void addReportData(TreeReport report, WebMarkupContainer parent)
	{
		DataView dataView = new TreeReportDataView("reportData", new TreeReportDataProvider(report.getReportMatrix()));

		dataView.setItemsPerPage(20);
		
		parent.add(new PagingNavigator("navigator", dataView));
		parent.add(dataView);
	}

	/**
	 * Get a model instance based on the TreeReportColumn arguments
	 * @param columnHeader
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	@SuppressWarnings("unchecked")
	private IModel getModelInstance(TreeReportColumn columnHeader) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
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
		
		for (TreeReportColumn treeReportColumn : reportConfig.getReportColumns())
		{
			Label columnHeader = new Label(Integer.toString(i++), new ResourceModel(treeReportColumn.getColumnHeaderResourceKey()));
			columnHeader.setVisible(treeReportColumn.isVisible());
			columnHeaders.add(columnHeader);
			addColumnTypeStyling(treeReportColumn.getColumnType(), columnHeader);
			
			logger.debug("Adding report columnheader " + treeReportColumn.getColumnHeaderResourceKey() + ", visible: " +  columnHeader.isVisible());
		}
		
		parent.add(columnHeaders);
	}
	
	/**
	 * Add column type specific styling
	 * @param columnType
	 * @param label
	 */
	private void addColumnTypeStyling(TreeReportColumn.ColumnType columnType, Label label)
	{
		if (columnType != TreeReportColumn.ColumnType.OTHER && label != null)
		{
			label.add(new SimpleAttributeModifier("style", "text-align: right"));
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
			Serializable[] rowValues = (Serializable[])item.getModelObject();
			int i = 0;
			
			List<Serializable> thisCellValues = new ArrayList<Serializable>();
			
			// add cells for a row
			for (Serializable cellValue : rowValues)
			{
				if (reportConfig.getReportColumns()[i].isVisible())
				{
					Label cellLabel;
					
					thisCellValues.add(cellValue);
				
					if (isDuplicate(i, cellValue))
					{
						cellLabel = new Label(Integer.toString(i), new Model(""));
					}
					else if (reportConfig.getReportColumns()[i].getConversionModel() == null)
					{
						cellLabel = new Label(Integer.toString(i), new Model(cellValue));
					}
					else
					{
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
					&& previousCellValues.get(i).equals(cellValue));			
		}
	};		
}
