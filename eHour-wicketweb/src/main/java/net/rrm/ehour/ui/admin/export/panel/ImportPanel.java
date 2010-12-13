package net.rrm.ehour.ui.admin.export.panel;

import net.rrm.ehour.export.service.ImportService;
import net.rrm.ehour.export.service.ParseSession;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/9/10 - 11:33 PM
 */
public class ImportPanel extends AbstractBasePanel<ParseSession>
{
    private static final long serialVersionUID = 5207207571905588721L;
    
    @SpringBean
    private ImportService importService;

    public ImportPanel(String id, ParseSession session)
    {
        super(id, new Model<ParseSession>(session));

        importService.importDatabase(session);

        ParseStatusPanel status = new ParseStatusPanel("status", getPanelModel());
        add(status);
    }

}
