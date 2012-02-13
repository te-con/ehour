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

package net.rrm.ehour.ui.common.border;

import net.rrm.ehour.ui.common.util.WebGeo;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;


/**
 * Rounded border with a square left top mainly used for tabs 
 **/

public class GreySquaredRoundedBorder extends Border
{
	private static final long serialVersionUID = -793890240017442386L;

	public GreySquaredRoundedBorder(String id)
	{
		this(id, WebGeo.NOT_DEFINED);
	}
	
	public GreySquaredRoundedBorder(String id, WebGeo width)
	{
		super(id);
		
		WebMarkupContainer greyFrame = new WebMarkupContainer("greyFrame");
		add(greyFrame);
		
		WebMarkupContainer greySquaredFrame = new WebMarkupContainer("greySquaredFrame");
		
		if (width != null)
		{
			greySquaredFrame.add(new SimpleAttributeModifier("style", "width: " + width.toString()));
		}
		
		greySquaredFrame.add(getBodyContainer());
		greyFrame.add(greySquaredFrame);
	}

}
