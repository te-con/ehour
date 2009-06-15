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

import java.io.Serializable;

import net.rrm.ehour.domain.DomainObject;

import org.apache.wicket.Application;
import org.apache.wicket.Localizer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * TODO 
 **/

public abstract class LocalizedResourceRenderer implements IChoiceRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3533972441275552509L;

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	public Object getDisplayValue(Object obj)
	{
		String key = getResourceKey(obj);
			
		Localizer localizer = Application.get().getResourceSettings().getLocalizer();
		
		return localizer.getString(key, null);
	}

	/**
	 * 
	 * @param o
	 * @return
	 */
	protected abstract String getResourceKey(Object o);

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
	 */
	@SuppressWarnings("unchecked")
	public String getIdValue(Object object, int index)
	{
		return ((DomainObject<Serializable, Serializable>)object).getPK().toString();
	}
}
