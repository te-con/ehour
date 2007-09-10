/**
 * Created on Sep 10, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
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

package net.rrm.ehour.ui.component;

import org.apache.wicket.Resource;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebResponse;
import org.jfree.chart.JFreeChart;

/**
 * Display jfreechart
 **/

public class JFreeChartImage extends Image
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1758762143604579082L;

	private int width;

	private int height;

	public JFreeChartImage(String id, JFreeChart chart, int width, int height)
	{
		super(id, new Model(chart));
		this.width = width;
		this.height = height;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.image.Image#getImageResource()
	 */
	@Override
	@SuppressWarnings("serial")
	protected Resource getImageResource()
	{
		return new DynamicImageResource()
		{
			@Override
			protected byte[] getImageData()
			{
				JFreeChart chart = (JFreeChart) getModelObject();
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

}