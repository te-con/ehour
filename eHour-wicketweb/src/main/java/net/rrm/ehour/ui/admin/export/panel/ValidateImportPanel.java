package net.rrm.ehour.ui.admin.export.panel;

import net.rrm.ehour.export.service.ImportException;
import net.rrm.ehour.export.service.ImportService;
import net.rrm.ehour.export.service.ParseSession;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/7/10 - 2:11 AM
 */
public class ValidateImportPanel extends AbstractBasePanel<ParseSession>
{
    @SpringBean
    private ImportService importService;

    private static final String ID_IMPORT_LINK = "importLink";


    public ValidateImportPanel(String id, String xmlData)
    {
        super(id);

        ParseSession session = null;

        try
        {
            session = importService.prepareImportDatabase(xmlData);
        } catch (ImportException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        setDefaultModel(new Model<ParseSession>(session));
        initPanel();
    }

    public ValidateImportPanel(String id, IModel<ParseSession> model)
    {
        super(id, model);

        initPanel();
    }

    private void initPanel()
    {
        IModel<ParseSession> model = getPanelModel();

        ParseStatusPanel status = new ParseStatusPanel("status", model);
        add(status);

        Component link;

        if (!model.getObject().isImported() && !model.getObject().hasErrors())
        {
            link = new AjaxLink<Void>(ID_IMPORT_LINK)
            {
                @Override
                public void onClick(AjaxRequestTarget target)
                {
                    importXml();
                }
            };
        } else
        {
            link = new WebMarkupContainer(ID_IMPORT_LINK);
            link.setVisible(false);
        }

        add(link);
    }

    private void importXml()
    {
        try
        {
            ParseSession session = getPanelModel().getObject();
            importService.importDatabase(session);
            session.setImported(true);
        } catch (ImportException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}