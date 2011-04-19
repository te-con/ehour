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
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.ResourceModel;

/**
 * Overview page
 */

@AuthorizeInstantiation("ROLE_CONSULTANT")
public class MonthOverviewPage extends AbstractBasePage<Void>
{
    private static final long serialVersionUID = -6873845464139697303L;
    private static final String ID_CONTENT_CONTAINER = "contentContainer";

    public enum OpenPanel
    {
        OVERVIEW, TIMESHEET
    }

    public static final String PARAM_OPEN = "openPanel";

    private CalendarPanel calendarPanel;
    private ContextualHelpPanel helpPanel;

    public MonthOverviewPage()
    {
        this(OpenPanel.OVERVIEW);
    }

    public MonthOverviewPage(PageParameters parameters)
    {
        this(parameters.getAsEnum(PARAM_OPEN, OpenPanel.class));
    }

    /**
     * @param panelToOpen
     */
    public MonthOverviewPage(OpenPanel panelToOpen)
    {
        super(new ResourceModel("overview.title"), null);

        // add calendar panel
        calendarPanel = new CalendarPanel("sidePanel", getEhourWebSession().getUser().getUser());
        add(calendarPanel);

        WebMarkupContainer contentContainer;

        if (panelToOpen == OpenPanel.OVERVIEW)
        {
            helpPanel = new ContextualHelpPanel("contextHelp", "overview.help.header", "overview.help.body", "Month+overview");
            contentContainer = new OverviewPanel(ID_CONTENT_CONTAINER);
        } else
        {
            calendarPanel.setHighlightWeekStartingAt(DateUtil.getDateRangeForWeek(EhourWebSession.getSession().getNavCalendar()));
            helpPanel = getTimesheetHelpPanel();
            contentContainer = getTimesheetPanel();
        }

        add(helpPanel);
        addOrReplaceContentContainer(contentContainer);
    }

    /**
     * Handle Ajax request
     *
     * @param target
     */
    @Override
    public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
    {
        AjaxEventType type = ajaxEvent.getEventType();
        AjaxRequestTarget target = ajaxEvent.getTarget();

        if (type == CalendarAjaxEventType.MONTH_CHANGE)
        {
            calendarChanged(target);
        } else if (type == CalendarAjaxEventType.WEEK_CLICK
                || type == TimesheetAjaxEventType.WEEK_NAV)
        {
            calendarWeekClicked(target);
            calendarPanel.setHighlightWeekStartingAt(DateUtil.getDateRangeForWeek(EhourWebSession.getSession().getNavCalendar()));
            calendarPanel.refreshCalendar(target);
        } else if (type == TimesheetAjaxEventType.TIMESHEET_SUBMIT)
        {
            calendarPanel.refreshCalendar(target);
        }

        return false;
    }

    /**
     * Calendar week clicked
     *
     * @param target
     */
    private void calendarWeekClicked(AjaxRequestTarget target)
    {
        TimesheetPanel panel = getTimesheetPanel();
        addOrReplaceContentContainer(panel, target);

        ContextualHelpPanel replacementHelp = getTimesheetHelpPanel();
        helpPanel.replaceWith(replacementHelp);
        helpPanel = replacementHelp;
        target.addComponent(replacementHelp);

    }

    /**
     * Calendar changed, update panels
     *
     * @param target
     */
    private void calendarChanged(AjaxRequestTarget target)
    {
        WebMarkupContainer replacementPanel;

        if (this.get(ID_CONTENT_CONTAINER) instanceof TimesheetPanel)
        {
            replacementPanel = getTimesheetPanel();
        } else
        {
            replacementPanel = new OverviewPanel(ID_CONTENT_CONTAINER);
        }

        addOrReplaceContentContainer(replacementPanel, target);
    }

    private void addOrReplaceContentContainer(WebMarkupContainer contentContainer)
    {
        contentContainer.setOutputMarkupId(true);
        addOrReplace(contentContainer);
    }

    private void addOrReplaceContentContainer(WebMarkupContainer contentContainer, AjaxRequestTarget target)
    {
        addOrReplaceContentContainer(contentContainer);
        AjaxRequestTarget.get().addComponent(contentContainer);
    }

    /**
     * Get timesheet panel for current user & current month
     *
     * @return
     */
    private TimesheetPanel getTimesheetPanel()
    {
        return new TimesheetPanel(ID_CONTENT_CONTAINER,
                getEhourWebSession().getUser().getUser(),
                getEhourWebSession().getNavCalendar());
    }

    /**
     * @return
     */
    private ContextualHelpPanel getTimesheetHelpPanel()
    {
        ContextualHelpPanel helpPanel = new ContextualHelpPanel("contextHelp",
                "timesheet.help.header",
                "timesheet.help.body",
                "Entering+hours");

        helpPanel.setOutputMarkupId(true);

        return helpPanel;
    }
}
