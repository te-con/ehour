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

package net.rrm.ehour.ui.panel.report.user;

import java.io.Serializable;

import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.report.aggregate.AggregateReport;
import net.rrm.ehour.ui.report.aggregate.value.ReportNode;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.ResourceModel;

/**
 * Abstract report panel
 **/

public abstract class AbstractAggregateReportPanel extends Panel
{
	private static final long serialVersionUID = -6757047600645464803L;
	private static final Logger	logger = Logger.getLogger(AbstractAggregateReportPanel.class);
	/**
	 * 
	 * @param id
	 * @param report
	 */
	public AbstractAggregateReportPanel(String id, AggregateReport report)
	{
		super(id);
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("reportFrame", "Report");
		add(greyBorder);
		
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		greyBorder.add(blueBorder);
		
		addHeaderColumns(blueBorder);
		
		add(new StyleSheetReference("reportStyle", new CompressedResourceReference(this.getClass(), "style/reportStyle.css")));
	}
	
	private void addReportData(AggregateReport report, WebMarkupContainer parent)
	{
		ListView viewNodes = new ListView("reportData", report.getNodes())
		{

			@Override
			protected void populateItem(ListItem item)
			{
				ReportNode rootNode = (ReportNode)item.getModelObject();
				
			}
		};
	}
	
	private void addReportNodeColumns(int column, ReportNode node, int hideFromHierarchyLevel, WebMarkupContainer parent)
	{
		for (Serializable columnValue : node.getColumnValues())
		{
			if (!getReportColumns()[column].isVisible())
			{
				// column not visible
			}
			else if (node.getHierarchyLevel() >= hideFromHierarchyLevel)
			{
				// show column
			}
			else
			{
				// add nbsp as column
			}
		}
	}
	
	/**
	 * Add column headers
	 */
	private void addHeaderColumns(WebMarkupContainer parent)
	{
		AggregateReportColumn[]	reportColumns = getReportColumns();
		RepeatingView	columnHeaders = new RepeatingView("columnHeaders");
		int				i = 0;
		
		for (AggregateReportColumn aggregateReportColumn : reportColumns)
		{
			Label columnHeader = new Label(Integer.toString(i++), new ResourceModel(aggregateReportColumn.getColumnHeaderResourceKey()));
			columnHeader.setVisible(aggregateReportColumn.isVisible());
			columnHeaders.add(columnHeader);
			
			logger.debug("Adding report columnheader " + aggregateReportColumn.getColumnHeaderResourceKey() + ", visible: " +  columnHeader.isVisible());
		}
		
		parent.add(columnHeaders);
	}
	
	/**
	 * Get report columns
	 * @return
	 */
	protected abstract AggregateReportColumn[] getReportColumns();
	
	
}
