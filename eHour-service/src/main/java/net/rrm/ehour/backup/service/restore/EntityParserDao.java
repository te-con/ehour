package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;

/**
 * Wraps the dao for persisting/retrieving so a different dao can be injected when
 * validating the XML
 *
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/28/10 - 1:31 AM
 */
public interface EntityParserDao {
    <T extends DomainObject<?, ?>> Serializable persist(T object);

    <T extends Serializable> T find(Serializable primaryKey, Class<T> type);
}
