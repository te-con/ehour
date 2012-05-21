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

package net.rrm.ehour.ui.common.cache;

import org.apache.log4j.Logger;
import org.springframework.security.core.codec.Hex;
import org.springframework.util.DigestUtils;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple object cache that holds an object for a specified time in memory (INVALID_AFTER)
 **/

public class ObjectCache implements Serializable
{
	private static final long serialVersionUID = -4289955073669895813L;
	private static final int 	MAX_ENTRIES = 5;
	private static final long 	INVALID_AFTER = 30 * 60 * 1000;
	private static final Logger LOGGER = Logger.getLogger(ObjectCache.class);
	
	private Map<String, CacheEntry>	cache;
	
	/**
	 * Add report to cache
	 * @return
	 */
	public String addObjectToCache(CachableObject cacheObject)
	{
		initCache();
		
		checkCacheForAddition();
		
		
		return addCacheEntryToCache(cacheObject);
	}
	
	private String addCacheEntryToCache(CachableObject object)
	{
		CacheEntry	entry = new CacheEntry(object);

		String id = createId();
		
		object.setCacheId(id);
		
		cache.put(id, entry);
		
		return id;
	}

	/**
	 * Check cache for stale entries
	 */
	public void checkCache()
	{
		if (cache != null)
		{
			long expireBefore = new Date().getTime() - INVALID_AFTER;
			
			for (String id : cache.keySet())
			{
				CacheEntry cacheEntry = cache.get(id);
				
				if (cacheEntry.addedTimestamp < expireBefore)
				{
					LOGGER.info("Removing id " + id + " from cache");
					cache.remove(id);
				}
			}
		}
	}
	
	/**
	 * Check the cache and remove the oldest entry if MAX_ENTRIES reached
	 */
	private void checkCacheForAddition()
	{
        if (cache != null && cache.size() >= MAX_ENTRIES) {
            List<CacheEntry> entries = new ArrayList<CacheEntry>(cache.values());
            Collections.sort(entries);

            String idToDelete = entries.get(0).cachedObject.getCacheId();

            LOGGER.debug("Removing oldest entry with id " + idToDelete);

            cache.remove(idToDelete);
        }
	}
	
	/**
	 * Get report from cache
	 * @return
	 */
	public CachableObject getObjectFromCache(String cacheId)
	{
		initCache();
		
		LOGGER.debug("Retrieving report from object with id " + cacheId);
		
		if (!cache.containsKey(cacheId))
		{
			LOGGER.error("Cache doesn't contain report with id " + cacheId);
			throw new IllegalArgumentException("Cache doesn't contain object");
		}
		else
		{
			return cache.get(cacheId).cachedObject;
		}
	}
	
	/**
	 * Create id based on current timestamp
	 * @return
	 */
	private String createId()
	{
		byte[]	shaTimestamp;
		
		shaTimestamp = DigestUtils.md5Digest(Long.toString(new Date().getTime()).getBytes());
		
		return new String(Hex.encode(shaTimestamp));
	}
	
	/**
	 * Init cache
	 */
	private void initCache()
	{
		if (cache == null)
		{
			cache = new ConcurrentHashMap<String, CacheEntry>();
		}
	}
	
	private class CacheEntry implements Comparable<CacheEntry>, Serializable
	{
		private static final long serialVersionUID = 1L;
		private long addedTimestamp;
		private CachableObject cachedObject;
		
		CacheEntry(CachableObject cachedObject)
		{
			this.cachedObject = cachedObject;
			this.addedTimestamp = new Date().getTime();
			
		}
		
		public int compareTo(CacheEntry o)
		{
			return (int)(addedTimestamp - o.addedTimestamp);
		}
	}
}
