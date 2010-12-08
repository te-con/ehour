package net.rrm.ehour.ui.admin.export.panel;

import net.rrm.ehour.export.service.ImportException;
import net.rrm.ehour.export.service.ImportService;
import net.rrm.ehour.export.service.ParseSession;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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

    @SpringBean
    private ImportService importService;


    public ValidateImportPanel(String id, String xmlData)
    {
        super(id);

        ParseSession session = null;

        try
        {
            session = importService.prepareImportDatabase(xmlData);
            setDefaultModel(new Model<ParseSession>(session));
            initPanel();
        } catch (ImportException e)
        {
            handleImportException(e);
            add(createDummyLink());
        }
    }

    public ValidateImportPanel(String id, IModel<ParseSession> model)
    {
        super(id, model);

        initPanel();
    }

    private void initPanel()
    {
        IModel<ParseSession> model = getPanelModel();

        ParseStatusPanel status = new ParseStatusPanel(ID_STATUS, model);
        add(status);

        Component link;

        if (model.getObject().isCanBeImported())
        {
            link = new AjaxLink<Void>(ID_IMPORT_LINK)
            {
                @Override
                public void onClick(AjaxRequestTarget target)
                {
                    try
                    {
                        importXml();
                    } catch (ImportException e)
                    {
                        Component linkComponent = ValidateImportPanel.this.get(ID_IMPORT_LINK);
                        linkComponent.setVisible(false);
                        target.addComponent(linkComponent);

                        Component exceptionPanel = handleImportException(e);
                        target.addComponent(exceptionPanel);

                    }
                }
            };
        } else
        {
            link = createDummyLink();
        }

        add(link);
    }

    private Component createDummyLink()
    {
        Component link;
        link = new WebMarkupContainer(ID_IMPORT_LINK);
        link.setVisible(false);
        return link;
    }

    private void updateStatus(Component statusComponent)
    {
        statusComponent.setOutputMarkupId(true);
        addOrReplace(statusComponent);
    }

    private Component handleImportException(ImportException e)
    {
        Label errorMessage = new Label(ID_STATUS, "Failed to parse: " + e.getMessage());
        updateStatus(errorMessage);
        return errorMessage;
    }

    private void importXml() throws ImportException
    {
        ParseSession session = getPanelModel().getObject();
        importService.importDatabase(session);
        session.setImported(true);
    }
}