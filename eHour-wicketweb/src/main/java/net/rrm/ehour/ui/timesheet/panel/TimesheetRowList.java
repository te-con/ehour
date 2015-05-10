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

import com.google.common.collect.Lists;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.common.form.FormHighlighter;
import net.rrm.ehour.ui.timesheet.dto.GrandTotal;
import net.rrm.ehour.ui.timesheet.dto.ProjectTotalModel;
import net.rrm.ehour.ui.timesheet.dto.TimesheetCell;
import net.rrm.ehour.ui.timesheet.dto.TimesheetRow;
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import net.rrm.ehour.ui.timesheet.panel.renderer.TimesheetIconRenderFactory;
import net.rrm.ehour.ui.timesheet.panel.renderer.TimesheetIconRenderFactoryCollection;
import net.rrm.ehour.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.List;

/**
 * Representation of a timesheet row
 */

public class TimesheetRowList extends ListView<TimesheetRow> {
    private static final long serialVersionUID = -6905022018110510887L;
    private static final String DAY_ID = "day";
    private static final String DAY_OPTIONS_ID = "options";

    private final GrandTotal grandTotals;
    private Form<?> form;

    private final IModel<TimesheetContainer> timesheetContainerModel;
    private MarkupContainer provider;

    @SpringBean
    private TimesheetIconRenderFactoryCollection iconRenderer;

    public TimesheetRowList(String id, List<TimesheetRow> model, GrandTotal grandTotals, IModel<TimesheetContainer> timesheetContainerModel, Form<?> form, MarkupContainer provider) {
        super(id, model);
        this.timesheetContainerModel = timesheetContainerModel;
        this.provider = provider;
        setReuseItems(true);
        this.grandTotals = grandTotals;
        this.form = form;
    }

    @Override
    protected void populateItem(ListItem<TimesheetRow> item) {
        final TimesheetRow row = item.getModelObject();
        ProjectAssignment assignment = row.getProjectAssignment();

        item.add(createBookWholeWeekLink(row, "bookWholeWeek"));
        item.add(new Label("project", assignment.getProject().getName()));
        Label role = new Label("role", String.format("(%s)", assignment.getRole()));
        role.setVisible(StringUtils.isNotBlank(assignment.getRole()));
        item.add(role);
        item.add(new Label("projectCode", assignment.getProject().getProjectCode()));
        item.add(createStatusLabel(item));
        addInputCells(item, row);
        item.add(createTotalHoursLabel(row));
    }

