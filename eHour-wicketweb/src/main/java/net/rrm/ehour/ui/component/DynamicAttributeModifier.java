/**
 * Created on Aug 28, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.component;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * Attribute modifier in which the model determines whether it's enabled or not
 **/

public class DynamicAttributeModifier extends AttributeModifier
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4038138991690100212L;
	private IModel	enablingModel;
	
	/**
	 * 
	 * @param attribute
	 * @param addAttributeIfNotPresent
	 * @param replaceModel
	 * @param enablingModel
	 */
	public DynamicAttributeModifier(String attribute, boolean addAttributeIfNotPresent,
									IModel replaceModel, IModel enablingModel)
	{
		super(attribute, addAttributeIfNotPresent, replaceModel);
		this.enablingModel = enablingModel;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.AttributeModifier#isEnabled(org.apache.wicket.Component)
	 */
	@Override
	public boolean isEnabled(Component comp)
	{
		return !((Boolean)enablingModel.getObject()).booleanValue();
	}
}