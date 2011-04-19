package net.rrm.ehour.ui.report.util;

import net.rrm.ehour.ui.common.cache.CachableObject;
import net.rrm.ehour.ui.common.session.EhourWebSession;

public class ReportUtil
{
	private ReportUtil()
	{
		
	}
	
	/**
	 * keep the report available for excel reporting in a later request
	 * @param report
	 */
	public static void storeInCache(CachableObject report)
	{
		EhourWebSession.getSession().getObjectCache().addObjectToCache(report);
	}
}
