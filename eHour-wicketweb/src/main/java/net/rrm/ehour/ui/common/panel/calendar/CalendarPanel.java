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

package net.rrm.ehour.ui.common.panel.calendar;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.common.component.DisablingAjaxLink;
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.formguard.GuardDirtyFormUtil;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.sidepanel.SidePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.util.DateUtil;
import org.apache.log4j.Logger;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Calendar.*;

/**
 * Navigation Calendar
 */

public class CalendarPanel extends SidePanel {
    private static final long serialVersionUID = -7777893083323915299L;

    private final static Logger LOGGER = Logger.getLogger(CalendarPanel.class);

    private WebMarkupContainer calendarFrame;
    private User user;
    private boolean fireWeekClicks;
    private DateRange highlightWeekStartingAt;
    private EhourConfig config;

    private CalendarWeekFactory calendarWeekFactory;

    public CalendarPanel(String id, User user) {
        this(id, user, true);
    }

    public CalendarPanel(String id, User user, boolean allowWeekClicks) {
        super(id);

        calendarWeekFactory = new CalendarWeekFactory();

        setOutputMarkupId(true);

        config = EhourWebSession.getSession().getEhourConfig();

        this.user = user;
        fireWeekClicks = allowWeekClicks;

        calendarFrame = getFrame();
        add(calendarFrame);

        buildCalendar(calendarFrame);
    }

    private void buildCalendar(WebMarkupContainer parent) {
        // first get the data
        Calendar month = EhourWebSession.getSession().getNavCalendar();

        List<CalendarWeek> weeks = calendarWeekFactory.createWeeks(config.getFirstDayOfWeek(), user.getUserId(), ((GregorianCalendar) month.clone()));

        // set month label
        parent.add(new Label("currentMonth", new DateModel(month, ((EhourWebSession) getSession()).getEhourConfig(), DateModel.DATESTYLE_MONTHONLY)));

        // previous & next month links
        parent.add(createPreviousMonthLink("previousMonthLink"));

        parent.add(createNextMonthLink("nextMonthLink"));

        // content
        addCalendarWeeks(parent, weeks);
    }

    public void refreshCalendar(AjaxRequestTarget target) {
        WebMarkupContainer replacementFrame = getFrame();
        buildCalendar(replacementFrame);

        calendarFrame.replaceWith(replacementFrame);
        calendarFrame = replacementFrame;
        target.addComponent(replacementFrame);
    }

    private WebMarkupContainer getFrame() {
        WebMarkupContainer calendarFrame = new WebMarkupContainer("calendarFrame");
        calendarFrame.setOutputMarkupId(true);

        return calendarFrame;
    }

    private AjaxLink<Void> createNextMonthLink(String id) {
        AjaxLink<Void> nextMonthLink = new ChangeMonthLink(id, 1);
        nextMonthLink.add(new Image("nextMonthImg", new ResourceReference(CalendarPanel.class, "arrow_right.gif")));
        return nextMonthLink;
    }

    private AjaxLink<Void> createPreviousMonthLink(String id) {
        AjaxLink<Void> previousMonthLink = new ChangeMonthLink(id, -1);
        previousMonthLink.add(new Image("previousMonthImg", new ResourceReference(CalendarPanel.class, "arrow_left.gif")));
        return previousMonthLink;
    }

