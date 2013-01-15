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
import net.rrm.ehour.ui.common.util.WebGeo;

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
		this(id, new Model<String>(), null, null);
	}


	/**
	 * No title but with print & excel link
	 * @param id
	 * @param printLink
	 * @param excelLink
	 */
	public GreyRoundedBorder(String id, Link<Void> printLink, Link<Void> excelLink)
	{
		this(id, new Model<String>(), false, printLink, excelLink);
	}
	
	/**
	 * Title, without the links 
	 * @param id
	 * @param title
	 */
	public GreyRoundedBorder(String id, String title)
	{
		this(id, new Model<String>(title));
	}
	
	/**
	 * Title, without the links
	 * @param id
	 * @param title
	 */
	public GreyRoundedBorder(String id, IModel<String> title)
	{
		this(id, title, null, null);
	}
	
	/**
	 * Title and print & excel links
	 * @param id
	 * @param title
	 * @param printLink
	 * @param excelLink
	 */
	public GreyRoundedBorder(String id, IModel<String> title, Link<Void> printLink, Link<Void> excelLink)
	{
		this(id, title, true, printLink, excelLink);
	}
	
	public GreyRoundedBorder(String id, IModel<String> title, boolean showTitle, Link<?> printLink, Link<?> excelLink)
	{
		super(id);
		
		greyFrame = new WebMarkupContainer("greyFrame");

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
