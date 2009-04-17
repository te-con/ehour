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
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.util.time.Time;
import org.apache.wicket.util.value.ValueMap;

/**
 * Abstract excel resource which sets content type and rest
 **/

public abstract class AbstractExcelResource extends DynamicWebResource
{
	private static final long serialVersionUID = -9078717513448771202L;
	private final static Logger logger = Logger.getLogger(AbstractExcelResource.class);
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.DynamicWebResource#getResourceState()
	 */
	@Override
	protected ResourceState getResourceState()
	{
		ValueMap 		params = getParameters();
		ExcelResourceState	state = new ExcelResourceState();
		
		if (params.containsKey("reportId"))
		{
			Report report = getReport(params);
			
			try
			{
				byte[] data = getExcelData(report);
				state.setData(data);
				
			} catch (Exception e)
			{
				// FIXME handle better
				e.printStackTrace();
				logger.error("While creating excel report", e);
			}
		}
		else
		{
			logger.error("No valid report id provided");
		}
		
		return state;
	}

	private Report getReport(ValueMap params)
	{
		String reportId = params.getString("reportId");
		Report report = (Report)EhourWebSession.getSession().getObjectCache().getObjectFromCache(reportId);
		return report;
	}
	
	/**
	 * Get excel data as a byte array
	 * (public for testing)
	 * @return
	 */
	public abstract byte[] getExcelData(Report report) throws Exception;
	
	protected abstract String getFilename();

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.DynamicWebResource#setHeaders(org.apache.wicket.protocol.http.WebResponse)
	 */
	protected void setHeaders(WebResponse response)
	{
//		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.setAttachmentHeader(getFilename());
	}

	/**
	 * Resource state
	 * @author Thies
	 *
	 */
	private class ExcelResourceState extends ResourceState
	{
		private byte[] data;
		
		/*
		 * (non-Javadoc)
		 * @see org.apache.wicket.markup.html.DynamicWebResource$ResourceState#getContentType()
		 */
		@Override
		public String getContentType()
		{
			return "application/x-ms-excel";
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
}
