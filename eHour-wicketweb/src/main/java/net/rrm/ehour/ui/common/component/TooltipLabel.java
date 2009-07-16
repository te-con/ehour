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

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.JavaScriptReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Tooltip label with info image next to it.
 * Displays a tooltip when hovered over the text using SweetTitles
 * When the tooltipText is empty the tooltip img button is now shown
 * 
 * Created on Feb 1, 2009, 6:57:30 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class TooltipLabel extends Panel
{
	private static final long serialVersionUID = -2407607082770130038L;

	/**
	 * Includes info img
	 * @param id
	 * @param label 
	 * @param tooltipText
	 */
	public TooltipLabel(final String id, String label, String tooltipText)
	{
		this(id, label, tooltipText, true);
	}
	
	/**
	 * 
	 * @param id
	 * @param label
	 * @param tooltipText
	 * @param showInfoImg
	 */
	public TooltipLabel(final String id, String label, String tooltipText, boolean showInfoImg)
	{
		this(id, new Model(label), new Model(tooltipText), showInfoImg);
	}
	
	/**
	 * @param id
	 * @param label
	 */
	public TooltipLabel(String id, IModel label, IModel tooltipText, boolean showInfoImg)
	{
		super(id);
		
		add(new Label("content", label));
		
		add(new JavaScriptReference("addEventJs", new ResourceReference(TooltipLabel.class, "js/addEvent.js")));
		add(new JavaScriptReference("sweetTitlesJs", new ResourceReference(TooltipLabel.class, "js/sweetTitles.js")));
		
		Boolean showTooltip = StringUtils.isBlank((String)tooltipText.getObject()) ? Boolean.FALSE : Boolean.TRUE;
		
		
		add(new AttributeModifier("showtooltip", true, new Model(showTooltip.toString())));
		add(new AttributeModifier("title", true, tooltipText));
		
		ContextImage img = new ContextImage("infoImg", new Model("img/info.gif"));
		img.setVisible(showInfoImg);
		add(img);
	}
}
