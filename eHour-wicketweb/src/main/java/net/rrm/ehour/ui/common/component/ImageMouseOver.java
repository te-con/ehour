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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


/**
 * Created on Jan 31, 2009, 7:27:59 PM
 * @author Thies Edeling (thies@te-con.nl)
 * 
 * Adds an onMouseOn & onMouseOff attribute to an image
 *
 */
public class ImageMouseOver 
{
	public static void addMouseOver(Component imgComponent, Component parent, String imageUriOn, String imageUriOff)
	{
		parent.add(HeaderContributor.forJavaScript(ImageMouseOver.class, "js/ImageMouseOver.js"));
		
		imgComponent.add(new OnMouseOnLoad("onmouseover", true, new Model("onMouseOver(this, '" + imageUriOn + "');")));
		imgComponent.add(new AttributeModifier("onmouseout", true, new Model("onMouseOut(this, '" + imageUriOff + "');")));
	}

	private static class OnMouseOnLoad extends AttributeModifier
	{
		private static final long serialVersionUID = -5494844004223491158L;

		public OnMouseOnLoad(String attribute, boolean addAttributeIfNotPresent, IModel replaceModel)
		{
			super(attribute, addAttributeIfNotPresent, replaceModel);
		}

		@Override
		public void renderHead(IHeaderResponse response)
		{
			super.renderHead(response);
			
			response.renderOnLoadJavascript("initImagePreload()");
		}
	}
}
