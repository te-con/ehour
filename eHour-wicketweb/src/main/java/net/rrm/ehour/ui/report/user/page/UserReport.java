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

package net.rrm.ehour.ui.report.user.page;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.page.AbstractReportPage;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
import net.rrm.ehour.ui.report.panel.user.UserReportPanel;
import net.rrm.ehour.ui.report.panel.user.criteria.UserReportCriteriaPanel;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Reporting for user
 **/

@AuthorizeInstantiation("ROLE_CONSULTANT")
public class UserReport extends AbstractReportPage
{
	private static final long serialVersionUID = -8867366237264687482L;

	private WebMarkupContainer		reportDataPanel;

	public UserReport()
	{
		super(new ResourceModel("userreport.title"));
		
		ReportCriteria reportCriteria = getReportCriteria(true);
		IModel	model = new CompoundPropertyModel(reportCriteria);
		setModel(model);

		// add criteria
		add(new UserReportCriteriaPanel("sidePanel", model));

		// add data
		reportDataPanel = getReport();
		add(reportDataPanel);
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
			WebMarkupContainer	replacement = getReport();
			reportDataPanel.replaceWith(replacement);
			reportDataPanel = replacement;
			ajaxEvent.getTarget().addComponent(replacement);
		}
		
		return false;
	}
	
	/**
	 * Get report
	 * @return
	 */
	private WebMarkupContainer getReport()
	{
		ReportCriteria criteria = (ReportCriteria)getModelObject();
		
		// add data
		CustomerAggregateReport	customerAggregateReport = new CustomerAggregateReport(criteria);
		EhourWebSession.getSession().getObjectCache().addObjectToCache(customerAggregateReport);
		
		UserReportPanel panel = new UserReportPanel("userReportPanel", customerAggregateReport, UserReportPanel.Option.INCLUDE_LINKS);
		panel.setOutputMarkupId(true);
		return panel;
	}
}
