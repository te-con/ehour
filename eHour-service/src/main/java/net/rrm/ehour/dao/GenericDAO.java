/**
 * Created on Dec 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.dao;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.domain.DomainObject;

/**
 * GenericDAO interface for CRUD on domain objects **/

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
}
