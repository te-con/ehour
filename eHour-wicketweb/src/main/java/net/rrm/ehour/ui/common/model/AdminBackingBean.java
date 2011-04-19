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

package net.rrm.ehour.ui.common.model;

import java.io.Serializable;

import net.rrm.ehour.domain.DomainObject;

/**
 * Backing bean interface for the admin screens. 
 **/

public interface AdminBackingBean extends Serializable
{
	/**
	 * @return the serverMessage
	 */
	public String getServerMessage();

	/**
	 * @param serverMessage the serverMessage to set
	 */
	public void setServerMessage(String serverMessage);
	
	/**
	 * Get the domain object behind the backing bean
	 * @return
	 */
	public DomainObject<?, ?> getDomainObject(); 
}
