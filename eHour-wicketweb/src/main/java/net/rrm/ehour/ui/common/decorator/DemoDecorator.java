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

package net.rrm.ehour.ui.common.decorator;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.model.IModel;

/**
 * Display demo decorator
 **/

public class DemoDecorator implements IAjaxCallDecorator
{
	private static final long serialVersionUID = 1432993030793501257L;

	private IModel<String>	msgModel;
	
	public DemoDecorator(IModel<String> msgModel)
	{
		this.msgModel = msgModel;
	}
	
	public CharSequence decorateOnFailureScript(CharSequence script)
	{
		return "alert('" + msgModel.getObject() + "');";
	}

	public CharSequence decorateOnSuccessScript(CharSequence script)
	{
		return "alert('" +  msgModel.getObject() + "');";
	}

	public CharSequence decorateScript(CharSequence script)
	{
		return "alert('" +  msgModel.getObject() + "');";
	}
}
