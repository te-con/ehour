package net.rrm.ehour.ui.admin.backup.restore;

import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.ui.admin.backup.BackupAjaxEventType;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.PlaceholderPanel;
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.model.MessageResourceModel;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.ResourceModel;

public class RestoreDbFormPanel extends AbstractAjaxPanel<Void> {
    private static final String ID_PARSE_STATUS = "parseStatus";
    private static final String ID_RESTORE_BORDER = "restoreBorder";

    private Form<Void> form;

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

        form.add(new PlaceholderPanel(ID_PARSE_STATUS));
    }

    private Form<Void> addUploadForm(String id) {
        Form<Void> form = new Form<>(id);
        form.setMultiPart(true);

        final FileUploadField file = new FileUploadField("file");
        form.add(file);

        form.add(new AjaxSubmitLink("ajaxSubmit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Component replacementPanel;

                String errorMessage;

                if ((errorMessage = isValidUpload(file)) == null) {
                    byte[] bytes = file.getFileUpload().getBytes();
                    final String xmlData = new String(bytes);

                    replacementPanel = new AjaxLazyLoadPanel(ID_PARSE_STATUS) {
                        @Override
                        public Component getLazyLoadComponent(String markupId) {
                            return new ValidateRestorePanel(markupId, xmlData);
                        }

                        @Override
                        public Component getLoadingComponent(String markupId) {
                            return new Label(markupId, new ResourceModel("admin.import.label.validating"));
                        }
                    };
                } else {
                    replacementPanel = new Label(ID_PARSE_STATUS, new MessageResourceModel("admin.import.error.invalidFile", this, errorMessage));
                }

                replaceStatusPanel(replacementPanel, target);
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                // TODO Implement
            }
        });

        return form;
    }

    private String isValidUpload(FileUploadField field) {
        String errorMessage;

        if (field.getFileUpload() != null) {
            FileUpload upload = field.getFileUpload();

            if (upload.getContentType() == null || !upload.getContentType().toLowerCase().contains("text")) {
                errorMessage = "Invalid content type";
            } else if (StringUtils.isBlank(upload.getClientFileName())) {
                errorMessage = "Empty file";
            } else if (upload.getBytes() == null || upload.getBytes().length == 0 || upload.getSize() == 0) {
                errorMessage = "Empty file";
            } else {
                errorMessage = null;
            }
        } else {
            errorMessage = "Empty file";
        }

        return errorMessage;
    }

    @SuppressWarnings("unchecked")
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        boolean continueWithPropagating = true;

        if (ajaxEvent.getEventType() == BackupAjaxEventType.VALIDATED) {
            PayloadAjaxEvent<ParseSession> event = (PayloadAjaxEvent<ParseSession>) ajaxEvent;
            final ParseSession session = event.getPayload();

            Component replacement = new AjaxLazyLoadPanel(ID_PARSE_STATUS) {
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
