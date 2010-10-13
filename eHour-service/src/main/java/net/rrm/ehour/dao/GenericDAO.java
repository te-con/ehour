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

package net.rrm.ehour.dao;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.domain.DomainObject;

/**
 * GenericDAO interface for CRUD on domain objects **/

@SuppressWarnings("unchecked")
public interface GenericDAO <T extends DomainObject, PK extends Serializable>
{
	/**
	 * Find all domain objects
	 * @return
	 */
	public List<T> findAll();
	
	/**
	 * Delete domain object
	 * @param domObj
	 */
	public void delete(T domObj);
	
	/**
	 * Delete on primary key
	 * @param pk
	 */
	public void delete(PK pk);
	
	/**
	 * Persist domain object
	 * @param domObj
	 * @return
	 */
	public T persist(T domObj);
	
	/**
	 * Find by primary key
	 * @param id
	 * @return
	 */
	public T findById(PK id);
	
	/**
	 * Merge the domain object
	 */
	public T merge(T domoj);
	
	/**
	 * Initialize lazy loading domain object
	 * @param domObj
	 */
	public void initializeObject(Object domObj);
}
