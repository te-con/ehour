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

package net.rrm.ehour.ui.common.panel;

import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventListener;

import org.apache.wicket.model.IModel;


public abstract class AbstractAjaxPanel<T> extends AbstractBasePanel<T> implements AjaxEventListener
{
	private static final long serialVersionUID = 5723792133447447887L;

	public AbstractAjaxPanel(String id)
	{
		super(id);
	}

	public AbstractAjaxPanel(String id, IModel<T> model)
	{
		super(id, model);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		return true;
	}
}
