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
	 * 
	 * @param id
	 */
	public GreyRoundedBorder(String id)
	{
		this(id, new Model(), false, null, null);
	}

	public GreyRoundedBorder(String id, Link printLink, Link excelLink)
	{
		this(id, new Model(), false, printLink, excelLink);
	}
	
	/**
	 * 
	 * @param id
	 * @param title
	 */
	public GreyRoundedBorder(String id, String title)
	{
		this(id, new Model(title));
	}
	
	public GreyRoundedBorder(String id, IModel title)
	{
		this(id, title, true, null, null);
	}
	
	/**
	 * 
	 * @param id
	 * @param title
	 */
	public GreyRoundedBorder(String id, IModel title, boolean showTitle, Link printLink, Link excelLink)
	{
		super(id);
		
		Label	label = new Label("greyTabTitle", title);
		add(label);
		label.setVisible(showTitle);
		
		if (printLink == null)
		{
			printLink = new Link("printLink")
			{
				@Override
				public void onClick()
				{
					// not visible anyway
				}
			};
			printLink.setVisible(false);
		}
		
		add(printLink);
		
		if (excelLink == null)
		{
			excelLink = new Link("excelLink")
			{
				@Override
				public void onClick()
				{
					// not visible anyway
				}
			};
			excelLink.setVisible(false);
		}
		
		add(excelLink);		
	}
	
}
