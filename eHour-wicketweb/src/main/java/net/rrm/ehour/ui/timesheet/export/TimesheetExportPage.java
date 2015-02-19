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

package net.rrm.ehour.ui.timesheet.export;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.ui.common.border.CustomTitledGreyRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventListener;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.calendar.CalendarAjaxEventType;
import net.rrm.ehour.ui.common.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.common.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.page.AbstractReportPage;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Print month page
 */
@AuthorizeInstantiation(UserRole.ROLE_USER)
public class TimesheetExportPage extends AbstractReportPage<ReportCriteria> implements AjaxEventListener {
    private static final long serialVersionUID = 1891959724639181159L;

    private static final String ID_SELECTION_FORM = "selectionForm";
    private static final String ID_FRAME = "printSelectionFrame";
    private static final String ID_BLUE_BORDER = "blueBorder";

    private TimesheetExportCriteriaPanel criteriaPanel;
    private CustomTitledGreyRoundedBorder greyBorder;

    public TimesheetExportPage() {
        this(EhourWebSession.getSession().getNavCalendar());
    }

    public TimesheetExportPage(Calendar forMonth) {
        super(new ResourceModel("printMonth.title"));

        setDefaultModel(createModelForMonth(forMonth));

        add(createCalendarPanel("sidePanel"));

        add(new ContextualHelpPanel("contextHelp", "printMonth.help.header", "printMonth.help.body"));

        greyBorder = new CustomTitledGreyRoundedBorder(ID_FRAME, newTitleLabel(forMonth));
        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder(ID_BLUE_BORDER);
        greyBorder.add(blueBorder);
        add(greyBorder);

        criteriaPanel = createExportCriteriaPanel(ID_SELECTION_FORM);
        blueBorder.add(criteriaPanel);
    }

    private TimesheetExportCriteriaPanel createExportCriteriaPanel(String id) {
        return new TimesheetExportCriteriaPanel(id, getPageModel());
    }

    private CalendarPanel createCalendarPanel(String id) {
        return new CalendarPanel(id, EhourWebSession.getUser(), false);
    }

    private CompoundPropertyModel<ReportCriteria> createModelForMonth(Calendar forMonth) {
        ReportCriteria reportCriteria = getReportCriteria();

        reportCriteria.getUserSelectedCriteria().setSelectedReportType(UserSelectedCriteria.ReportType.INDIVIDUAL_USER);
        reportCriteria.getUserSelectedCriteria().setReportRange(DateUtil.getDateRangeForMonth(forMonth));

        if (reportCriteria.getUserSelectedCriteria().getProjects() == null) {
            reportCriteria.getUserSelectedCriteria().setProjects(new ArrayList<Project>());
        }

        return new CompoundPropertyModel<>(reportCriteria);
    }

    @Override
    protected void determineDefaultReportType(UserSelectedCriteria userSelectedCriteria) {
        userSelectedCriteria.setReportTypeToIndividualUser(EhourWebSession.getUser());
    }

    @Override
    protected UserSelectedCriteria getUserSelectedCriteria() {
        return initUserCriteria();
    }

    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        if (ajaxEvent.getEventType() == CalendarAjaxEventType.MONTH_CHANGE) {
            changeMonth(ajaxEvent);
        }

        return true;
    }

    private void changeMonth(AjaxEvent ajaxEvent) {
        setDefaultModel(createModelForMonth(getEhourWebSession().getNavCalendar()));

        TimesheetExportCriteriaPanel replacementPanel = createExportCriteriaPanel(ID_SELECTION_FORM);

        criteriaPanel.replaceWith(replacementPanel);
        ajaxEvent.getTarget().add(replacementPanel);
        criteriaPanel = replacementPanel;

        Label newLabel = newTitleLabel(getEhourWebSession().getNavCalendar());
        greyBorder.replaceInBorder(newLabel);
        ajaxEvent.getTarget().add(newLabel);
    }

    private Label newTitleLabel(Calendar cal) {
        Label label = new Label("title", new StringResourceModel("printMonth.header",
                this, null,
                new Object[]{new DateModel(cal, getConfig(), DateModel.DATESTYLE_MONTHONLY)}));
        label.setOutputMarkupId(true);
        return label;
    }
}
