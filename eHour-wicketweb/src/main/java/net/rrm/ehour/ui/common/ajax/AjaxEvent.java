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

package net.rrm.ehour.ui.common.ajax;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Transfer object for ajax events
 **/

public class AjaxEvent implements Serializable
{
	private static final long serialVersionUID = -8723330496152721044L;
	private AjaxEventType	eventType;
	
	/**
	 * 
	 * @param target
	 * @param eventType
	 */
	public AjaxEvent(AjaxEventType eventType)
	{
		this.eventType = eventType;
	}

	/**
	 * @return the target
	 */
	public AjaxRequestTarget getTarget()
	{
		return AjaxRequestTarget.get();
	}

	/**
	 * @return the eventType
	 */
	public AjaxEventType getEventType()
	{
		return eventType;
	}
}
