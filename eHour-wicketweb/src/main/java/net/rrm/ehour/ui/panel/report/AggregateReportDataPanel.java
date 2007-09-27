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
import java.util.Arrays;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.model.CurrencyModel;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.report.aggregate.AggregateReport;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.ui.util.HtmlUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

/**
 * Aggregate report data panel
 **/

public class AggregateReportDataPanel extends Panel
{
	private static final long serialVersionUID = -6757047600645464803L;
	private static final Logger	logger = Logger.getLogger(AggregateReportDataPanel.class);
	private AggregateReportColumn[]	reportColumns;
	
	/**
	 * Default constructor 
	 * @param id
	 * @param report report data
	 */
	public AggregateReportDataPanel(String id, AggregateReport report, ReportType reportType)
	{
		super(id);
		
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		add(blueBorder);
		
		initReportColumns(reportType);
		
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
	private void addGrandTotal(AggregateReport report, WebMarkupContainer parent)
	{
		RepeatingView	totalView = new RepeatingView("cell");
		int				i = 0;
		float			hours = 0;
		float			turnOver = 0;

		EhourConfig config = ((EhourWebSession)this.getSession()).getEhourConfig();
		
		// get totals
		for (ReportNode node : report.getNodes())
		{
			turnOver += node.getTurnover().floatValue();
			hours += node.getHours().floatValue();
		}
		
		// add cells
		totalView.add(new Label(Integer.toString(i++), new ResourceModel("report.total")));
		
		for (; i < reportColumns.length; i++)
		{
			if (reportColumns[i].isVisible())
			{
				Label label = null;
				
				if (reportColumns[i].getColumnType() == AggregateReportColumn.ColumnType.OTHER)
				{
					label = HtmlUtil.getNbspLabel(Integer.toString(i));
				}
				else if (reportColumns[i].getColumnType() == AggregateReportColumn.ColumnType.HOUR)
				{
					label = new Label(Integer.toString(i), new FloatModel(hours, config));
				}
				else if (reportColumns[i].getColumnType() == AggregateReportColumn.ColumnType.TURNOVER)
				{
					label = new Label(Integer.toString(i), new CurrencyModel(turnOver, config));
				}
				
				addColumnTypeStyling(reportColumns[i].getColumnType(), label);
				totalView.add(label);
			}
		}
		
		parent.add(totalView);
	}
	
	/**
	 * Add report data table to the component
	 * @param report
	 * @param parent
	 */
	private void addReportData(AggregateReport report, WebMarkupContainer parent)
	{
		@SuppressWarnings("serial")
		ListView rootNodeView = new ListView("reportData", report.getNodes())
		{
			@Override
			protected void populateItem(ListItem item)
			{
				ReportNode rootNode = (ReportNode)item.getModelObject();

				item.add(getReportNodeRows(rootNode));
				item.add(getTotalRow(rootNode));
			}
		};
		
		parent.add(rootNodeView);
	}

	/**
	 * Add the total row for a block (root node)
	 * @param reportNode
	 * @return
	 */
	private Component getTotalRow(ReportNode reportNode)
	{
		RepeatingView	totalView = new RepeatingView("cell");
		
		EhourConfig config = ((EhourWebSession)this.getSession()).getEhourConfig();
		
		int i = 0;
		
		for (AggregateReportColumn column : reportColumns)
		{
			if (column.isVisible())
			{
				String	id = Integer.toString(i++);
				if (column.getColumnType() == AggregateReportColumn.ColumnType.OTHER)
				{
					totalView.add(HtmlUtil.getNbspLabel(id));
				}
				else if (column.getColumnType() == AggregateReportColumn.ColumnType.HOUR)
				{
					totalView.add(new Label(id, new FloatModel(reportNode.getHours(), config)));
				}
				else if (column.getColumnType() == AggregateReportColumn.ColumnType.TURNOVER)
				{
					totalView.add(new Label(id, new CurrencyModel(reportNode.getTurnover(), config)));
				}
			}
		}
		
		return totalView;
	}
	
	/**
	 * Get root node rows & cells
	 * @param reportNode
	 * @return
	 */
	private Component getReportNodeRows(ReportNode reportNode)
	{
		Serializable[][]		matrix = reportNode.getNodeMatrix(reportColumns.length);
	
		// add rows per node
		@SuppressWarnings("serial")
		ListView rootNodeView = new ListView("row", Arrays.asList(matrix))
		{
			@Override
			protected void populateItem(ListItem item)
			{
				RepeatingView cells = new RepeatingView("cell");
				Serializable[] rowValues = (Serializable[])item.getModelObject();
				int i = 0;
				
				// add cells for a row
				for (Serializable cellValue : rowValues)
				{
					if (reportColumns[i].isVisible())
					{
						Label cellLabel;
					
						if (reportColumns[i].getConversionModel() == null)
						{
							cellLabel = new Label(Integer.toString(i), new Model(cellValue));
						}
						else
						{
							IModel model;

							try
							{
								model = getModelInstance(reportColumns[i]);
								model.setObject(cellValue);
							} catch (Exception e)
							{
								logger.warn("Could not instantiate model", e);
								model = new Model(cellValue);
							}
							
							cellLabel = new Label(Integer.toString(i), model);
							addColumnTypeStyling(reportColumns[i].getColumnType(), cellLabel);
							
						}
						
						cells.add(cellLabel);
					}
					
					i++;
				}
				
				item.add(cells);
				
				if (item.getIndex() % 2 == 1)
				{
					item.add(new SimpleAttributeModifier("style", "background-color: #fefeff"));
				}
			}
			
		};

		return rootNodeView;
	}

	/**
	 * Get a model instance
	 * @param columnHeader
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 */
	@SuppressWarnings("unchecked")
	private IModel getModelInstance(AggregateReportColumn columnHeader) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
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
				// let's not make it too complex, just check argument length and not check types..
				if (constructor.getParameterTypes().length == columnHeader.getConversionModelConstructorParams().length)
				{
					model = (IModel)constructor.newInstance(columnHeader.getConversionModelConstructorParams());
					break;
				}
			}
		}
		
		return model;
	}
	
	/**
	 * Add header columns to parent
	 * @param parent
	 */
	private void addHeaderColumns(WebMarkupContainer parent)
	{
		RepeatingView	columnHeaders = new RepeatingView("columnHeaders");
		int				i = 0;
		
		for (AggregateReportColumn aggregateReportColumn : reportColumns)
		{
			Label columnHeader = new Label(Integer.toString(i++), new ResourceModel(aggregateReportColumn.getColumnHeaderResourceKey()));
			columnHeader.setVisible(aggregateReportColumn.isVisible());
			columnHeaders.add(columnHeader);
			addColumnTypeStyling(aggregateReportColumn.getColumnType(), columnHeader);
			
			logger.debug("Adding report columnheader " + aggregateReportColumn.getColumnHeaderResourceKey() + ", visible: " +  columnHeader.isVisible());
		}
		
		parent.add(columnHeaders);
	}
	
	/**
	 * Add column type specific styling
	 * @param columnType
	 * @param label
	 */
	private void addColumnTypeStyling(AggregateReportColumn.ColumnType columnType, Label label)
	{
		if (columnType != AggregateReportColumn.ColumnType.OTHER)
		{
			label.add(new SimpleAttributeModifier("style", "text-align: right"));
		}
	}
	
	/**
	 * 
	 * @param reportType
	 * @return
	 */
	private void initReportColumns(ReportType reportType)
	{
		EhourConfig config = ((EhourWebSession)this.getSession()).getEhourConfig();
		
		reportColumns = ReportColumnUtil.getReportColumns(config, reportType);
	}
}
