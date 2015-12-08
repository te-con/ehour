package net.rrm.ehour.ui.admin.backup;

import net.rrm.ehour.ui.admin.AbstractAdminPage;
import net.rrm.ehour.ui.admin.backup.backup.BackupDbPanel;
import net.rrm.ehour.ui.admin.backup.restore.RestoreDbFormPanel;
import net.rrm.ehour.ui.common.panel.contexthelp.ContextualHelpPanel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ResourceModel;

public class BackupDbPage extends AbstractAdminPage<Void> {
    private static final String ID_BACKUP = "backup";
    private static final String ID_RESTORE = "restore";

    private static final long serialVersionUID = 821234996218723175L;

    public BackupDbPage() {
        super(new ResourceModel("admin.export.title"));

        add(new BackupDbPanel(ID_BACKUP));
        add(new RestoreDbFormPanel(ID_RESTORE));

        add(new ContextualHelpPanel("help", "admin.export.help.header", "admin.export.help.body"));
    }
}
