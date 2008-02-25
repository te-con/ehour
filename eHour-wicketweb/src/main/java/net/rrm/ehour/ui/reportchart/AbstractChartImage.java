/**
 * Created on Feb 25, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.reportchart;

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
