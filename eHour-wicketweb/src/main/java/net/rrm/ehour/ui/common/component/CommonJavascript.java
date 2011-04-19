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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


/**
 * Created on Jan 31, 2009, 7:27:59 PM
 * @author Thies Edeling (thies@te-con.nl)
 * 
 * Adds an onMouseOn & onMouseOff attribute to an image
 *
 */
public class CommonJavascript 
{
	private static final ResourceReference JS_MOUSE_OVER = new ResourceReference(CommonJavascript.class, "js/ImageMouseOver.js");
	
	/**
	 * Add mouse over on importer
	 * @param imgComponent the importer which has the mouse over effect
	 * @param parent the parent component (panel/page) of the imgComponent
	 * @param imageUriOn image URI as text of the mouse over image
	 * @param imageUriOff image URI as text of the mouse out image
	 */
	public static void addMouseOver(Component imgComponent, Component parent, String imageUriOn, String imageUriOff, String id)
	{
		parent.add(JavascriptPackageResource.getHeaderContribution(JS_MOUSE_OVER));
		
		imgComponent.add(new OnMouseOnLoad("onmouseover", true, new Model<String>("onMouseOver(this, '" + imageUriOn + "', '" + id + "');")));
		imgComponent.add(new AttributeModifier("onmouseout", true, new Model<String>("onMouseOut(this, '" + imageUriOff + "', '" + id + "');")));
	}
	
	/**
	 * Modifier that makes sure that the images are resetted to null after load
	 * Created on Feb 1, 2009, 6:47:22 PM
	 * @author Thies Edeling (thies@te-con.nl) 
	 *
	 */
	@SuppressWarnings("serial")
	private static class OnMouseOnLoad extends AttributeModifier
	{
		OnMouseOnLoad(String attribute, boolean addAttributeIfNotPresent, IModel<String> replaceModel)
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
