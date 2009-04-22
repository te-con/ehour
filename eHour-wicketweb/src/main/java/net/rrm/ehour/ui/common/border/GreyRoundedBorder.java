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

import net.rrm.ehour.ui.common.util.HtmlUtil;
import net.rrm.ehour.ui.common.util.WebWidth;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * CSS in ehour.css as it's very common
 **/

public class GreyRoundedBorder extends Border
{
	private static final long serialVersionUID = 7184643596615028876L;
	protected WebMarkupContainer greyFrame;
	
	/**
	 * Default border. No title, links and default width 
	 * @param id
	 */
	public GreyRoundedBorder(String id)
	{
		this(id, new Model(), null, null);
	}

	/**
	 * No title but specified width
	 * @param id
	 * @param width
	 */
	public GreyRoundedBorder(String id, WebWidth width)
	{
		this(id, new Model(), false, null, null, width);
	}

	
	/**
	 * No title but with print & excel link
	 * @param id
	 * @param printLink
	 * @param excelLink
	 */
	public GreyRoundedBorder(String id, Link printLink, Link excelLink)
	{
		this(id, new Model(), false, printLink, excelLink, null);
	}
	
	/**
	 * Title, without the links 
	 * @param id
	 * @param title
	 */
	public GreyRoundedBorder(String id, String title)
	{
		this(id, new Model(title));
	}
	
	/**
	 * Title, without the links
	 * @param id
	 * @param title
	 */
	public GreyRoundedBorder(String id, IModel title)
	{
		this(id, title, null, null);
	}
	
	/**
	 * Title, without the links but with specified width
	 * @param id
	 * @param title
	 */
	public GreyRoundedBorder(String id, IModel title, WebWidth width)
	{
		this(id, title, true, null, null, width);
	}	
	
	/**
	 * Title and print & excel links
	 * @param id
	 * @param title
	 * @param printLink
	 * @param excelLink
	 */
	public GreyRoundedBorder(String id, IModel title, Link printLink, Link excelLink)
	{
		this(id, title, true, printLink, excelLink, null);
	}
	
	/**
	 * With title, links and width
	 * @param id
	 * @param title
	 * @param showTitle
	 * @param printLink
	 * @param excelLink
	 * @param width
	 */
	public GreyRoundedBorder(String id, IModel title, boolean showTitle, Link printLink, Link excelLink, WebWidth width)
	{
		super(id);
		
		greyFrame = new WebMarkupContainer("greyFrame");
		
		if (width != null)
		{
			greyFrame.add(new SimpleAttributeModifier("style", "width: " + width.getWidth().toString() + "px"));
		}
		
		Label	label = new Label("greyTabTitle", title);
		greyFrame.add(label);
		label.setVisible(showTitle);
		
		if (printLink == null)
		{
			printLink = HtmlUtil.getInvisibleLink("printLink");
		}
		
		greyFrame.add(printLink);
		
		if (excelLink == null)
		{
			excelLink = HtmlUtil.getInvisibleLink("excelLink");
		}
		
		greyFrame.add(excelLink);
		
		add(greyFrame);
		
		greyFrame.add(getBodyContainer());
	}
}
