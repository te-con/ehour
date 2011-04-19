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
import org.apache.wicket.model.IModel;

/**
 * Attribute modifier in which the model determines whether it's enabled or not
 **/

public class DynamicAttributeModifier extends AttributeModifier
{
	private static final long serialVersionUID = 4038138991690100212L;

	private IModel<Boolean>	enablingModel;
	private	boolean	reverseResult;
	/**
	 * 
	 * @param attribute
	 * @param addAttributeIfNotPresent
	 * @param replaceModel
	 * @param enablingModel
	 */
	public DynamicAttributeModifier(String attribute, boolean addAttributeIfNotPresent,
									IModel<?> replaceModel, IModel<Boolean> enablingModel)
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
			IModel<?> replaceModel, IModel<Boolean> enablingModel, boolean reverseResult)
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
				? enablingModel.getObject().booleanValue() 
				: !enablingModel.getObject().booleanValue();
	}
}