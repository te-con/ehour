/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.common.component;

import java.awt.image.BufferedImage;

import net.rrm.ehour.value.ImageLogo;

import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.protocol.http.WebResponse;

/**
 * Created on Apr 22, 2009, 6:14:07 PM
 * 
 * @author Thies Edeling (thies@te-con.nl)
 * 
 */
public class ImageResource extends DynamicImageResource
{
	private static final long serialVersionUID = -633274686788198830L;
	private byte[] image;

	/**
	 * 
	 * @param image
	 * @param format (extension)
	 */
	public ImageResource(byte[] image, String format)
	{
		this.image = image;
		setFormat(format);
	}

	public ImageResource(ImageLogo logo)
	{
		this.image = logo.getImageData();
		setFormat(logo.getImageType());
	}
	
	public ImageResource(BufferedImage image)
	{
		this.image = toImageData(image);
	}

	@Override
	protected byte[] getImageData()
	{
		if (image != null)
		{
			return image;
		} else
		{
			return new byte[0];
		}

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
}
