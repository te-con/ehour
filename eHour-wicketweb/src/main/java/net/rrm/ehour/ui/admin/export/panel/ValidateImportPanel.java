package net.rrm.ehour.ui.admin.export.panel;

import net.rrm.ehour.export.service.ImportService;
import net.rrm.ehour.export.service.ParseSession;
import net.rrm.ehour.ui.admin.export.ExportAjaxEventType;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/7/10 - 2:11 AM
 */
public class ValidateImportPanel extends AbstractBasePanel<ParseSession>
{
    static final String ID_STATUS = "status";
    static final String ID_IMPORT_LINK = "importLink";
    private static final long serialVersionUID = -505699078695316620L;

    @SpringBean
    private ImportService importService;

    public ValidateImportPanel(String id, String xmlData)
    {
        super(id);

        ParseSession session = importService.prepareImportDatabase(xmlData);

        setDefaultModel(new Model<ParseSession>(session));
        initPanel();
    }

    private void initPanel()
    {
        IModel<ParseSession> model = getPanelModel();

        ParseStatusPanel status = new ParseStatusPanel(ID_STATUS, model);
        add(status);

        Component link;

        link = new AjaxLink<Void>(ID_IMPORT_LINK)
        {
            @Override
            public void onClick(AjaxRequestTarget target)
            {
                PayloadAjaxEvent<ParseSession> event = new PayloadAjaxEvent<ParseSession>(ExportAjaxEventType.VALIDATED,
                                                                                            ValidateImportPanel.this.getPanelModel().getObject(),
                                                                                            target);

                EventPublisher.publishAjaxEvent(ValidateImportPanel.this, event);
            }
        };

        link.setVisible(model.getObject().isImportable());

        add(link);
    }

}