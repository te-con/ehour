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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.List;

/**
 * Representation of a timesheet row
 */

public class TimesheetRowList extends ListView<TimesheetRow> {
    private static final long serialVersionUID = -6905022018110510887L;

    private final GrandTotal grandTotals;
    private Form<?> form;

    private final TimesheetContainer timesheetContainer;
    private MarkupContainer provider;

    @SpringBean
    private TimesheetIconRenderFactoryCollection iconRenderer;

    public TimesheetRowList(String id, List<TimesheetRow> model, GrandTotal grandTotals, TimesheetContainer timesheetContainer, Form<?> form, MarkupContainer provider) {
        super(id, model);
        this.timesheetContainer = timesheetContainer;
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
        link.setVisible(EhourWebApplication.get().isBookWholeWeekEnabled());
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
        Label totalHours = new Label("total", new ProjectTotalModel(row));
        totalHours.setOutputMarkupId(true);
        return totalHours;
    }

    private void addInputCells(ListItem<TimesheetRow> item, final TimesheetRow row) {
        Calendar currentDate = (Calendar) row.getFirstDayOfWeekDate().clone();

        ProjectAssignment assignment = row.getProjectAssignment();
        DateRange range = new DateRange(assignment.getDateStart(), assignment.getDateEnd());

        // now add every cell
        for (int i = 1;
             i <= 7;
             i++, currentDate.add(Calendar.DAY_OF_YEAR, 1)) {
            String id = "day" + i;

            int index = currentDate.get(Calendar.DAY_OF_WEEK) - 1;
            TimesheetCell timesheetCell = row.getTimesheetCells()[index];

            if (DateUtil.isDateWithinRange(currentDate, range)) {
                item.add(timesheetCell.isLocked() ? createLockedTimesheetEntry(id, row, index) : createInputTimesheetEntry(id, row, index));
            } else {
                item.add(timesheetCell.isWithHours() ? createLockedTimesheetEntry(id, row, index) : createEmptyTimesheetEntry(id) );
            }
        }
    }

    private Fragment createLockedTimesheetEntry(String id, TimesheetRow row, int index) {
        Fragment fragment = new Fragment(id, "dayLocked", provider);

        TimesheetCell timesheetCell = row.getTimesheetCells()[index];
        PropertyModel<Float> cellModel = new PropertyModel<>(timesheetCell, "timesheetEntry.hours");

        String css = timesheetCell.getTimesheetEntry() != null && StringUtils.isNotBlank(timesheetCell.getTimesheetEntry().getComment()) ? "lockedday" : "lockeddaynocomment";
        fragment.add(AttributeModifier.append("class", css));

        // make sure it's added to the grandtotal
        grandTotals.addValue(index, cellModel);

        fragment.add(new Label("day", cellModel));

        fragment.add(renderOptions(timesheetCell, DayStatus.LOCKED));

        return fragment;
    }

    private RepeatingView renderOptions(TimesheetCell timesheetCell, DayStatus status) {
        RepeatingView options = new RepeatingView("options");

        for (TimesheetIconRenderFactory renderFactory : iconRenderer.getRenderFactories()) {
            options.add(renderFactory.renderForId(options.newChildId(), timesheetCell, status, timesheetContainer));
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
        fragment.add(renderOptions(timesheetCell, DayStatus.OPEN));

        return fragment;
    }

    private TimesheetTextField createTextFieldWithValidation(TimesheetCell timesheetCell, final int index) {
        PropertyModel<Float> cellModel = new PropertyModel<>(timesheetCell, "timesheetEntry.hours");

        // make sure it's added to the grandtotal
        grandTotals.addValue(index, cellModel);

        // add inputfield with validation to the parent
        final TimesheetTextField dayInput = new TimesheetTextField("day", cellModel, 1);
        dayInput.setOutputMarkupId(true);

        // add validation
        AjaxFormComponentUpdatingBehavior behavior = new AjaxFormComponentUpdatingBehavior("onblur") {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // update the project total
                target.add(dayInput.getParent().getParent().get("total"));

                // update the grand total & day total
                Component parent = dayInput.findParent(Form.class).get("blueFrame").get("blueFrame_body");

                target.add(parent.get("grandTotal"));
                target.add(parent.get("day" + grandTotals.getOrderForIndex(index) + "Total"));

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

    public static enum DayStatus {
        OPEN,
        LOCKED
    }
}