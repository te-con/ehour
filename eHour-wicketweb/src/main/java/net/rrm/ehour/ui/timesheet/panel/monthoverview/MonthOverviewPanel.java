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

package net.rrm.ehour.ui.timesheet.panel.monthoverview;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.TooltipLabel;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.ui.timesheet.export.ExportMonthSelectionPage;
import net.rrm.ehour.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Month overview panel for consultants
 */

public class MonthOverviewPanel extends Panel
{
    private static final long serialVersionUID = -8977205040520638758L;
    private static final Logger LOG = Logger.getLogger(MonthOverviewPanel.class);

    private final TimesheetOverview timesheetOverview;
    private final int thisMonth;
    private final int thisYear;
    private final Calendar overviewFor;

    public MonthOverviewPanel(String id, TimesheetOverview timesheetOverview, final Calendar overviewForMonth)
    {
        super(id);

        setOutputMarkupId(true);

        EhourWebSession session = (EhourWebSession) getSession();
        EhourConfig config = session.getEhourConfig();

        this.timesheetOverview = timesheetOverview;
        thisMonth = overviewForMonth.get(Calendar.MONTH);
        thisYear = overviewForMonth.get(Calendar.YEAR);

        this.overviewFor = (Calendar) overviewForMonth.clone();
        DateUtil.dayOfWeekFix(overviewFor);
        overviewFor.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());

        Link<String> printLink = new Link<String>("printLink")
        {
            private static final long serialVersionUID = 4816200369282788652L;

            @Override
            public void onClick()
            {
                setResponsePage(new ExportMonthSelectionPage(overviewForMonth));
            }
        };

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("greyFrame",
                new ResourceModel("monthoverview.overview"),
                true,
                printLink, null,
                WebGeo.W_CONTENT_MEDIUM);
        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");

        greyBorder.add(blueBorder);
        add(greyBorder);

        addDayLabels(blueBorder, session.getEhourConfig());

