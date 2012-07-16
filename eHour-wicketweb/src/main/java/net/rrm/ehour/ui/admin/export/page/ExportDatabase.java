package net.rrm.ehour.ui.admin.export.page;

import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.export.service.ExportService;
import net.rrm.ehour.ui.common.util.AuthUtil;
import net.rrm.ehour.ui.common.util.WebUtils;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Time;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 12:26:28 AM
 */
public class ExportDatabase extends ByteArrayResource {
    public static final String ID_EXPORT_DB = "exportDb";

    private static final long serialVersionUID = 8027677671905365904L;
    public static final String CONTENT_TYPE = "text/xml";

    @SpringBean(name = "exportService")
    private ExportService exportService;

    public ExportDatabase() {
        super(CONTENT_TYPE);
    }

    @Override
    protected ResourceResponse newResourceResponse(final Attributes attributes) {
        final ResourceResponse response = new ResourceResponse();

        response.setLastModified(Time.now());

        response.setFileName(getFilename());

        if (response.dataNeedsToBeWritten(attributes)) {
            response.setContentType(CONTENT_TYPE);

            response.setContentDisposition(ContentDisposition.ATTACHMENT);

            final byte[] data = getData();
            if (data == null) {
                response.setError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                response.setWriteCallback(new WriteCallback() {
                    @Override
                    public void writeData(final Attributes attributes) {
                        attributes.getResponse().write(data);
                    }
                });

                configureResponse(response, attributes);
            }
        }

        return response;
    }


    private byte[] getData() {
        boolean authorized = checkAuthorization();

        if (authorized) {
            WebUtils.springInjection(this);

            String xmlExport = exportService.exportDatabase();

            try {
                return xmlExport.getBytes("UTF-8");

            } catch (UnsupportedEncodingException e) {
                // won't happen as UTF-8 is required by all java impls
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

    public void setExportService(ExportService exportService) {
        this.exportService = exportService;
    }
}
