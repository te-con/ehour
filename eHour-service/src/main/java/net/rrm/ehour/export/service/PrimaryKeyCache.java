package net.rrm.ehour.export.service;

import net.rrm.ehour.domain.DomainObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/20/10 - 1:21 AM
 */
public class PrimaryKeyCache
{
    Map<Class<? extends DomainObject<?, ?>>, Map<?, ?>> keyMap = new HashMap<Class<? extends DomainObject<?, ?>>, Map<?, ?>>();

    public <PK extends Serializable> void putKey(Class<? extends DomainObject<PK, ?>> domainObjectClass, PK oldKey, PK newKey)
    {
        Map<PK, PK> oldNewKeyMap;

        if (keyMap.containsKey(domainObjectClass))
        {
            oldNewKeyMap = (Map<PK, PK>) keyMap.get(domainObjectClass);
        } else
        {
            oldNewKeyMap = new HashMap<PK, PK>();
        }

        oldNewKeyMap.put(oldKey, newKey);

        keyMap.put(domainObjectClass, oldNewKeyMap);
    }

    public <PK extends Serializable> PK getKey(Class<? extends DomainObject<PK, ?>> domainObjectClass, PK oldKey)
    {
        if (keyMap.containsKey(domainObjectClass))
        {
            return (PK) keyMap.get(domainObjectClass).get(oldKey);

        }

        return null;
    }
}
