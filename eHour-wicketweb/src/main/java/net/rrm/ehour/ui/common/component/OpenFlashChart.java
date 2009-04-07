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

import ofc4j.model.Chart;

import org.apache.wicket.IResourceListener;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.util.resource.AbstractStringResourceStream;
import org.apache.wicket.util.resource.IResourceStream;


public class OpenFlashChart extends Panel implements IResourceListener
{
	private static final long serialVersionUID = -6601630129888436301L;

	static final ResourceReference SWF_RESOURCE = new ResourceReference(OpenFlashChart.class, "js/open-flash-chart.swf");

	final WebResource jsonResource;

	final SWFObject swf;

	public OpenFlashChart(String id, final String width, final String height, final Chart chart)
	{
		this(id, width, height, new Model(chart.toString()));
	}

	public OpenFlashChart(String id, final int width, final int height, final Chart chart)
	{
		this(id, String.valueOf(width), String.valueOf(height), new Model(chart.toString()));
	}

	public OpenFlashChart(String id, final int width, final int height, IModel jsonModel)
	{
		this(id, String.valueOf(width), String.valueOf(height), jsonModel);
	}

	@SuppressWarnings("serial")
	public OpenFlashChart(String id, final String width, final String height, final IModel jsonModel)
	{
		super(id);
	
		final IResourceStream json = new AbstractStringResourceStream("text/plain")
		{
			@Override
			public String getString()
			{
				return (String) jsonModel.getObject();
			}
		};
	
		jsonResource = new WebResource()
		{
			@Override
			public IResourceStream getResourceStream()
			{
				return json;
			}
		};
		jsonResource.setCacheable(false);
	
		String swfURL = RequestUtils.toAbsolutePath(urlFor(SWF_RESOURCE).toString());
		add(swf = new SWFObject(swfURL, width, height, "9.0.0"));
	}

	private String getUrlForJson()
	{
		CharSequence dataPath = RequestCycle.get().urlFor(OpenFlashChart.this, IResourceListener.INTERFACE);
		return RequestUtils.toAbsolutePath(dataPath.toString());
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender()
	{
		swf.addParameter("data-file", getUrlForJson());
		super.onBeforeRender();
	}

	public String getJavascript()
	{
		swf.addParameter("data-file", getUrlForJson());
		
		return swf.getJavascript();
	}
	
	/**
	 * Actually handle the request
	 */
	public void onResourceRequested()
	{
		jsonResource.onResourceRequested();
	}
	
	/**
	 * @return the swf
	 */
	public SWFObject getSwf()
	{
		return swf;
	}
}