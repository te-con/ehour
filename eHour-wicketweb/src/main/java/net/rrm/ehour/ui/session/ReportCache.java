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

package net.rrm.ehour.ui.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.Report;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

/**
 * Simple report cache that holds report for a specified time in memory
 * TODO Switch to ehcache someday
 * FIXME add refresh
 **/

public class ReportCache implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4289955073669895813L;
	private final static int 	MAX_ENTRIES = 5;
	private final static long 	INVALID_AFTER = 30 * 60 * 1000;
	private final static Logger logger = Logger.getLogger(ReportCache.class);
	
	private Map<String, CacheEntry>	cache;
	
	/**
	 * 
	 */
	public ReportCache()
	{
		logger.debug("ReportCache instantiated");
	}
	
	/**
	 * Add report to cache
	 * @param report
	 * @return
	 */
	public String addReportToCache(Report report, ReportData<?> reportData)
	{
		initCache();
		
		checkCacheForAddition();
		
		CacheEntry	entry = new CacheEntry();
		entry.addedTimstamp= new Date().getTime();
		entry.report = report;
		entry.reportData = reportData;
		
		String id = createId();
		
		report.setReportId(id);
		
		cache.put(id, entry);
		
		logger.debug("Report cached with id " + id);
		
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
					logger.info("Removing id " + id + " from cache");
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
				
				String idToDelete = entries.get(0).report.getReportId();
				
				logger.debug("Removing oldest entry with id " + idToDelete);
				
				cache.remove(idToDelete);
			}
		}		
	}
	
	/**
	 * Get report from cache
	 * @param id
	 * @return
	 */
	public Report getReportFromCache(String id)
	{
		initCache();
		
		logger.debug("Retrieving report from cache with id " + id);
		
		if (!cache.containsKey(id))
		{
			logger.error("Cache doesn't contain report with id " + id);
			return null;
		}
		else
		{
			return cache.get(id).report;
		}
	}
	
	/**
	 * Get report from cache
	 * @param id
	 * @return
	 */
	public ReportData<? extends ReportElement> getReportDataFromCache(String id)
	{
		initCache();
		
		logger.debug("Retrieving report data from cache with id " + id);
		
		if (!cache.containsKey(id))
		{
			logger.error("Cache doesn't contain report data with id " + id);
			return null;
		}
		else
		{
			return cache.get(id).reportData;
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
			cache = new HashMap<String, CacheEntry>();
		}
	}
	
	private class CacheEntry implements Comparable<CacheEntry>, Serializable
	{
		private static final long serialVersionUID = 1L;
		private long		addedTimstamp;
		private Report		report;
		private ReportData<? extends ReportElement>	reportData;
		
		public int compareTo(CacheEntry o)
		{
			return (int)(addedTimstamp - o.addedTimstamp);
		}
	}
}
