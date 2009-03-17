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

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
/**
 * TODO 
 **/

public class ObjectCacheTest
{

	@Test
	public void testAddObjectToCache()
	{
		ObjectCache reportCache = new ObjectCache();

		MockReport report = new MockReport();
		long id = report.id;
		String cacheId = reportCache.addObjectToCache(report);

		report = null;
		
		report = (MockReport)reportCache.getObjectFromCache(cacheId);
		
		assertEquals(id, report.id);
	}
	
	class MockReport implements CachableObject
	{
		private static final long serialVersionUID = 1L;
		long id = new Date().getTime();
		private String cacheId;
		/**
		 * @return the cacheId
		 */
		public String getCacheId()
		{
			return cacheId;
		}
		/**
		 * @param cacheId the cacheId to set
		 */
		public void setCacheId(String cacheId)
		{
			this.cacheId = cacheId;
		}

		
	}
}
