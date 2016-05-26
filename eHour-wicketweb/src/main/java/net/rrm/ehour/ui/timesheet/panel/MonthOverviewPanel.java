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

package net.rrm.ehour.ui.timesheet.panel;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.sort.TimesheetEntryComparator;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Month overview panel for consultants
 */

public class MonthOverviewPanel extends Panel {
    private static final long serialVersionUID = -8977205040520638758L;

    private final TimesheetOverview timesheetOverview;
    private final int thisMonth;
    private final int thisYear;
    private final Calendar overviewFor;
    private static final TimesheetEntryComparator comparator = new TimesheetEntryComparator();

    public MonthOverviewPanel(String id, TimesheetOverview timesheetOverview, final Calendar overviewForMonth) {
        super(id);

        setOutputMarkupId(true);

        EhourConfig config = EhourWebSession.getEhourConfig();

        this.timesheetOverview = timesheetOverview;
        thisMonth = overviewForMonth.get(Calendar.MONTH);
        thisYear = overviewForMonth.get(Calendar.YEAR);

        this.overviewFor = (Calendar) overviewForMonth.clone();
        DateUtil.dayOfWeekFix(overviewFor);
        overviewFor.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("greyFrame",
                new ResourceModel("monthoverview.overview"));
        GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");

        greyBorder.add(blueBorder);
        add(greyBorder);

        addDayLabels(blueBorder, config);

        createMonthCalendar(blueBorder);
    }

    private void createMonthCalendar(WebMarkupContainer parent) {
        RepeatingView calendarView = new RepeatingView("calendarView");

        while ((overviewFor.get(Calendar.YEAR) == thisYear) &&
                (overviewFor.get(Calendar.MONTH) <= thisMonth) || overviewFor.get(Calendar.YEAR) < thisYear) {
            WebMarkupContainer row = new WebMarkupContainer(calendarView.newChildId());
            calendarView.add(row);

            createWeek(row);
        }

        parent.add(calendarView);
    }

    private void createWeek(WebMarkupContainer row) {
        row.add(new Label("weekNumber", Integer.toString(overviewFor.get(Calendar.WEEK_OF_YEAR))));

        addDayNumbersToWeek(row);
        addDayValuesToWeek(row);
    }

    private void addDayValuesToWeek(WebMarkupContainer row) {
        for (int i = 1; i <= 7; i++, overviewFor.add(Calendar.DATE, 1)) {
            String dayId = "day" + i + "Value";

            if (overviewFor.get(Calendar.MONTH) == thisMonth) {
                row.add(createDay(dayId));
            } else {
                row.add(createEmptyDay(dayId));
            }
        }
    }

    private Fragment createDay(String dayId) {
        Fragment fragment;

        List<TimesheetEntry> timesheetEntries = null;

        if (timesheetOverview.getTimesheetEntries() != null) {
            timesheetEntries = timesheetOverview.getTimesheetEntries().get(overviewFor.get(Calendar.DAY_OF_MONTH));
        }

        if (timesheetEntries != null && !timesheetEntries.isEmpty()) {
            fragment = createDayContents(dayId, timesheetEntries);
        } else {
            fragment = new Fragment(dayId, "noProjects", this);
        }

        if (DateUtil.isWeekend(overviewFor)) {
            fragment.add(AttributeModifier.replace("style", "background-color: #eef6fe"));
        }

        return fragment;
    }

    @SuppressWarnings("serial")
    private Fragment createDayContents(String dayId, List<TimesheetEntry> timesheetEntries) {
        Fragment fragment;
        fragment = new Fragment(dayId, "showProjects", this);

        //sort by Project Code
        if(timesheetEntries != null)
            Collections.sort(timesheetEntries, comparator);

        ListView<TimesheetEntry> projects = new ListView<TimesheetEntry>("projects", timesheetEntries) {
            @Override
            protected void populateItem(ListItem<TimesheetEntry> item) {
                TimesheetEntry entry = item.getModelObject();

                Project project = entry.getEntryId().getProjectAssignment().getProject();
                Label projectCodeLabel = new Label("projectCode", project.getProjectCode());
                projectCodeLabel.setMarkupId(String.format("prjV%d", project.getProjectId()));
                projectCodeLabel.setOutputMarkupId(true);

                item.add(projectCodeLabel);
                item.add(new Label("hours", new Model<>(entry.getHours())));
            }
        };

        fragment.add(projects);
        return fragment;
    }

    private Label createEmptyDay(String dayId) {
        Label label = HtmlUtil.getNbspLabel(dayId);

        if (monthIsBeforeCurrent(overviewFor, thisMonth, thisYear)) {
            label.add(AttributeModifier.replace("class", "noMonthBefore"));
        } else {
            label.add(AttributeModifier.replace("class", "noMonthAfter"));
        }
        return label;
    }

    private void addDayNumbersToWeek(WebMarkupContainer row) {
        for (int i = 1; i <= 7; i++, overviewFor.add(Calendar.DATE, 1)) {
            Label dayLabel;
            String id = "day" + i;

            //
            if (overviewFor.get(Calendar.MONTH) == thisMonth) {
                dayLabel = new Label(id, Integer.toString(overviewFor.get(Calendar.DAY_OF_MONTH)));
            }
            // print space holders if not current month
            else {
                dayLabel = HtmlUtil.getNbspLabel(id);

                if (!monthIsBeforeCurrent(overviewFor, thisMonth, thisYear)) {
                    dayLabel.add(AttributeModifier.replace("class", "noMonth"));
                }
            }

            row.add(dayLabel);
        }

        // reset the abused calendar
        overviewFor.add(Calendar.DATE, -7);
    }

    private boolean monthIsBeforeCurrent(Calendar calendar, int thisMonth, int thisYear) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        return month < thisMonth && year == thisYear ||
                year < thisYear;
    }

    private void addDayLabels(WebMarkupContainer parent, EhourConfig config) {
        Calendar cal = new GregorianCalendar();
        cal.setFirstDayOfWeek(config.getFirstDayOfWeek());
        cal.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());

        for (int dayNumber = 1; dayNumber <= 7; dayNumber++, cal.add(Calendar.DAY_OF_WEEK, 1)) {
            parent.add(new Label("day" + dayNumber, new DateModel(cal, config, DateModel.DATESTYLE_TIMESHEET_DAYONLY)));
        }
    }
}
