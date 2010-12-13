package net.rrm.ehour.ui.admin.export.page;

import net.rrm.ehour.export.service.ExportService;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Time;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 13, 2010 - 12:26:28 AM
 */
public class ExportDatabase extends DynamicWebResource
{
    public static final String ID_EXPORT_DB = "exportDb";

    private static final long serialVersionUID = 8027677671905365904L;


    @SpringBean(name = "exportService")
    private ExportService exportService;

    @Override
    protected ResourceState getResourceState()
    {
        CommonWebUtil.springInjection(this);
                
        String xmlExport = exportService.exportDatabase();

        ExportResourceState state = new ExportResourceState();

        try
        {
            byte[] bytes = xmlExport.getBytes("UTF-8");
            state.data = bytes;

        } catch (UnsupportedEncodingException e)
        {
            // won't happen as UTF-8 is required by all java impls
        }

        return state;
    }

    	protected void setHeaders(WebResponse response)
	{
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        response.setAttachmentHeader("eHour-xml-backup-" + format.format(new Date()) + ".xml");
	}

    private class ExportResourceState extends ResourceState
    {
        private byte[] data = new byte[2];

        /*
           * (non-Javadoc)
           * @see org.apache.wicket.markup.html.DynamicWebResource$ResourceState#getContentType()
           */

        @Override
        public String getContentType()
        {
            return "text/xml";
        }

        @Override
        public byte[] getData()
        {
            return data;
        }

        void setData(byte[] data)
        {
            this.data = data;
        }

        @Override
        public int getLength()
        {
            return data.length;
        }

        @Override
        public Time lastModifiedTime()
        {
            return Time.now();
        }
    }

    public void setExportService(ExportService exportService)
    {
        this.exportService = exportService;
    }
}
