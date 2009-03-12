/**
 * Created on Sep 15, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.common.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

/**
 * Simple object cache that holds an object for a specified time in memory (INVALID_AFTER)
 * TODO Switch to ehcache someday
 * FIXME add refresh
 **/

public class ObjectCache implements Serializable
{
	private static final long serialVersionUID = -4289955073669895813L;
	private final static int 	MAX_ENTRIES = 5;
	private final static long 	INVALID_AFTER = 30 * 60 * 1000;
	private final static Logger LOGGER = Logger.getLogger(ObjectCache.class);
	
	private Map<String, CacheEntry>	cache;
	
	/**
	 * Add report to cache
	 * @param report
	 * @return
	 */
	public String addObjectToCache(CachableObject cacheObject, Object o)
	{
		return "";
	}
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
	 * TODO add Spring timer
	 */
	public void checkCache()
	{
		if (cache != null)
		{
			long expireBefore = new Date().getTime() - INVALID_AFTER;
			
			for (String id : cache.keySet())
			{
				CacheEntry cacheEntry = cache.get(id);
				
				if (cacheEntry.addedTimstamp < expireBefore)
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
		if (cache != null)
		{
			if (cache.size() >= MAX_ENTRIES)
			{
				List<CacheEntry> entries = new ArrayList<CacheEntry>(cache.values());
				Collections.sort(entries);
				
				String idToDelete = entries.get(0).cachedObject.getCacheId();
				
				LOGGER.debug("Removing oldest entry with id " + idToDelete);
				
				cache.remove(idToDelete);
			}
		}		
	}
	
	/**
	 * Get report from cache
	 * @param id
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
		
		shaTimestamp = DigestUtils.sha(Long.toString(new Date().getTime()));
		
		return new String(Hex.encodeHex(shaTimestamp));
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
		private long		addedTimstamp;
		private CachableObject cachedObject;
		
		CacheEntry(CachableObject cachedObject)
		{
			this.cachedObject = cachedObject;
			this.addedTimstamp = new Date().getTime();
			
		}
		
		public int compareTo(CacheEntry o)
		{
			return (int)(addedTimstamp - o.addedTimstamp);
		}
	}
}
