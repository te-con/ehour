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

import net.rrm.ehour.ui.common.ajax.OnClickDecorator;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;

/**
 * AjaxLink which disables itself after first click
 **/

public abstract class DisablingAjaxLink extends AjaxLink
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6432581905417785191L;

	public DisablingAjaxLink(String id)
	{
		super(id);
	}
	
	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator()
	{
		return new OnClickDecorator("if (this.disabled) { return false; }; this.disabled = true;", null, null,
						"this.disabled=false");
	}
}
