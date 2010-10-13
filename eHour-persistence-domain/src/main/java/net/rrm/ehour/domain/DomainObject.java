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

package net.rrm.ehour.domain;

import java.io.Serializable;

/**
 * Domain Object
 **/

public abstract class DomainObject <PK extends Serializable, DO extends Serializable> implements Serializable, Comparable<DO>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6706927368888981236L;

	/**
	 * Get primary key
	 * @return
	 */
	public abstract PK getPK();

	/**
	 * 
	 * @return
	 */
	public String getFullName()
	{
		return null;
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public abstract boolean equals(Object object);

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public abstract int hashCode();
}
