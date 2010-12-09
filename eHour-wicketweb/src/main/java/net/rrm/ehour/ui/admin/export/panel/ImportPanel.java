package net.rrm.ehour.ui.admin.export.panel;

import net.rrm.ehour.export.service.ParseSession;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.model.Model;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/9/10 - 11:33 PM
 */
public class ImportPanel extends AbstractBasePanel<ParseSession>
{
    public ImportPanel(String id, ParseSession session)
    {
        super(id, new Model<ParseSession>(session));

//        initPanel();
    }
}
