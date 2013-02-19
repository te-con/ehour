package net.rrm.ehour.persistence.backup.dao;

import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: Nov 19, 2010 - 12:07:04 AM
 */
public interface RestoreDao
{
    <T extends DomainObject<?, ?>> Serializable persist(T object);

    <T, PK extends Serializable> T find(PK primaryKey, Class<T> type);

    void flush();

    <T> void delete(Class<T> type);
}
