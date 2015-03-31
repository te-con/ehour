package net.rrm.ehour.ui.admin.backup.restore;

import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.restore.BackupFileUtil;
import net.rrm.ehour.backup.service.restore.RestoreService;
import net.rrm.ehour.ui.admin.backup.BackupAjaxEventType;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.JavaScriptConfirmation;
import net.rrm.ehour.ui.common.decorator.DemoDecorator;
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.wicketstuff.async.components.IRunnableFactory;
import org.wicketstuff.async.components.InteractionState;
import org.wicketstuff.async.components.ProgressBar;
import org.wicketstuff.async.components.ProgressButton;
import org.wicketstuff.async.task.AbstractTaskContainer;
import org.wicketstuff.async.task.DefaultTaskManager;
import org.wicketstuff.async.task.IProgressObservableRunnable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RestoreDbFormPanel extends AbstractAjaxPanel<Void> {
    private static final Logger LOGGER = Logger.getLogger(RestoreDbFormPanel.class);

    private static final String ID_FEEDBACK = "feedback";
    private static final String ID_RESTORE_BORDER = "restoreBorder";

    private Form<Void> form;

    @SpringBean
    private RestoreService restoreService;
    private FeedbackPanel feedback;

    public RestoreDbFormPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        GreyRoundedBorder frame = new GreyRoundedBorder("frame", new ResourceModel("admin.export.restore.title"));
        addOrReplace(frame);

        GreyBlueRoundedBorder restoreBorder = new GreyBlueRoundedBorder(ID_RESTORE_BORDER);
        frame.add(restoreBorder);

        form = addUploadForm("form");
        restoreBorder.add(form);

        feedback = new FeedbackPanel(ID_FEEDBACK);
        feedback.setOutputMarkupId(true);
        form.add(feedback);
    }

    private Form<Void> addUploadForm(String id) {
        final boolean inDemoMode = EhourWebSession.getEhourConfig().isInDemoMode();

        Form<Void> form = new Form<>(id);
        form.setMultiPart(true);

        final FileUploadField file = new FileUploadField("file");
        form.add(file);

        AbstractTaskContainer taskContainer = DefaultTaskManager.getInstance().makeContainer(1000l, TimeUnit.MINUTES);

        final ParseSession session = new ParseSession();

        ProgressButton progressButton = new ProgressButton("submit", form, Model.of(taskContainer), getRunnableFactory(session), Duration.milliseconds(500l)) {
            @Override
            protected void onTaskError(AjaxRequestTarget ajaxRequestTarget) {
                ajaxRequestTarget.add(feedback);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (isValidUpload(file)) {
                    byte[] bytes = file.getFileUpload().getBytes();
                    try {
                        String tempFilename = BackupFileUtil.writeToTempFile(bytes);
                        session.setFilename(tempFilename);

                        super.onSubmit(target, form);
                    } catch (IOException e) {
                        LOGGER.error("While restoring", e);
                        error("Failed to write to temp file");
                    }

                    this.setEnabled(false);
                } else {
                    target.add(feedback);
                }
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                List<IAjaxCallListener> callListeners = attributes.getAjaxCallListeners();

                if (inDemoMode) {
                    callListeners.add(new DemoDecorator());
                } else {
                    callListeners.add(new JavaScriptConfirmation(new ResourceModel("admin.import.restore.confirm")));
                    callListeners.add(new LoadingSpinnerDecorator());
                }
            }
        };

        progressButton.registerMessageModel(Model.of("Start"), InteractionState.STARTABLE);
        progressButton.registerMessageModel(Model.of("Running..."), InteractionState.NON_INTERACTIVE);

        form.add(progressButton);

        form.add(new ProgressBar("bar", progressButton));

        return form;
    }

    private IRunnableFactory getRunnableFactory(final ParseSession session) {
        return new IRunnableFactory() {
            @Override
            public Runnable getRunnable() {
                return new IProgressObservableRunnable() {
                    @Override
                    public double getProgress() {
                        return session.getProgress();
                    }

                    @Override
                    public String getProgressMessage() {
                        return "Restoring";
                    }

                    @Override
                    public void run() {
                        try {
                            restoreService.importDatabase(session);
                        } catch (ImportException e) {
                            LOGGER.error(e);
                            throw new IllegalArgumentException(e);
                        }
                    }
                };
            }
        };
    }

    private boolean isValidUpload(FileUploadField field) {
        if (field.getFileUpload() != null) {
            FileUpload upload = field.getFileUpload();

            if (upload.getContentType() == null || !upload.getContentType().toLowerCase().contains("text")) {
                error("Invalid content type");
            } else if (StringUtils.isBlank(upload.getClientFileName())) {
                error("Empty file");
            } else if (upload.getBytes() == null || upload.getBytes().length == 0 || upload.getSize() == 0) {
                error("Empty file");
            }
        } else {
            error("Empty file");
        }

        return !hasErrorMessage();
    }

    @SuppressWarnings("unchecked")
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        boolean continueWithPropagating = true;

        if (ajaxEvent.getEventType() == BackupAjaxEventType.VALIDATED) {
            PayloadAjaxEvent<ParseSession> event = (PayloadAjaxEvent<ParseSession>) ajaxEvent;
            final ParseSession session = event.getPayload();

            Component replacement = new AjaxLazyLoadPanel(ID_FEEDBACK) {
                @Override
                public Component getLazyLoadComponent(String markupId) {
                    AjaxRequestTarget target = getRequestCycle().find(AjaxRequestTarget.class);

                    target.appendJavaScript("showHideSpinner(false);");
                    return new RestoreDbPanel(markupId, session);
                }

                @Override
                public Component getLoadingComponent(String markupId) {
                    AjaxRequestTarget target = getRequestCycle().find(AjaxRequestTarget.class);

                    target.appendJavaScript("showHideSpinner(true);");
                    return new Label(markupId, new ResourceModel("admin.import.label.restoring"));
                }
            };

            AjaxRequestTarget target = event.getTarget();

            replaceStatusPanel(replacement, target);
            continueWithPropagating = false;
        }

        return continueWithPropagating;
    }

    private void replaceStatusPanel(Component replacement, AjaxRequestTarget target) {
        replacement.setOutputMarkupId(true);
        form.addOrReplace(replacement);
        target.add(replacement);
    }
}
