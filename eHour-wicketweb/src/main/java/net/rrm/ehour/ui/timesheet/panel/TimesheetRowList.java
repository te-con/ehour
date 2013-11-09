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
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.common.component.CommonModifiers;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.common.FormHighlighter;
import net.rrm.ehour.ui.timesheet.dto.GrandTotal;
import net.rrm.ehour.ui.timesheet.dto.ProjectTotalModel;
import net.rrm.ehour.ui.timesheet.dto.TimesheetRow;
import net.rrm.ehour.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.RangeValidator;

import java.util.Calendar;
import java.util.List;

/**
 * Representation of a timesheet row
 */

public class TimesheetRowList extends ListView<TimesheetRow> {
    private static final long serialVersionUID = -6905022018110510887L;

    private EhourConfig config;
    private final GrandTotal grandTotals;
    private Form<?> form;

    private static final Logger LOGGER = Logger.getLogger(TimesheetRowList.class);
    private MarkupContainer provider;

    public TimesheetRowList(String id, List<TimesheetRow> model, GrandTotal grandTotals, Form<?> form, MarkupContainer provider) {
        super(id, model);
        this.provider = provider;
        setReuseItems(true);
        this.grandTotals = grandTotals;
        this.form = form;

        config = EhourWebSession.getSession().getEhourConfig();
    }

    @Override
    protected void populateItem(ListItem<TimesheetRow> item) {
        final TimesheetRow row = item.getModelObject();
        ProjectAssignment assignment = row.getProjectAssignment();

        item.add(createBookWholeWeekLink(row));
        item.add(new Label("project", assignment.getProject().getName()));
        Label role = new Label("role", String.format("(%s)", assignment.getRole()));
        role.setVisible(StringUtils.isNotBlank(assignment.getRole()));
        item.add(role);
        item.add(new Label("projectCode", assignment.getProject().getProjectCode()));
        item.add(createStatusLabel(item));
        addInputCells(item, row);
        item.add(createTotalHoursLabel(row));
    }

    private Label createTotalHoursLabel(final TimesheetRow row) {
        Label totalHours = new Label("total", new ProjectTotalModel(row));
        totalHours.setOutputMarkupId(true);
        return totalHours;
    }

    private void addInputCells(ListItem<TimesheetRow> item, final TimesheetRow row) {
        Calendar currentDate = (Calendar) row.getFirstDayOfWeekDate().clone();

        DateRange range = new DateRange(row.getProjectAssignment().getDateStart(),
                row.getProjectAssignment().getDateEnd());

        // now add every cell
        for (int i = 1;
             i <= 7;
             i++, currentDate.add(Calendar.DAY_OF_YEAR, 1)) {
            String id = "day" + i;

            if (DateUtil.isDateWithinRange(currentDate, range)) {
                createTimesheetEntryItems(id, row, currentDate.get(Calendar.DAY_OF_WEEK) - 1, item);
            } else {
                createEmptyTimesheetEntry(id, item);
            }
        }
    }

    private AjaxLink<Void> createBookWholeWeekLink(final TimesheetRow row) {
        AjaxLink<Void> projectLink = new AjaxLink<Void>("bookWholeWeek") {
            private static final long serialVersionUID = -663239917205218384L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                row.bookRemainingHoursOnRow();
                target.add(form);
            }
        };

        ContextImage img = new ContextImage("bookImg", new Model<String>("img/check_all_off.png"));
        projectLink.add(img);

        return projectLink;
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

    private void createEmptyTimesheetEntry(String id, ListItem<TimesheetRow> item) {
        Fragment fragment = new Fragment(id, "dayInputHidden", provider);

        item.add(fragment);
    }

    private void createTimesheetEntryItems(String id, TimesheetRow row, final int index, ListItem<TimesheetRow> item) {
        Fragment fragment = new Fragment(id, "dayInput", provider);

        item.add(fragment);

        fragment.add(createTextFieldWithValidation(row, index));

        createTimesheetEntryComment(row, index, fragment);
    }

