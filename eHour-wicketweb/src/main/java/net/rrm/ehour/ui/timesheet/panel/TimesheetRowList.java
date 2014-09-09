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
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.common.component.CommonModifiers;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.common.FormHighlighter;
import net.rrm.ehour.ui.timesheet.dto.GrandTotal;
import net.rrm.ehour.ui.timesheet.dto.ProjectTotalModel;
import net.rrm.ehour.ui.timesheet.dto.TimesheetCell;
import net.rrm.ehour.ui.timesheet.dto.TimesheetRow;
import net.rrm.ehour.util.DateUtil;
import org.apache.commons.lang.StringUtils;
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

    public TimesheetRowList(String id, final List<TimesheetRow> model, GrandTotal grandTotals, Form<?> form)
    {
        super(id, model);
        setReuseItems(true);
        this.grandTotals = grandTotals;
        this.form = form;

        config = EhourWebSession.getEhourConfig();
    }


    /*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
	 */
	@Override
	protected void populateItem(ListItem<TimesheetRow> item)
	{
		final TimesheetRow row = (TimesheetRow) item.getModelObject();

		item.add(createBookWholeWeekLink(row));
		item.add(new Label("activityCode", row.getActivity().getName()));
        item.add(new Label("availableHours", new PropertyModel<Float>(row.getActivity(), "availableHours")));
		item.add(createStatusLabel(item));
		addInputCells(item, row);
		item.add(createTotalHoursLabel(row));
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

        DateRange range = new DateRange(row.getActivity().getDateStart(),
                row.getActivity().getDateEnd());

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
                item.add(createEmptyTimesheetEntry(id));
            }
        }
    }

    private Fragment createAndAddFragment(String id, String fragmentId) {
        return new Fragment(id, fragmentId, provider);
    }

    private Fragment createLockedTimesheetEntry(String id, TimesheetRow row, int index) {
        Fragment fragment = createAndAddFragment(id, "dayLocked");

        TimesheetCell timesheetCell = row.getTimesheetCells()[index];
        PropertyModel<Float> cellModel = new PropertyModel<Float>(timesheetCell, "timesheetEntry.hours");

        String css = timesheetCell.getTimesheetEntry() != null && StringUtils.isNotBlank(timesheetCell.getTimesheetEntry().getComment()) ? "lockedday" : "lockeddaynocomment";
        fragment.add(AttributeModifier.append("class", css));

        // make sure it's added to the grandtotal
        grandTotals.addValue(index, cellModel);

        fragment.add(new Label("day", cellModel));

        createTimesheetEntryComment(row, index, fragment, DayStatus.LOCKED);

        return fragment;
    }

    private Fragment createEmptyTimesheetEntry(String id) {
        return createAndAddFragment(id, "dayInputHidden");
    }

    private Fragment createInputTimesheetEntry(String id, TimesheetRow row, final int index) {
        Fragment fragment = createAndAddFragment(id, "dayInput");
        fragment.add(createTextFieldWithValidation(row, index));

        createTimesheetEntryComment(row, index, fragment, DayStatus.OPEN);

        return fragment;
    }

    private TimesheetTextField createTextFieldWithValidation(TimesheetRow row, final int index) {
        TimesheetCell timesheetCell = row.getTimesheetCells()[index];
        PropertyModel<Float> cellModel = new PropertyModel<Float>(timesheetCell, "timesheetEntry.hours");

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

    private enum DayStatus {
        OPEN,
        LOCKED
    }

    @SuppressWarnings("serial")
    private void createTimesheetEntryComment(final TimesheetRow row, final int index, WebMarkupContainer parent, DayStatus status) {
        final PropertyModel<String> commentModel = new PropertyModel<String>(row, "timesheetCells[" + index + "].timesheetEntry.comment");

        final ModalWindow modalWindow = new ModalWindow("dayWin");
        modalWindow.setResizable(false);
        modalWindow.setInitialWidth(500);
        modalWindow.setInitialHeight(325);

        modalWindow.setTitle(new StringResourceModel("timesheet.dayCommentsTitle", this, null));

        Component panel = status == DayStatus.OPEN ? new TimesheetEntryCommentPanel(modalWindow.getContentId(), commentModel, row, index, modalWindow) :
                new TimesheetEntryLockedCommentPanel(modalWindow.getContentId(), commentModel, row, index, modalWindow);
        modalWindow.setContent(panel);

        final AjaxLink<Void> commentLink = new AjaxLink<Void>("commentLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                target.prependJavaScript("Wicket.Window.unloadConfirmation = false;");
                modalWindow.show(target);
            }

            @Override
            protected void onBeforeRender() {
                ContextImage img = createContextImage(commentModel);

                addOrReplace(img);
                super.onBeforeRender();
            }
        };

        commentLink.setVisible(status == DayStatus.OPEN || StringUtils.isNotBlank(commentModel.getObject()));

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

    abstract class AbstractTimesheetEntryCommentPanel extends AbstractBasePanel<String> {
        private static final long serialVersionUID = 1L;
        private final TimesheetRow row;
        private final int index;
        protected final ModalWindow window;

        public AbstractTimesheetEntryCommentPanel(String id, final IModel<String> model, TimesheetRow row, int index, final ModalWindow window) {
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
                            new Object[]{row.getActivity().getFullName(),
                                    new DateModel(thisDate, config, DateModel.DATESTYLE_DAYONLY_LONG)})));

            AbstractLink cancelButton = new AjaxLink<Void>("cancel") {
                private static final long serialVersionUID = 1L;

                @Override
                public void onClick(AjaxRequestTarget target) {
                    AbstractTimesheetEntryCommentPanel.this.getPanelModel().setObject(previousModel);
                    window.close(target);
                }
            };
            addOrReplace(cancelButton);
        }
    }

    class TimesheetEntryCommentPanel extends AbstractTimesheetEntryCommentPanel {
        public TimesheetEntryCommentPanel(String id, final IModel<String> model, TimesheetRow row, int index, final ModalWindow window) {
            super(id, model, row, index, window);
        }

        @Override
        protected void onBeforeRender() {
            super.onBeforeRender();

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
        }
    }

    class TimesheetEntryLockedCommentPanel extends AbstractTimesheetEntryCommentPanel {
        private static final long serialVersionUID = 14343L;

        public TimesheetEntryLockedCommentPanel(String id, final IModel<String> model, TimesheetRow row, int index, final ModalWindow window) {
            super(id, model, row, index, window);
        }

        @Override
        protected void onBeforeRender() {
            super.onBeforeRender();

            addOrReplace(new Label("comment", getPanelModel()));
        }
    }
}