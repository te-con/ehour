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

package net.rrm.ehour.ui.pm.page;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.component.PlaceholderPanel;
import net.rrm.ehour.ui.common.component.TabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.common.page.AbstractBasePage;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
import net.rrm.ehour.ui.pm.panel.PmReportPanel;

import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaPanel;
import net.rrm.ehour.ui.report.panel.criteria.ReportTabbedPanel;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.*;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Project management base station :)
 **/
@AuthorizeInstantiation("ROLE_PROJECTMANAGER")
public class ProjectManagement extends AbstractBasePage<ReportCriteria>
{
	private static final long serialVersionUID = 898442184509251553L;
    public static final String REPORT_PANEL = "reportPanel";

    @SpringBean
	private ProjectService	projectService;

	@SpringBean
	private AggregateReportService	aggregateReportService;

	public ProjectManagement()
	{
		super(new ResourceModel("pmReport.title"));
		
		ReportCriteria reportCriteria = getReportCriteria();
		
		IModel<ReportCriteria>	model = new CompoundPropertyModel<ReportCriteria>(reportCriteria);
		setDefaultModel(model);
		
		// add criteria
		add(new ProjectManagementReportCriteriaPanel("sidePanel", model));
		
		add(new PlaceholderPanel(REPORT_PANEL));
	}

	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == ReportCriteriaAjaxEventType.CRITERIA_UPDATED && ajaxEvent.getTarget() != null)
		{
            List<ITab> tabList = new ArrayList<ITab>();

            final ProjectManagerReport report = getReportData();

            EhourConfig config = EhourWebSession.getSession().getEhourConfig();

            tabList.add(new AbstractTab(new Model<String>(report.getProject().getFullName()))
            {
                private static final long serialVersionUID = 1L;

                @SuppressWarnings("unchecked")
                @Override
                public Panel getPanel(String panelId)
                {
                    return new PmReportPanel(panelId, report);
                }
            });


            TabbedPanel reportPanel = new TabbedPanel(REPORT_PANEL, tabList);
            addOrReplace(reportPanel);
			ajaxEvent.getTarget().addComponent(reportPanel);
		}
		
		return true;
	}

	private ReportCriteria getReportCriteria()
	{
		ReportCriteria reportCriteria = new ReportCriteria();
		
		User user = EhourWebSession.getSession().getUser().getUser();
		
		List<Project> projects = projectService.getProjectManagerProjects(user);
		
		AvailableCriteria availCriteria = reportCriteria.getAvailableCriteria();
		availCriteria.setProjects(projects);
		
		return reportCriteria;
	}
	
	private ProjectManagerReport getReportData()
	{
		ReportCriteria 	criteria = getPageModelObject();

		ProjectManagerReport reportData = null;
		DateRange	reportRange = criteria.getUserCriteria().getReportRange();
		
		if (criteria.getUserCriteria().isInfiniteStartDate())
		{
			reportRange.setDateStart(null);
		}

		if (criteria.getUserCriteria().isInfiniteEndDate())
		{
			reportRange.setDateEnd(null);
		}

		if (criteria.getUserCriteria().getProject() != null)
		{
			// only one can be there
			Project project = criteria.getUserCriteria().getProject();
			reportData = aggregateReportService.getProjectManagerDetailedReport(reportRange, project.getPK());
		}
		
		return reportData;
	}

}
