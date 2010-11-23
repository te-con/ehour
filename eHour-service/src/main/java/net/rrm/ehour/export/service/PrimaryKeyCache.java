package net.rrm.ehour.export.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 11/20/10 - 1:21 AM
 */
public class PrimaryKeyCache
{
    Map<Class<?>, Map<Serializable, Serializable>> keyMap = new HashMap<Class<?>, Map<Serializable, Serializable>>();

    public void putKey(Class<?> domainObjectClass, Serializable oldKey, Serializable newKey)
    {
        Map<Serializable, Serializable> oldNewKeyMap;

        if (keyMap.containsKey(domainObjectClass))
        {
            oldNewKeyMap = (Map<Serializable, Serializable>) keyMap.get(domainObjectClass);
        } else
        {
            oldNewKeyMap = new HashMap<Serializable, Serializable>();
        }

        oldNewKeyMap.put(oldKey, newKey);

        keyMap.put(domainObjectClass, oldNewKeyMap);
    }

    public Serializable getKey(Class<?> domainObjectClass, Serializable oldKey)
    {
        if (keyMap.containsKey(domainObjectClass))
        {
            return keyMap.get(domainObjectClass).get(oldKey);

        }

        return null;
    }

    boolean isEmpty()
    {
        return keyMap.isEmpty();
    }
}
