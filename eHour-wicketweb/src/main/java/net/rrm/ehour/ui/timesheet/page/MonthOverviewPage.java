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

package net.rrm.ehour.ui.timesheet.page;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.page.AbstractBasePage;
import net.rrm.ehour.ui.common.panel.calendar.CalendarAjaxEventType;
import net.rrm.ehour.ui.common.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.common.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.common.TimesheetAjaxEventType;
import net.rrm.ehour.ui.timesheet.panel.OverviewPanel;
import net.rrm.ehour.ui.timesheet.panel.TimesheetPanel;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import java.util.Calendar;

@AuthorizeInstantiation(UserRole.ROLE_USER)
public class MonthOverviewPage extends AbstractBasePage<Void> {
    private static final long serialVersionUID = -6873845464139697303L;
    private static final String ID_CONTENT_CONTAINER = "contentContainer";

    public enum OpenPanel {
        OVERVIEW, TIMESHEET
    }

    public static final String PARAM_OPEN = "openPanel";

    private CalendarPanel calendarPanel;
    private ContextualHelpPanel helpPanel;

    public MonthOverviewPage() {
        this(OpenPanel.OVERVIEW);
    }

    public MonthOverviewPage(PageParameters parameters) {
        super(new ResourceModel("overview.title"), null);


        StringValue param = parameters.get(PARAM_OPEN);

        if (param != null) {
            init(OpenPanel.valueOf(param.toOptionalString()));
        } else {
            init(OpenPanel.OVERVIEW);
        }
    }

    public MonthOverviewPage(OpenPanel panelToOpen) {
        super(new ResourceModel("overview.title"), null);

        init(panelToOpen);
    }

    private void init(OpenPanel panelToOpen) {
        // add calendar panel
        calendarPanel = new CalendarPanel("sidePanel", EhourWebSession.getUser());
        add(calendarPanel);

        WebMarkupContainer contentContainer;

        if (panelToOpen == OpenPanel.OVERVIEW) {
            helpPanel = new ContextualHelpPanel("contextHelp", "overview.help.header", "overview.help.body");
            contentContainer = new OverviewPanel(ID_CONTENT_CONTAINER);
        } else {
            calendarPanel.setHighlightWeekStartingAt(DateUtil.getDateRangeForWeek(EhourWebSession.getSession().getNavCalendar()));
            helpPanel = getTimesheetHelpPanel();
            contentContainer = createTimesheetPanel(ID_CONTENT_CONTAINER, EhourWebSession.getUser(), getEhourWebSession().getNavCalendar());
        }

        add(helpPanel);
        addOrReplaceContentContainer(contentContainer);
    }

    @Override
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();
        AjaxRequestTarget target = ajaxEvent.getTarget();

        if (type == CalendarAjaxEventType.MONTH_CHANGE) {
            calendarChanged(target);
        } else if (type == CalendarAjaxEventType.WEEK_CLICK
                || type == TimesheetAjaxEventType.WEEK_NAV) {
            calendarWeekClicked(target);
            calendarPanel.setHighlightWeekStartingAt(DateUtil.getDateRangeForWeek(EhourWebSession.getSession().getNavCalendar()));
            calendarPanel.refreshCalendar(target);
        } else if (type == TimesheetAjaxEventType.TIMESHEET_SUBMIT) {
            calendarPanel.refreshCalendar(target);
        }

        return false;
    }

    /**
     * Calendar week clicked
     *
     * @param target
     */
    private void calendarWeekClicked(AjaxRequestTarget target) {
        TimesheetPanel panel = createTimesheetPanel(ID_CONTENT_CONTAINER, EhourWebSession.getUser(), getEhourWebSession().getNavCalendar());
        addOrReplaceContentContainer(panel, target);

        ContextualHelpPanel replacementHelp = getTimesheetHelpPanel();
        helpPanel.replaceWith(replacementHelp);
        helpPanel = replacementHelp;
        target.add(replacementHelp);

    }

    /**
     * Calendar changed, update panels
     *
     * @param target
     */
    private void calendarChanged(AjaxRequestTarget target) {
        WebMarkupContainer replacementPanel;

        if (this.get(ID_CONTENT_CONTAINER) instanceof TimesheetPanel) {
            replacementPanel = createTimesheetPanel(ID_CONTENT_CONTAINER, EhourWebSession.getUser(), getEhourWebSession().getNavCalendar());
        } else {
            replacementPanel = new OverviewPanel(ID_CONTENT_CONTAINER);
        }

        addOrReplaceContentContainer(replacementPanel, target);
    }

    private void addOrReplaceContentContainer(WebMarkupContainer contentContainer) {
        contentContainer.setOutputMarkupId(true);
        addOrReplace(contentContainer);
    }

    private void addOrReplaceContentContainer(WebMarkupContainer contentContainer, AjaxRequestTarget target) {
        addOrReplaceContentContainer(contentContainer);
        target.add(contentContainer);
    }

    /**
     * Get timesheet panel for current user & current month
     */
    protected TimesheetPanel createTimesheetPanel(String id, User user, Calendar forWeek) {
        return new TimesheetPanel(id, user, forWeek);
    }

    private ContextualHelpPanel getTimesheetHelpPanel() {
        ContextualHelpPanel helpPanel = new ContextualHelpPanel("contextHelp",
                "timesheet.help.header",
                "timesheet.help.body"
        );

        helpPanel.setOutputMarkupId(true);

        return helpPanel;
    }
}
