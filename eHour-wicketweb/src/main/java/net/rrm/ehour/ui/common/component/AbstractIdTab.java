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

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.model.IModel;

/**
 * Abstract tab with an ID 
 **/

public abstract class AbstractIdTab extends AbstractTab
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7566476444711512172L;
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
