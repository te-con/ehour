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

package net.rrm.ehour.ui.common.renderers;

import net.rrm.ehour.domain.DomainObject;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * Domain object renderer choice renders
 **/

public class DomainObjectChoiceRenderer<T extends DomainObject<?, ?>> implements IChoiceRenderer<T>
{
	private static final long serialVersionUID = 9021045667533511410L;

	public Object getDisplayValue(T object)
	{
		return object.getFullName();
	}

	public String getIdValue(T object, int index)
	{
		return object.getPK().toString();
	}
}
