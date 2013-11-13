package net.rrm.ehour.ui.admin.backup;

import net.rrm.ehour.backup.service.ParseSession;
import net.rrm.ehour.ui.admin.AbstractAdminPage;
import net.rrm.ehour.ui.admin.backup.restore.RestoreDbPanel;
import net.rrm.ehour.ui.admin.backup.restore.ValidateRestorePanel;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.PlaceholderPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.model.MessageResourceModel;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.ResourceModel;


public class BackupDbPage extends AbstractAdminPage<Void> {
    private static final String ID_PARSE_STATUS = "parseStatus";
    private static final String ID_RESTORE_BORDER = "restoreBorder";

    private static final long serialVersionUID = 821234996218723175L;
    private Form<Void> form;

    public BackupDbPage() {
        super(new ResourceModel("admin.export.title"));

        GreyRoundedBorder frame = new GreyRoundedBorder("frame", new ResourceModel("admin.export.title"));
        add(frame);

        GreyBlueRoundedBorder backupBorder = new GreyBlueRoundedBorder("backupBorder");
        frame.add(backupBorder);

        backupBorder.add(new Link<Void>("backupLink") {
            @Override
            public void onClick() {
                getRequestCycle().scheduleRequestHandlerAfterCurrent(new BackupDbRequestHandler());
            }
        });

        GreyBlueRoundedBorder restoreBorder = new GreyBlueRoundedBorder(ID_RESTORE_BORDER);
        frame.add(restoreBorder);
        form = addUploadForm("form");

        restoreBorder.add(form);

        form.add(new PlaceholderPanel(ID_PARSE_STATUS));
    }

    private Form<Void> addUploadForm(String id) {
        Form<Void> form = new Form<Void>(id);
        form.setMultiPart(true);

        add(form);

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
    @Override
    public boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
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
