package net.rrm.ehour.ui.admin.backup.restore;

import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.restore.RestoreService;
import net.rrm.ehour.ui.admin.backup.BackupAjaxEventType;
import net.rrm.ehour.ui.common.decorator.DemoDecorator;
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/7/10 - 2:11 AM
 */
public class ValidateRestorePanel extends AbstractBasePanel<ParseSession> {
    static final String ID_STATUS = "status";
    static final String ID_IMPORT_LINK = "importLink";
    private static final long serialVersionUID = -505699078695316620L;

    @SpringBean
    private RestoreService restoreService;

    public ValidateRestorePanel(String id, String xmlData) {
        super(id);

        ParseSession session = restoreService.prepareImportDatabase(xmlData);
        add(new Label("statusMessage", new ResourceModel(session.hasErrors() ? "admin.import.error.validateFailed" : "admin.import.error.validateSuccess")));
        setDefaultModel(new Model<ParseSession>(session));
        initPanel();
    }

    private void initPanel() {
        IModel<ParseSession> model = getPanelModel();

        ParseStatusPanel status = new ParseStatusPanel(ID_STATUS, model);
        add(status);

        Component link = new AjaxLink<Void>(ID_IMPORT_LINK) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                if (!getConfig().isInDemoMode()) {
                    PayloadAjaxEvent<ParseSession> event = new PayloadAjaxEvent<ParseSession>(BackupAjaxEventType.VALIDATED,
                            ValidateRestorePanel.this.getPanelModel().getObject(),
                            target);

                    EventPublisher.publishAjaxEvent(ValidateRestorePanel.this, event);
                }
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                attributes.getAjaxCallListeners().add(getConfig().isInDemoMode() ? new DemoDecorator() : new LoadingSpinnerDecorator());
            }

        };

        link.setVisible(model.getObject().isImportable());

        add(link);
    }

}