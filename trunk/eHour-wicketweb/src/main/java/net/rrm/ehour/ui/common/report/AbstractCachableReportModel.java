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

package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.common.cache.CachableObject;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Type interface for reports 
 **/

public abstract class AbstractCachableReportModel extends LoadableDetachableModel implements CachableObject, Report 
{
	private static final long serialVersionUID = -8583320436270110287L;
	private String cacheId;
	private ReportCriteria criteria;

	public AbstractCachableReportModel(ReportCriteria criteria)
	{
		this.criteria = criteria;
	}
	
	public DateRange getReportRange()
	{
		return criteria != null ? criteria.getReportRange() : null;
	}
	
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
	
	/**
	 * @return the criteria
	 */
	public ReportCriteria getReportCriteria()
	{
		return criteria;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected final Object load()
	{
		return getReportData();
	}
	
	public ReportData getReportData()
	{
		return getReportData(getReportCriteria());
	}
	
	/**
	 * @return the reportData
	 */
	protected abstract ReportData getReportData(ReportCriteria reportCriteria);
}
