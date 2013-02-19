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

package net.rrm.ehour.persistence.value;

import java.io.Serializable;

/**
 * Created on Apr 22, 2009, 5:49:56 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class ImageLogo implements Serializable
{
    private byte[] imageData;
    private int width;
    private int height;
    private String imageType;

    public byte[] getImageData()
    {
        return imageData;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public String getImageType()
    {
        return imageType;
    }

    public void setImageData(byte[] imageData)
    {
        this.imageData = imageData.clone();
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public void setImageType(String imageType)
    {
        this.imageType = imageType;
    }


}
