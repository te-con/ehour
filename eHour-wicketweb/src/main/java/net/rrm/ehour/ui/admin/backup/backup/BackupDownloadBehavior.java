package net.rrm.ehour.ui.admin.backup.backup;

import net.rrm.ehour.backup.service.backup.DatabaseBackupService;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebUtils;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupDownloadBehavior extends AbstractDownloadBehavior {
    @SpringBean(name = "databaseBackupService")
    private DatabaseBackupService databaseBackupService;

    public BackupDownloadBehavior(IModel<Boolean> model) {
        super(model);
    }

    @Override
    protected IResourceStream getResourceStream() {

        try {
            return new StringResourceStream(new String(getData(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected String getFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return "eHour-xml-backup-" + format.format(new Date()) + ".xml";
    }

    private byte[] getData() {
        boolean authorized = checkAuthorization();

        if (authorized) {
            WebUtils.springInjection(this);

            return databaseBackupService.exportDatabase();
        }

        throw new IllegalArgumentException("You're not authorized");
    }

    private boolean checkAuthorization() {
        return EhourWebSession.getSession().isAdmin();
    }
}
