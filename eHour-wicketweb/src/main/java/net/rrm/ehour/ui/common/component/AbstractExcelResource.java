/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.component;

import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.log4j.Logger;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.time.Time;

import javax.servlet.http.HttpServletResponse;

/**
 * Abstract excel resource which sets content type and rest
 */

public abstract class AbstractExcelResource extends ByteArrayResource {
    private static final long serialVersionUID = -9078717513448771202L;
    private static final Logger LOGGER = Logger.getLogger(AbstractExcelResource.class);
    public static final String CONTENT_TYPE = "application/x-ms-excel";

    public AbstractExcelResource() {
        super(CONTENT_TYPE);
    }

    protected byte[] getReport(Attributes attributes) {
        PageParameters parameters = attributes.getParameters();

        StringValue optionalReportId = parameters.get("reportId");

        if (!optionalReportId.isEmpty()) {
            String reportId = optionalReportId.toString();
            Report report = getReport(reportId);

            try {
                return getExcelData(report);

            } catch (Exception e) {
                // FIXME handle better
                LOGGER.error("While creating excel report", e);
            }
        }

        throw new IllegalArgumentException("No valid report id provided");
    }

    private Report getReport(String reportId) {
        return (Report) EhourWebSession.getSession().getObjectCache().getObjectFromCache(reportId);
    }

    public abstract byte[] getExcelData(Report report) throws Exception;

    protected abstract String getFilename();

    @Override
    protected ResourceResponse newResourceResponse(final Attributes attributes) {
        final ResourceResponse response = new ResourceResponse();

        response.setLastModified(Time.now());

        response.setFileName(AbstractExcelResource.this.getFilename());

        if (response.dataNeedsToBeWritten(attributes)) {
            response.setContentType(CONTENT_TYPE);

            response.setContentDisposition(ContentDisposition.ATTACHMENT);

            final byte[] imageData = getReport(attributes);
            if (imageData == null) {
                response.setError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                response.setWriteCallback(new WriteCallback() {
                    @Override
                    public void writeData(final Attributes attributes) {
                        attributes.getResponse().write(imageData);
                    }
                });

                configureResponse(response, attributes);
            }
        }

        return response;
    }
}
