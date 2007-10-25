/**
 * Created on Oct 20, 2007
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

package net.rrm.ehour.report.dao;

import java.io.Serializable;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate impl of report per month 
 **/

public class ReportPerMonthDAOHibernateImpl extends HibernateDaoSupport implements ReportPerMonthDAO
{

	public List<FlatProjectAssignmentAggregate> getHoursPerDayForAssignment(List<Integer> assignmentId, DateRange dateRange)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignment(DateRange dateRange)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignmentForProjects(List<Serializable> projectId, DateRange dateRange)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignmentForUsers(List<Serializable> userId, List<Serializable> projectId, DateRange dateRange)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public List<FlatProjectAssignmentAggregate> getHoursPerMonthPerAssignmentForUsers(List<Serializable> userId, DateRange dateRange)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
