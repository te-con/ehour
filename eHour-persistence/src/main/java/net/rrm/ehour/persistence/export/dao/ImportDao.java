package net.rrm.ehour.persistence.export.dao;

import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 19, 2010 - 12:07:04 AM
 */
public interface ImportDao
{
    public <T extends DomainObject<?, ?>> Serializable persist(T object);

    public <T, PK extends Serializable> T find(PK primaryKey, Class<T> type);
}
