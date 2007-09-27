/**
 * Created on May 28, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.border;

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
	public GreyRoundedBorder(String id, Integer width)
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
	public GreyRoundedBorder(String id, IModel title, Integer width)
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
	public GreyRoundedBorder(String id, IModel title, boolean showTitle, Link printLink, Link excelLink, Integer width)
	{
		super(id);
		
		WebMarkupContainer greyFrame = new WebMarkupContainer("greyFrame");
		
		if (width != null)
		{
			greyFrame.add(new SimpleAttributeModifier("style", "width: " + width.toString() + "px"));
		}
		
		Label	label = new Label("greyTabTitle", title);
		greyFrame.add(label);
		label.setVisible(showTitle);
		
		if (printLink == null)
		{
			printLink = getInvisibleLink("printLink");
		}
		
		greyFrame.add(printLink);
		
		if (excelLink == null)
		{
			excelLink = getInvisibleLink("excelLink");
		}
		
		greyFrame.add(excelLink);
		
		add(greyFrame);
		
		greyFrame.add(getBodyContainer());
	}

	/**
	 * Get an invisible link
	 * @param id
	 * @return
	 */
	@SuppressWarnings("serial")
	private Link getInvisibleLink(String id)
	{
		Link link = new Link(id)
		{
			@Override
			public void onClick()
			{
				// not visible anyway
			}
		};
		
		link.setVisible(false);
		
		return link;
	}
	
}
