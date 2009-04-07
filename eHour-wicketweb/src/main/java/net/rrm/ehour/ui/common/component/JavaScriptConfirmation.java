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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Javascript confirmation dialog
 */

public class JavaScriptConfirmation extends AttributeModifier
{
	private static final long serialVersionUID = -4438660782434392618L;

	/**
	 * 
	 * @param event
	 * @param msg
	 */
	public JavaScriptConfirmation(String event, String msg)
	{
		this(event, new Model(msg));
	}

	/**
	 * 
	 * @param event
	 * @param msg
	 */
	public JavaScriptConfirmation(String event, IModel msg)
	{
		super(event, true, msg);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.AttributeModifier#newValue(java.lang.String, java.lang.String)
	 */
	protected String newValue(final String currentValue, final String replacementValue)
	{
		String result = "if (confirm('" + replacementValue + "')) { ";
		
		if (currentValue != null)
		{
			result = result + currentValue + "; } else { return false; }";
		}
		return result;
	}
}