    @SuppressWarnings("serial")
    private void addCalendarWeeks(WebMarkupContainer container, List<CalendarWeek> weeks) {
        ListView<CalendarWeek> view = new ListView<CalendarWeek>("weeks", weeks) {
            public void populateItem(final ListItem<CalendarWeek> item) {
                CalendarWeek week = item.getModelObject();
                Calendar renderDate = (Calendar) week.getWeekStart().clone();

                for (int dayInWeek = 1; dayInWeek <= 7; dayInWeek++) {
                    boolean weekend = DateUtil.isWeekend(renderDate);

                    int currentDay = renderDate.get(DAY_OF_WEEK);
                    CalendarDay day = week.getDay(currentDay);

                    Label label = createLabel(dayInWeek, week, day, weekend);
                    item.add(label);

                    renderDate.add(Calendar.DATE, 1);
                }

                item.setOutputMarkupId(true);

                if (fireWeekClicks) {
                    fireWeekClicks(item, week);
                } else {
                    item.add(new SimpleAttributeModifier("style", "cursor:default"));
                }
            }

            private void fireWeekClicks(final ListItem<CalendarWeek> item, CalendarWeek week) {
                if (highlightWeekStartingAt == null ||
                        !DateUtil.isDateWithinRange(week.getWeekStart().getTime(), highlightWeekStartingAt)) {
                    item.add(new WeekClick("onclick", week.getWeek(), week.getYear()));
                    item.add(new SimpleAttributeModifier("onmouseover", "backgroundOn(this)"));
                    item.add(new SimpleAttributeModifier("onmouseout", "backgroundOff(this)"));
                }
            }

            private Label createLabel(int dayOfWeek, CalendarWeek week, CalendarDay day, boolean weekend) {
                Label label;
                String id = "day" + dayOfWeek;

                // when day is null the date is in the next/previous month
                if (day == null) {
                    label = HtmlUtil.getNbspLabel(id);
                } else {
                    label = new Label(id, new PropertyModel<Integer>(day, "monthDay"));
                }

                // determine css class
                StringBuilder cssClass = new StringBuilder(weekend ? "WeekendDay" : "WeekDay");

                // booked days are bold
                if (day != null && day.isBooked()) {
                    cssClass.append(" Booked");
                }

                // first day doesn't have margin-left
                if (dayOfWeek == 1) {
                    cssClass.append(" FirstDay");

                    if (week.getLocation() == ElementLocation.FIRST) {
                        cssClass.append(" FirstDayOfFirstRow");
                    } else if (week.getLocation() == ElementLocation.LAST) {
                        cssClass.append(" FirstDayOfLastRow");
                    }
                }

                if (dayOfWeek == 7) {
                    if (week.getLocation() == ElementLocation.FIRST) {
                        cssClass.append(" LastDayOfFirstRow");
                    } else if (week.getLocation() == ElementLocation.LAST) {
                        cssClass.append(" LastDayOfLastRow");
                    }
                }



                label.add(new SimpleAttributeModifier("class", cssClass.toString()));

                // determine custom css properties
                StringBuilder style = new StringBuilder();

                // selected weeks have a more dark background
                if (highlightWeekStartingAt != null &&
                        DateUtil.isDateWithinRange(week.getWeekStart().getTime(), highlightWeekStartingAt)) {
                    style.append("background-color: #edf5fe;");
                }

                if (style.length() > 0) {
                    label.add(new SimpleAttributeModifier("style", style.toString()));
                }

                return label;
            }
        };

        container.add(view);
    }

    /**
     * Changes the month
     *
     * @author Thies
     */
    private class ChangeMonthLink extends DisablingAjaxLink {
        private static final long serialVersionUID = 1L;
        private int monthChange;

        public ChangeMonthLink(String id, int monthChange) {
            super(id);

            this.monthChange = monthChange;
        }

        @Override
        public void onClick(AjaxRequestTarget target) {
            EhourWebSession session = EhourWebSession.getSession();
            Calendar month = session.getNavCalendar();
            month.add(Calendar.MONTH, monthChange);
            month.set(DAY_OF_MONTH, 1);
            session.setNavCalendar(month);

            // do it before it gets replaced, otherwise getPage is null due to new instantiation of links
            EventPublisher.publishAjaxEvent(ChangeMonthLink.this, new AjaxEvent(CalendarAjaxEventType.MONTH_CHANGE));

            refreshCalendar(target);

            this.setEnabled(false);
        }

        @Override
        protected IAjaxCallDecorator getAjaxCallDecorator() {
            return new LoadingSpinnerDecorator();
        }
    }

    private class WeekClick extends AjaxEventBehavior {
        private static final long serialVersionUID = 9164386260367481606L;

        private int week, year;

        public WeekClick(String id, int week, int year) {
            super(id);
            this.week = week;
            this.year = year;
        }

        @Override
        protected void onEvent(AjaxRequestTarget target) {
            EhourWebSession session = EhourWebSession.getSession();
            Calendar cal = DateUtil.getCalendar(session.getEhourConfig());

            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.WEEK_OF_YEAR, week);
            DateUtil.dayOfWeekFix(cal);
            cal.set(DAY_OF_WEEK, config.getFirstDayOfWeek());

            session.setNavCalendar(cal);

            EventPublisher.publishAjaxEvent(CalendarPanel.this, new PayloadAjaxEvent<Calendar>(CalendarAjaxEventType.WEEK_CLICK, cal));
        }

        @Override
        protected CharSequence getEventHandler() {
            CharSequence handler = super.getEventHandler();
            return GuardDirtyFormUtil.getEventHandler(handler);
        }

        @Override
        protected IAjaxCallDecorator getAjaxCallDecorator() {
            return new LoadingSpinnerDecorator();
        }
    }

    public void setHighlightWeekStartingAt(DateRange highlightWeekStartingAt) {
        this.highlightWeekStartingAt = highlightWeekStartingAt;
    }
}
