/**
 * Created on Aug 28, 2007
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
	private	boolean	reverseResult;
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
		this(attribute, addAttributeIfNotPresent, replaceModel, enablingModel, false);
	}

	/**
	 * 
	 * @param attribute
	 * @param addAttributeIfNotPresent
	 * @param replaceModel
	 * @param enablingModel
	 * @param inverseResult inverse the result of the model
	 */
	public DynamicAttributeModifier(String attribute, boolean addAttributeIfNotPresent,
			IModel replaceModel, IModel enablingModel, boolean reverseResult)
	{
		super(attribute, addAttributeIfNotPresent, replaceModel);
		this.enablingModel = enablingModel;
		this.reverseResult = reverseResult;
	}	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.AttributeModifier#isEnabled(org.apache.wicket.Component)
	 */
	@Override
	public boolean isEnabled(Component comp)
	{
		return (reverseResult) 
				? ((Boolean)enablingModel.getObject()).booleanValue() 
				: !((Boolean)enablingModel.getObject()).booleanValue();
	}
}