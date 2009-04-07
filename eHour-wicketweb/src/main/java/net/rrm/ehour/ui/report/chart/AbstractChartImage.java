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

package net.rrm.ehour.ui.report.chart;

import org.apache.wicket.Resource;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.jfree.chart.JFreeChart;

/**
 * Abstract chart image
 **/

public abstract class AbstractChartImage extends NonCachingImage
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2926007592965711057L;
	private int		width;
	private int		height;
	
	/**
	 * 
	 * @param id
	 * @param width
	 * @param height
	 */
	public AbstractChartImage(String id, 
								int width,
								int height)
	{
		this(id, null, width, height);
	}

	/**
	 * 
	 * @param id
	 * @param dataModel
	 * @param width
	 * @param height
	 */
	public AbstractChartImage(String id, 
								IModel dataModel,
								int width,
								int height)
	{
		super(id, dataModel);
		
		this.width = width;
		this.height = height;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.markup.html.image.Image#getImageResource()
	 */
	@Override
	protected Resource getImageResource()
	{
		return new DynamicImageResource()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected byte[] getImageData()
			{
				JFreeChart chart = generateChart();
				return toImageData(chart.createBufferedImage(width, height));
			}

			@Override
			protected void setHeaders(WebResponse response)
			{
				if (isCacheable())
				{
					super.setHeaders(response);
				} else
				{
					response.setHeader("Pragma", "no-cache");
					response.setHeader("Cache-Control", "no-cache");
					response.setDateHeader("Expires", 0);
				}
			}
		};
	}
	
	/**
	 * Generate chart
	 * @return
	 */
	protected abstract JFreeChart generateChart();
}