        createMonthCalendar(blueBorder);
    }

    /**
     * Create month calendar
     *
     * @param parent
     * @param calendar
     */
    private void createMonthCalendar(WebMarkupContainer parent)
    {
        LOG.debug("Creating month overview calendar for " + overviewFor.getTime().toString());

        RepeatingView calendarView = new RepeatingView("calendarView");

        while ((overviewFor.get(Calendar.YEAR) == thisYear) &&
                (overviewFor.get(Calendar.MONTH) <= thisMonth) || overviewFor.get(Calendar.YEAR) < thisYear)
        {
            WebMarkupContainer row = new WebMarkupContainer(calendarView.newChildId());
            calendarView.add(row);

            createWeek(row);
        }

        parent.add(calendarView);
    }

    /**
     * @param calendar
     * @param calendarView
     * @return
     */
    private void createWeek(WebMarkupContainer row)
    {
        row.add(new Label("weekNumber", Integer.toString(overviewFor.get(Calendar.WEEK_OF_YEAR))));

        addDayNumbersToWeek(row);
        addDayValuesToWeek(row);
    }

    /**
     * Add all the day values to the week
     *
     * @param row
     */
    private void addDayValuesToWeek(WebMarkupContainer row)
    {
        for (int i = 1; i <= 7; i++, overviewFor.add(Calendar.DATE, 1))
        {
            String dayId = "day" + i + "Value";

            if (overviewFor.get(Calendar.MONTH) == thisMonth)
            {
                row.add(createDay(dayId));
            } else
            {
                row.add(createEmptyDay(dayId));
            }
        }
    }

    /**
     * @param dayId
     * @return
     */
    private Fragment createDay(String dayId)
    {
        Fragment fragment;

        List<TimesheetEntry> timesheetEntries = null;

        if (timesheetOverview.getTimesheetEntries() != null)
        {
            timesheetEntries = timesheetOverview.getTimesheetEntries().get(overviewFor.get(Calendar.DAY_OF_MONTH));
        }

        if (timesheetEntries != null
                && timesheetEntries.size() > 0)
        {
            fragment = createDayContents(dayId, timesheetEntries);
        } else
        {
            fragment = new Fragment(dayId, "noProjects", this);
        }

        if (DateUtil.isWeekend(overviewFor))
        {
            fragment.add(new SimpleAttributeModifier("style", "background-color: #eef6fe"));
        }

        return fragment;
    }

    @SuppressWarnings("serial")
    private Fragment createDayContents(String dayId, List<TimesheetEntry> timesheetEntries)
    {
        Fragment fragment;
        fragment = new Fragment(dayId, "showProjects", this);

        ListView<TimesheetEntry> projects = new ListView<TimesheetEntry>("projects", timesheetEntries)
        {
            @Override
            protected void populateItem(ListItem<TimesheetEntry> item)
            {
                TimesheetEntry entry = item.getModelObject();

                item.add(createProjectCodeTooltip(entry));
                item.add(new Label("hours", new Model<Float>(entry.getHours())));
            }
        };

        fragment.add(projects);
        return fragment;
    }

    /**
     * @param dayId
     * @return
     */
    private Label createEmptyDay(String dayId)
    {
        Label label = HtmlUtil.getNbspLabel(dayId);

        if (monthIsBeforeCurrent(overviewFor, thisMonth, thisYear))
        {
            label.add(new SimpleAttributeModifier("class", "noMonthBefore"));
        } else
        {
            label.add(new SimpleAttributeModifier("class", "noMonthAfter"));
        }
        return label;
    }

    private TooltipLabel createProjectCodeTooltip(TimesheetEntry entry)
    {
        StringBuilder tooltipText;

        String description = entry.getEntryId().getProjectAssignment().getProject().getDescription();

        if (!StringUtils.isBlank(description))
        {
            tooltipText = new StringBuilder(description);
        } else
        {
            tooltipText = new StringBuilder(CommonWebUtil.getResourceModelString(new ResourceModel("general.noDesc")));
        }

        tooltipText.append("<br />");

        tooltipText.append("<em>");
        tooltipText.append(entry.getEntryId().getProjectAssignment().getProject().getName());
        tooltipText.append("</em>");

        String projectCode = entry.getEntryId().getProjectAssignment().getProject().getProjectCode();

        return new TooltipLabel("projectCode", projectCode, entry.getEntryId().getProjectAssignment().getProject().getName());
    }

    /**
     * Add row with day numbers
     *
     * @param sb
     * @param calendar
     * @param thisMonth
     */
    private void addDayNumbersToWeek(WebMarkupContainer row)
    {
        for (int i = 1; i <= 7; i++, overviewFor.add(Calendar.DATE, 1))
        {
            Label dayLabel;
            String id = "day" + i;

            //
            if (overviewFor.get(Calendar.MONTH) == thisMonth)
            {
                dayLabel = new Label(id, Integer.toString(overviewFor.get(Calendar.DAY_OF_MONTH)));
            }
            // print space holders if not current month
            else
            {
                dayLabel = HtmlUtil.getNbspLabel(id);

                if (!monthIsBeforeCurrent(overviewFor, thisMonth, thisYear))
                {
                    dayLabel.add(new SimpleAttributeModifier("class", "noMonth"));
                }
            }

            row.add(dayLabel);
        }

        // reset the abused calendar
        overviewFor.add(Calendar.DATE, -7);
    }

    /**
     * @param calendar
     * @param thisMonth
     * @param thisYear
     * @return
     */
    private boolean monthIsBeforeCurrent(Calendar calendar, int thisMonth, int thisYear)
    {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        return month < thisMonth && year == thisYear ||
                year < thisYear;
    }

    /**
     * Add day labels
     *
     * @param parent
     */
    private void addDayLabels(WebMarkupContainer parent, EhourConfig config)
    {
        Calendar cal = new GregorianCalendar();
        cal.setFirstDayOfWeek(config.getFirstDayOfWeek());
        cal.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());

        for (int i = 1; i <= 7; i++, cal.add(Calendar.DAY_OF_WEEK, 1))
        {
            parent.add(new Label("day" + i, new DateModel(cal, config, DateModel.DATESTYLE_TIMESHEET_DAYONLY)));
        }
    }
}
