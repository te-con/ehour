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

package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.common.cache.CachableObject;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Type interface for reports 
 **/

public abstract class AbstractCachableReportModel extends LoadableDetachableModel<ReportData> implements CachableObject, Report 
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
	protected final ReportData load()
	{
		return getReportData(getReportCriteria());
	}
	
	public final ReportData getReportData()
	{
		return (ReportData)getObject();
	}
	
	/**
	 * @return the reportData
	 */
	protected abstract ReportData getReportData(ReportCriteria reportCriteria);
}
