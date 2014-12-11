package net.rrm.ehour.ui.timesheet.panel.dailycomments;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.component.CommonModifiers;
import net.rrm.ehour.ui.common.component.KeepAliveTextArea;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.dto.TimesheetCell;
import net.rrm.ehour.ui.timesheet.panel.TimesheetRowList.DayStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.Date;

public class DailyCommentPanel extends Panel {
    private static final String COMMENT_LINK_IMG_ID = "commentLinkImg";

    public DailyCommentPanel(String id, TimesheetCell cell, DayStatus status) {
        super(id);

        final PropertyModel<String> commentModel = new PropertyModel<>(cell, "timesheetEntry.comment");

        final ModalWindow modalWindow = new ModalWindow("dayWin");
        modalWindow.setResizable(false);
        modalWindow.setInitialWidth(500);
        modalWindow.setInitialHeight(325);

        modalWindow.setTitle(new StringResourceModel("timesheet.dayCommentsTitle", this, null));

        Component panel = status == DayStatus.OPEN ? new TimesheetEntryCommentPanel(modalWindow.getContentId(), commentModel, cell, modalWindow) :
                new TimesheetEntryLockedCommentPanel(modalWindow.getContentId(), commentModel, cell, modalWindow);
        modalWindow.setContent(panel);
        modalWindow.showUnloadConfirmation(false);

        final AjaxLink<Void> commentLink = new AjaxLink<Void>("commentLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                modalWindow.show(target);
            }

            @Override
            protected void onBeforeRender() {
                addOrReplace(createContextImage(commentModel));
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

        add(modalWindow);

        commentLink.setOutputMarkupId(true);
        commentLink.add(CommonModifiers.tabIndexModifier(255));

        add(commentLink);
    }

    private WebMarkupContainer createContextImage(PropertyModel<String> commentModel) {
        WebMarkupContainer container = new WebMarkupContainer(COMMENT_LINK_IMG_ID);

        if (StringUtils.isNotBlank(commentModel.getObject())) {
            container.add(AttributeModifier.replace("class", "fa fa-pencil iconLinkOn"));
        }
        return container;
    }

    private void setCommentLinkClass(IModel<String> commentModel, AjaxLink<Void> commentLink) {
        commentLink.add(AttributeModifier.replace("class", StringUtils.isBlank(commentModel.getObject()) ? "timesheetEntryComment" : "timesheetEntryCommented"));
    }

    abstract class AbstractTimesheetEntryCommentPanel extends AbstractBasePanel<String> {
        private static final long serialVersionUID = 1L;
        private final TimesheetCell cell;
        protected final ModalWindow window;

        public AbstractTimesheetEntryCommentPanel(String id, final IModel<String> model, TimesheetCell cell, final ModalWindow window) {
            super(id, model);
            this.cell = cell;
            this.window = window;
        }

        @Override
        protected void onBeforeRender() {
            super.onBeforeRender();

            EhourConfig config = EhourWebSession.getEhourConfig();

            Date date = cell.getDate();

            final String previousModel = getPanelModelObject();
            addOrReplace(new Label("dayComments",
                    new StringResourceModel("timesheet.dayComments",
                            this,
                            null,
                            new Object[]{cell.getProjectAssignment().getFullName(),
                                    new DateModel(date, config, DateModel.DATESTYLE_DAYONLY_LONG)})));

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
        public TimesheetEntryCommentPanel(String id, final IModel<String> model, TimesheetCell cell, final ModalWindow window) {
            super(id, model, cell, window);
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

        public TimesheetEntryLockedCommentPanel(String id, final IModel<String> model, TimesheetCell cell, final ModalWindow window) {
            super(id, model, cell, window);
        }

        @Override
        protected void onBeforeRender() {
            super.onBeforeRender();

            addOrReplace(new Label("comment", getPanelModel()));
        }
    }
}
