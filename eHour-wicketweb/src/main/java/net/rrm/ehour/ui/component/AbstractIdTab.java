/**
 * Created on Dec 11, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.component;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.model.IModel;

/**
 * Abstract tab with an ID 
 **/

public abstract class AbstractIdTab extends AbstractTab
{
	private TabId id;
	
	public AbstractIdTab(IModel title, TabId id)
	{
		super(title);
		
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public TabId getId()
	{
		return id;
	}
	
	/**
	 * Type interface
	 * @author Thies
	 *
	 */
	public interface TabId
	{
	}	
}