    private TimesheetTextField createTextFieldWithValidation(TimesheetRow row, final int index) {
        PropertyModel<Float> cellModel = new PropertyModel<Float>(row, "timesheetCells[" + index + "].timesheetEntry.hours");

        // make sure it's added to the grandtotal
        grandTotals.addValue(index, cellModel);

        // list it on the page
        final TimesheetTextField dayInput = new TimesheetTextField("day", cellModel, 1);
        dayInput.add(RangeValidator.minimum(0f));
        dayInput.setOutputMarkupId(true);

        // make sure values are checked
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
                LOGGER.debug(target.getLastFocusedElementId() + " onblur error!", e);
                form.visitFormComponents(new FormHighlighter(target));
            }
        };

        dayInput.add(behavior);

        return dayInput;
    }

    @SuppressWarnings("serial")
    private void createTimesheetEntryComment(final TimesheetRow row, final int index, WebMarkupContainer parent) {
        final ModalWindow modalWindow;

        final PropertyModel<String> commentModel = new PropertyModel<String>(row, "timesheetCells[" + index + "].timesheetEntry.comment");

        modalWindow = new ModalWindow("dayWin");
        modalWindow.setResizable(false);
        modalWindow.setInitialWidth(400);
        modalWindow.setInitialHeight(225);

        modalWindow.setTitle(new StringResourceModel("timesheet.dayCommentsTitle", this, null));
        modalWindow.setContent(new TimesheetEntryCommentPanel(modalWindow.getContentId(),
                commentModel, row, index, modalWindow));

        final AjaxLink<Void> commentLink = new AjaxLink<Void>("dayLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                modalWindow.show(target);
            }

            @Override
            protected void onBeforeRender() {
                ContextImage img = createContextImage(commentModel);

                addOrReplace(img);
                super.onBeforeRender();
            }
        };

        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            public void onClose(AjaxRequestTarget target) {
                setCommentLinkClass(commentModel, commentLink);

                target.add(commentLink);
            }
        });

        parent.add(modalWindow);

        commentLink.setOutputMarkupId(true);
        commentLink.add(CommonModifiers.tabIndexModifier(255));

        parent.add(commentLink);
    }

    private ContextImage createContextImage(PropertyModel<String> commentModel) {
        ContextImage img;

        if (StringUtils.isBlank(commentModel.getObject())) {
            img = new ContextImage("commentLinkImg", new Model<String>("img/comment/comment_blue_off.gif"));
        } else {
            img = new ContextImage("commentLinkImg", new Model<String>("img/comment/comment_blue_on.gif"));
        }
        return img;
    }

    private void setCommentLinkClass(IModel<String> commentModel, AjaxLink<Void> commentLink) {
        commentLink.add(AttributeModifier.replace("class", StringUtils.isBlank(commentModel.getObject()) ? "timesheetEntryComment" : "timesheetEntryCommented"));
    }

    class TimesheetEntryCommentPanel extends AbstractBasePanel<String> {
        private static final long serialVersionUID = 1L;
        private final TimesheetRow row;
        private final int index;
        private final ModalWindow window;

        public TimesheetEntryCommentPanel(String id, final IModel<String> model, TimesheetRow row, int index, final ModalWindow window) {
            super(id, model);

            this.row = row;
            this.index = index;
            this.window = window;
        }

        @Override
        protected void onBeforeRender() {
            super.onBeforeRender();


            Calendar thisDate = (Calendar) row.getFirstDayOfWeekDate().clone();
            // Use the render order, not the index order, when calculating the date
            thisDate.add(Calendar.DAY_OF_YEAR, grandTotals.getOrderForIndex(index) - 1);

            final String previousModel = getPanelModelObject();
            addOrReplace(new Label("dayComments",
                    new StringResourceModel("timesheet.dayComments",
                            this,
                            null,
                            new Object[]{row.getProjectAssignment().getFullName(),
                                    new DateModel(thisDate, config, DateModel.DATESTYLE_DAYONLY_LONG)})));

            final KeepAliveTextArea textArea = new KeepAliveTextArea("comment", getPanelModel());
            textArea.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    // simple hack to get around IE's prob with nested forms in a modalwindow
                }
            });
            textArea.setOutputMarkupId(true);

            addOrReplace(textArea);

            AjaxLink<Void> submitButton = new AjaxLink<Void>("submit") {
                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    window.close(target);
                }
            };
            addOrReplace(submitButton);

            AbstractLink cancelButton = new AjaxLink<Void>("cancel") {
                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    TimesheetEntryCommentPanel.this.getPanelModel().setObject(previousModel);
                    window.close(target);
                }
            };
            addOrReplace(cancelButton);
        }
    }
}