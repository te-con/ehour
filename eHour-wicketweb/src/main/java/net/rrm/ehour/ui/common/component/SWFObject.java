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

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

/**
 * http://cwiki.apache.org/WICKET/open-flash-chart-and-wicket.html
 */
public class SWFObject extends AbstractBehavior implements IHeaderContributor
{
	private static final CompressedResourceReference SWFOBJECT_JS = new CompressedResourceReference(SWFObject.class, "js/swfobject.js");
	private static final long serialVersionUID = 1L;

	private Map<String, String> parameters = new HashMap<String, String>();
	private Map<String, String> attributes = new HashMap<String, String>();

	private String version;
	private String flashUrl;
	private String width;
	private String height;
	private Component component;

	@Override
	public void bind(Component component)
	{
		this.component = component;
		component.setOutputMarkupId(true);
	}

	public void renderHead(IHeaderResponse response)
	{
		response.renderJavascriptReference(SWFOBJECT_JS);

		response.renderJavascript(getJavascript(), null);
	}
	
	public String getJavascript()
	{
		final String id = component.getMarkupId();
		String parObj = buildDataObject(getParameters());
		String attObj = buildDataObject(getAttributes());

		// embedSWF: function(swfUrlStr, replaceElemIdStr, widthStr, heightStr,
		// swfVersionStr, xiSwfUrlStr, flashvarsObj, parObj, attObj)

		String javascript = String.format("swfobject.embedSWF('%s','%s', '%s', '%s', '%s', '%s', %s, %s );", flashUrl, id, width, height, version, "expressInstall.swf", parObj, attObj);
		
		return javascript;
		
	}

  /**
   * Construct.
   * <p/>
   * version can be a string in the format of 'majorVersion.minorVersion.revision'.
   * An example would be: "6.0.65". Or you can just require the major version, such as "6".
   *
   * @param flashUrl        The url of your swf file.
   * @param width           width of swf
   * @param height          height of movie
   * @param version         Flash version to support
   */
  	public SWFObject(final String flashUrl, final int width, final int height, final String version)
	{
		this(flashUrl, String.valueOf(width), String.valueOf(height), version);
	}

  /**
   * Construct.
   * @param flashUrl        URL to load up for swf
   * @param width           width of swf
   * @param height          height of movie
   * @param version         Flash version to support
   */
  public SWFObject(final String flashUrl, final String width, final String height, final String version)
	{
		if (flashUrl == null)
		{
			throw new IllegalArgumentException("Argument [flashUrl] cannot be null");
		}
		this.flashUrl = flashUrl;
		this.width = width;
		this.height = height;
		this.version = version;
	}

	private String buildDataObject(Map<String, String> data)
	{
		final String quote = "'";
		if (data != null && !data.isEmpty())
		{
			StringBuilder result = new StringBuilder();
			result.append("{");
			for (Map.Entry<String, String> e : getParameters().entrySet())
			{
				result.append(quote).append(e.getKey()).append(quote + ":" + quote).append(e.getValue()).append(quote);
			}
			result.append("}");
			return result.toString();
		}
		return "{}";
	}

	@Override
	public void onComponentTag(final Component component, final ComponentTag tag)
	{
	}

	protected Map<String, String> getParameters()
	{
		return parameters;
	}

	public void addParameter(String name, String value)
	{
		parameters.put(name, value);
	}

	protected Map<String, String> getAttributes()
	{
		return attributes;
	}

	public void addAttribute(String name, String value)
	{
		attributes.put(name, value);
	}

}