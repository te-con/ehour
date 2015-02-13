package net.rrm.ehour.ui.admin.backup.backup;

import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

public class BackupDbPanel extends AbstractBasePanel<Void> {
    private static final String ID_BACKUP_BORDER = "backupBorder";

    public BackupDbPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        GreyRoundedBorder frame = new GreyRoundedBorder("frame", new ResourceModel("admin.export.backup.title"));
        add(frame);

        GreyBlueRoundedBorder backupBorder = new GreyBlueRoundedBorder(ID_BACKUP_BORDER);
        frame.add(backupBorder);

        IModel<Boolean> busyModel = new Model<>(Boolean.FALSE);
        BackupDownloadBehavior downloadBehavior = new BackupDownloadBehavior(busyModel);
        SingleDownloadLink link = new SingleDownloadLink("backupLink", busyModel, downloadBehavior);
        backupBorder.add(link);
    }
}