    private Component createBookWholeWeekLink(final TimesheetRow row, final String bookWholeWeek) {
        final AjaxLink<Void> link = new AjaxLink<Void>(bookWholeWeek) {
            private static final long serialVersionUID = -663239917205218384L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                row.bookRemainingHoursOnRow();
                target.add(form);
            }
        };
        link.setVisible(EhourWebApplication.get().isBookWholeWeekEnabled() && !row.getTimesheet().isAnyLocked());
        return link;
    }

    private Label createStatusLabel(ListItem<TimesheetRow> item) {
        Label label = new Label("status", new PropertyModel<String>(item.getModel(), "status")) {
            @Override
            public boolean isVisible() {
                return StringUtils.isNotBlank(getDefaultModelObjectAsString());
            }
        };

        label.setEscapeModelStrings(false);
        label.setOutputMarkupId(true);
        label.setOutputMarkupPlaceholderTag(true);
        return label;
    }


    private Label createTotalHoursLabel(final TimesheetRow row) {
        final Label totalHours = new Label("total", new ProjectTotalModel(row)) {
            @Override
            public void onEvent(IEvent<?> event) {
                if (event.getPayload() instanceof TimesheetInputModifiedEvent) {
                    TimesheetInputModifiedEvent payload = (TimesheetInputModifiedEvent) event.getPayload();

                    if (payload.getForAssignment().equals(row.getProjectAssignment())) {
                        payload.getTarget().add(this);
                    }
                }
            }
        };

        totalHours.setOutputMarkupId(true);
        return totalHours;
    }

    private void addInputCells(ListItem<TimesheetRow> item, final TimesheetRow row) {
        Calendar currentDate = (Calendar) row.getFirstDayOfWeekDate().clone();

        ProjectAssignment assignment = row.getProjectAssignment();
        final DateRange range = new DateRange(assignment.getDateStart(), assignment.getDateEnd());

        final List<Calendar> dates = Lists.newArrayList();

        for (int i = 1;
             i <= 7;
             i++, currentDate.add(Calendar.DAY_OF_YEAR, 1)) {
            dates.add((Calendar) currentDate.clone());
        }

        item.add(new ListView<Calendar>("days", dates) {
            @Override
            protected void populateItem(ListItem<Calendar> item) {
                Calendar currentDate = item.getModelObject();

                int index = currentDate.get(Calendar.DAY_OF_WEEK) - 1;

                TimesheetCell timesheetCell = row.getTimesheetCells()[index];

                DayStatus dayStatus = determineDayStatus(timesheetCell, currentDate, range);

                Component day;

                switch (dayStatus) {
                    case HIDDEN:
                    default:
                        day = createEmptyTimesheetEntry(DAY_ID);
                        item.add(new WebMarkupContainer(DAY_OPTIONS_ID));
                        break;
                    case OPEN:
                        day = createInputTimesheetEntry(DAY_ID, row, index);
                        item.add(renderOptions(DAY_OPTIONS_ID, timesheetCell, dayStatus));
                        break;
                    case LOCKED:
                        day = createLockedTimesheetEntry(DAY_ID, row, index);
                        item.add(renderOptions(DAY_OPTIONS_ID, timesheetCell, dayStatus));
                        item.add(AttributeModifier.append("class", "locked"));
                        break;
                }

                String cssClass = "weekday";

                if (dayStatus == DayStatus.LOCKED) {
                    cssClass += " lockedCell";
                } else if (item.getIndex() == 0) {
                    cssClass = "sunday";
                } else if (item.getIndex() == 6) {
                    cssClass = "saturday";
                }

                day.add(AttributeModifier.replace("class", cssClass));
                item.add(day);
            }
        });
    }

    private DayStatus determineDayStatus(TimesheetCell timesheetCell, Calendar currentDate, DateRange range) {
        if (DateUtil.isDateWithinRange(currentDate, range)) {
            return timesheetCell.isLocked() ? DayStatus.LOCKED : DayStatus.OPEN;
        } else {
            return timesheetCell.isWithHours() ? DayStatus.LOCKED : DayStatus.HIDDEN;
        }
    }

    private Fragment createLockedTimesheetEntry(String id, TimesheetRow row, int index) {
        Fragment fragment = new Fragment(id, "dayLocked", provider);

        TimesheetCell timesheetCell = row.getTimesheetCells()[index];
        PropertyModel<Float> cellModel = new PropertyModel<>(timesheetCell, "timesheetEntry.hours");

        String css = timesheetCell.getTimesheetEntry() != null && StringUtils.isNotBlank(timesheetCell.getTimesheetEntry().getComment()) ? "lockedday" : "lockeddaynocomment";
        fragment.add(AttributeModifier.append("class", css));

        // make sure it's added to the grandtotal
        grandTotals.addValue(index, timesheetCell, cellModel);

        fragment.add(new Label(DAY_ID, cellModel));

        return fragment;
    }

    private RepeatingView renderOptions(String id, TimesheetCell timesheetCell, DayStatus status) {
        RepeatingView options = new RepeatingView(id);

        for (TimesheetIconRenderFactory renderFactory : iconRenderer.getRenderFactories()) {
            options.add(renderFactory.renderForId(options.newChildId(), timesheetCell, status, timesheetContainerModel));
        }

        return options;
    }

    private Fragment createEmptyTimesheetEntry(String id) {
        return new Fragment(id, "dayInputHidden", provider);
    }

    private Fragment createInputTimesheetEntry(String id, TimesheetRow row, final int index) {
        TimesheetCell timesheetCell = row.getTimesheetCells()[index];

        Fragment fragment = new Fragment(id, "dayInput", provider);
        fragment.add(createTextFieldWithValidation(timesheetCell, index));

        return fragment;
    }

    private TimesheetTextField createTextFieldWithValidation(final TimesheetCell timesheetCell, final int index) {
        PropertyModel<Float> cellModel = new PropertyModel<>(timesheetCell, "timesheetEntry.hours");

        // make sure it's added to the grandtotal
        grandTotals.addValue(index, timesheetCell, cellModel);

        // add inputfield with validation to the parent
        final TimesheetTextField dayInput = new TimesheetTextField(DAY_ID, cellModel, 1);
        dayInput.setOutputMarkupId(true);

        // add validation
        AjaxFormComponentUpdatingBehavior behavior = new AjaxFormComponentUpdatingBehavior("onblur") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                Form sink = dayInput.findParent(Form.class);
                send(sink, Broadcast.DEPTH, new TimesheetInputModifiedEvent(target, index, timesheetCell.getProjectAssignment()));
                form.visitFormComponents(new FormHighlighter(target));
            }

            @Override
            protected void onError(final AjaxRequestTarget target, RuntimeException e) {
                form.visitFormComponents(new FormHighlighter(target));
            }
        };

        dayInput.add(behavior);

        return dayInput;
    }

    public enum DayStatus {
        OPEN,
        LOCKED,
        HIDDEN
    }
}
