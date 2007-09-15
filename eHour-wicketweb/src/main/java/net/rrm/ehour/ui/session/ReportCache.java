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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.rrm.ehour.ui.report.Report;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

/**
 * Simple report cache that holds report for a specified time in memory
 * TODO Switch to ehcache someday
 * FIXME add refresh
 **/

public class ReportCache
{
	private final static int 	MAX_ENTRIES = 5;
	private final static long 	INVALID_AFTER = 30 * 60 * 1000;
	private final static Logger logger = Logger.getLogger(ReportCache.class);
	
	private Map<String, CacheEntry>	cache;
	
	/**
	 * 
	 */
	public ReportCache()
	{
		logger.info("ReportCache instantiated");
	}
	
	/**
	 * Add report to cache
	 * @param report
	 * @return
	 */
	public String addReportToCache(Report report)
	{
		initCache();
		
		CacheEntry	entry = new CacheEntry();
		entry.addedTimestamp = new Date().getTime();
		entry.report = report;
		
		String id = createId();
		
		report.setReportId(id);
		
		cache.put(id, entry);
		
		logger.debug("Report cached with id " + id);
		
		return id;
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
		
		return cache.get(id).report;
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
	
	private class CacheEntry
	{
		private long	addedTimestamp;
		private Report	report;
	}
}
