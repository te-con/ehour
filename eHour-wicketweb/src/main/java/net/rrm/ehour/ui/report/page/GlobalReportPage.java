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

package net.rrm.ehour.ui.report.page;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.page.command.DefaultGlobalReportPageAggregateCommand;
import net.rrm.ehour.ui.report.page.command.DefaultGlobalReportPageDetailedCommand;
import net.rrm.ehour.ui.report.page.command.GlobalReportPageAggregateCommand;
import net.rrm.ehour.ui.report.page.command.GlobalReportPageDetailedCommand;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaPanel;
import net.rrm.ehour.ui.report.panel.criteria.ReportTabbedPanel;
import net.rrm.ehour.ui.report.panel.criteria.type.ReportType;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Reporting 
 **/

@AuthorizeInstantiation("ROLE_REPORT")
public class GlobalReportPage extends AbstractReportPage<ReportCriteriaBackingBean>
{
	private static final long serialVersionUID = 6614404841734599622L;
	
	private ReportTabbedPanel tabPanel;
	private GlobalReportPageAggregateCommand aggregateCommand;
	private GlobalReportPageDetailedCommand detailedCommand;
	
	public GlobalReportPage()
	{
		this(new DefaultGlobalReportPageAggregateCommand(), new DefaultGlobalReportPageDetailedCommand());
	}
	
	public GlobalReportPage(GlobalReportPageAggregateCommand aggregateCommand, GlobalReportPageDetailedCommand detailedCommand)
	{
		super(new ResourceModel("report.global.title"));

		this.aggregateCommand = aggregateCommand;
		this.detailedCommand = detailedCommand;
		
		final ReportCriteria reportCriteria = getReportCriteria(false);
		final IModel<ReportCriteriaBackingBean> model = new CompoundPropertyModel<ReportCriteriaBackingBean>(new ReportCriteriaBackingBean(reportCriteria));
		setDefaultModel(model);		
		
		List<ITab> tabList = new ArrayList<ITab>();
		
		tabList.add(new AbstractTab(new KeyResourceModel("report.criteria.title"))
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public Panel getPanel(String panelId)
			{
				return new ReportCriteriaPanel(panelId, (IModel<ReportCriteriaBackingBean>)getDefaultModel());
			}
		});
		
		tabPanel = new ReportTabbedPanel("reportContainer", tabList);
		add(tabPanel);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.page.BasePage#ajaxEventReceived(net.rrm.ehour.ui.common.ajax.AjaxEvent)
	 */
	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == ReportCriteriaAjaxEventType.CRITERIA_UPDATED)
		{
			ReportCriteriaBackingBean backingBean = (ReportCriteriaBackingBean)getDefaultModelObject();
	
			clearTabs();
			
			if (backingBean.getReportType().equals(ReportType.AGGREGATE))
			{
				addAggregateReportPanelTabs	(backingBean);
			}
			else
			{
				addDetailedReportPanelTabs(backingBean);
			}
			
			ajaxEvent.getTarget().addComponent(tabPanel);
		}
		
		return false;
	}
	

	/**
	 * Clear tabs except for the first one
	 */
	private void clearTabs()
	{
		List<ITab> tabs = tabPanel.getTabs();
		
		while (tabs.size() > 1)
		{
			tabs.remove(1);
		}
	}
	
	/**
	 * Get the aggregate report panel
	 */
	private void addAggregateReportPanelTabs(ReportCriteriaBackingBean backingBean)
	{
		List<ITab> tabs = aggregateCommand.createAggregateReportTabs(backingBean);

		addTabs(tabs);
		
		tabPanel.setSelectedTab(1);
	}

	private void addTabs(List<ITab> tabs)
	{
		for (ITab iTab : tabs)
		{
			tabPanel.addTab(iTab);
		}
	}
	
	/**
	 * Get the report panel
	 */
	private void addDetailedReportPanelTabs(ReportCriteriaBackingBean backingBean)
	{
		List<ITab> tabs = detailedCommand.createDetailedReportTabs(backingBean);
		
		addTabs(tabs);
		
		tabPanel.setSelectedTab(1);	
	}
}
