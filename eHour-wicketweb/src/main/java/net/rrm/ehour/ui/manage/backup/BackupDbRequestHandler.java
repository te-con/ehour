package net.rrm.ehour.ui.manage.backup;

import net.rrm.ehour.backup.service.DatabaseBackupService;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.util.AuthUtil;
import net.rrm.ehour.ui.common.util.WebUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 12:26:28 AM
 */
public class BackupDbRequestHandler implements IRequestHandler {
    private static final long serialVersionUID = 8027677671905365904L;

    private static final Logger LOG = Logger.getLogger(BackupDbRequestHandler.class);
    public static final String CONTENT_TYPE = "text/xml";

    @SpringBean(name = "databaseBackupService")
    private DatabaseBackupService databaseBackupService;

    @Override
    public void detach(IRequestCycle requestCycle) {
    }

    @Override
    public void respond(IRequestCycle requestCycle) {
        try {
            HttpServletResponse response = (HttpServletResponse) requestCycle.getResponse().getContainerResponse();

            response.setContentType(CONTENT_TYPE);
            response.setHeader("Content-disposition", "attachment; filename=" + getFilename());

            byte[] data = getData();

            ServletOutputStream outputStream = response.getOutputStream();

            outputStream.write(data);
        } catch (IOException e) {
            LOG.error("Failed to backup database", e);
            throw new RuntimeException(e);
        }

    }


    private byte[] getData() {
        boolean authorized = checkAuthorization();

        if (authorized) {
            WebUtils.springInjection(this);

            String xmlExport = databaseBackupService.exportDatabase();

            try {
                return xmlExport.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }

        throw new IllegalArgumentException("You're not authorized");
    }

    private boolean checkAuthorization() {
        return (AuthUtil.hasRole(UserRole.ROLE_ADMIN));
    }


    private String getFilename() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return "eHour-xml-backup-" + format.format(new Date()) + ".xml";
    }
}
