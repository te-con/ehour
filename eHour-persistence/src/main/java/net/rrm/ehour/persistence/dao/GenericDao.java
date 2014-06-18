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

package net.rrm.ehour.persistence.dao;

import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;
import java.util.List;

/**
 * GenericDAO interface for CRUD on domain objects **/

public interface GenericDao <T extends DomainObject<?, ?>, PK extends Serializable>
{
	/**
	 * Find all domain objects
	 * @return
	 */
	List<T> findAll();
	
	/**
	 * Delete domain object
	 * @param domObj
	 */
	void delete(T domObj);
	
	/**
	 * Delete on primary key
	 * @param pk
	 */
	void delete(PK pk);
	
	/**
	 * Persist domain object
	 * @param domObj
	 * @return
	 */
	T persist(T domObj);
	
	/**
	 * Find by primary key
	 * @param id
	 * @return
	 */
	T findById(PK id);

	/**
	 * Merge the domain object
	 */
	T merge(T domoj);
}
