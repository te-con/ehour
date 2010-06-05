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

package net.rrm.ehour.report.service;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.dao.DetailedReportDao;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.util.EhourUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Report service for detailed reports implementation
 **/
@Service("detailedReportService")
public class DetailedReportServiceImpl extends AbstractReportServiceImpl<FlatReportElement>
										implements DetailedReportService
{
	@Autowired
	private	DetailedReportDao	detailedReportDAO;

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.DetailedReportService#getDetailedReportData(net.rrm.ehour.report.criteria.ReportCriteria)
	 */
	public ReportData getDetailedReportData(ReportCriteria reportCriteria)
	{
		return getReportData(reportCriteria);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.AbstractReportServiceImpl#getReportElements(java.util.List, java.util.List, net.rrm.ehour.data.DateRange)
	 */
	@Override
	protected List<FlatReportElement> getReportElements(List<User> users,
														List<Project >projects,
														DateRange reportRange)
	{
		List<FlatReportElement>	elements = new ArrayList<FlatReportElement>();
		
		if (users == null && projects == null)
		{
			elements = detailedReportDAO.getHoursPerDay(reportRange);
		}
		else if (projects == null && users != null)
		{
			elements = detailedReportDAO.getHoursPerDayForUsers(EhourUtil.getIdsFromDomainObjects(users), reportRange);
		}
		else if (projects != null && users == null)
		{
			elements = detailedReportDAO.getHoursPerDayForProjects(EhourUtil.getIdsFromDomainObjects(projects), reportRange);
		}
		else
		{
			elements = detailedReportDAO.getHoursPerDayForProjectsAndUsers(EhourUtil.getIdsFromDomainObjects(projects),
																			EhourUtil.getIdsFromDomainObjects(users),
																			reportRange);
		}
		
		return elements;
	}

	/**
	 * @param detailedReportDAO the detailedReportDAO to set
	 */
	public void setDetailedReportDAO(DetailedReportDao detailedReportDAO)
	{
		this.detailedReportDAO = detailedReportDAO;
	}
}
