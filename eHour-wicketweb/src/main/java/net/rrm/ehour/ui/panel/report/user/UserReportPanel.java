/**
 * Created on Jul 10, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.reports.aggregate.AggregateReportNode;
import net.rrm.ehour.ui.report.reports.aggregate.AggregateReportNode.SectionChild;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

/**
 * Report table
 **/

public class UserReportPanel extends AbstractAggregateReportPanel
{
	private static final long serialVersionUID = -2740688272163704885L;

	private final EhourConfig		config;
	private AggregateReportColumn[]	reportColumns;
	
	/**
	 * 
	 * @param id
	 * @param reportData
	 */
	@SuppressWarnings("serial")
	public UserReportPanel(String id, CustomerAggregateReport reportData)
	{
		super(id, reportData);
		
		config = EhourWebSession.getSession().getEhourConfig();
		
	}

	/**
	 * 
	 * @author Thies
	 *
	 */
	private class CustomerBlock extends ListView
	{
		private static final long serialVersionUID = -1589085260912518831L;
		private final AggregateReportNode<?, ?>	node;
		
		public CustomerBlock(String id, AggregateReportNode<?, ?> node)
		{
			super(id, node.getChildNodes());
			this.node = node;
		}

		@Override
		@SuppressWarnings("unchecked")
		protected void populateItem(ListItem item)
		{
			SectionChild	child = (SectionChild)item.getModelObject();
			
			item.add(new Label("customer", node.getRootValue().getName()));
			item.add(new Label("project", child.getChildValue().getName()));
			item.add(new Label("hours", new FloatModel(child.getHours(), config)));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.panel.report.user.AbstractAggregateReportPanel#getReportColumns()
	 */
	@Override
	protected AggregateReportColumn[] getReportColumns()
	{
		if (reportColumns == null)
		{
			reportColumns = new AggregateReportColumn[]{
									new AggregateReportColumn("userReport.report.customer"),
									new AggregateReportColumn("userReport.report.project"),
									new AggregateReportColumn("userReport.report.projectCode"),
									new AggregateReportColumn("userReport.report.hours"),
									new AggregateReportColumn("userReport.report.turnover", EhourWebSession.getSession().getEhourConfig().isShowTurnover())
							};
		}
		
		return reportColumns;
	}
}
