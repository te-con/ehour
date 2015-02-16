package net.rrm.ehour.ui.admin.backup.restore;

import net.rrm.ehour.backup.domain.ImportException;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.restore.RestoreService;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/9/10 - 11:33 PM
 */
public class RestoreDbPanel extends AbstractBasePanel<ParseSession>
{
    private static final long serialVersionUID = 5207207571905588721L;
    
    @SpringBean
    private RestoreService restoreService;

    public RestoreDbPanel(String id, ParseSession session)
    {
        super(id, new Model<>(session));

        try {
            restoreService.importDatabase(session);
        } catch (ImportException e) {
        }

        ParseStatusPanel status = new ParseStatusPanel("status", getPanelModel());
        add(status);

        add(new Label("statusMessage", new ResourceModel(session.hasErrors() ? "admin.import.button.validate" : "admin.import.label.importSuccess")));
    }

}
