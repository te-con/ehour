/**
 * Created on Nov 4, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.MailLogAssignment;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.ProjectManagerDashboard;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

/**
 * Provides reporting services on timesheets. 
 * @author Thies
 *
 */

public class AggregateReportServiceImpl extends AbstractReportServiceImpl<AssignmentAggregateReportElement> 
										implements AggregateReportService 
{
	private	ReportAggregatedDAO	reportAggregatedDAO;
	private	MailService			mailService;
	private	ProjectAssignmentService	projectAssignmentService;
	
	private	Logger				logger = Logger.getLogger(this.getClass());
	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getHoursPerAssignment(java.lang.Integer[])
	 */
	public List<AssignmentAggregateReportElement> getHoursPerAssignment(List<Serializable> projectAssignmentIds)
	{
		return reportAggregatedDAO.getCumulatedHoursPerAssignmentForAssignments(projectAssignmentIds);
	}
	
	/**
	 * Get the booked hours per project assignment for a date range
	 * @param userId
	 * @param
	 * @return 
	 */		
	public List<AssignmentAggregateReportElement> getHoursPerAssignmentInRange(Integer userId, DateRange dateRange)
	{
		List<AssignmentAggregateReportElement>	assignmentAggregateReportElements;

		List<User>	users = new ArrayList<User>();
		users.add(new User(userId));
		assignmentAggregateReportElements = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(users, dateRange);

		return assignmentAggregateReportElements;
	}	
	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.AggregateReportService#getAggregateReportData(net.rrm.ehour.report.criteria.ReportCriteria)
	 */
	public ReportData getAggregateReportData(ReportCriteria criteria)
	{
		return getReportData(criteria);
	}	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.AbstractReportServiceImpl#getReportElements(java.util.List, java.util.List, net.rrm.ehour.data.DateRange)
	 */
	@Override
	protected List<AssignmentAggregateReportElement> getReportElements(List<User> users,
																			List<Project >projects,
																			DateRange reportRange)
	{
		List<AssignmentAggregateReportElement>	aggregates = new ArrayList<AssignmentAggregateReportElement>();
		
		if (users == null && projects == null)
		{
			aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignment(reportRange);
		}
		else if (projects == null && users != null)
		{
			if (!CollectionUtils.isEmpty(users))
			{
				aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(users, reportRange);
			}
		}
		else if (projects != null && users == null)
		{
			if (!CollectionUtils.isEmpty(projects))
			{
				aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(projects, reportRange);
			}
		}
		else
		{
			if (!CollectionUtils.isEmpty(users) && !CollectionUtils.isEmpty(projects))
			{
				aggregates = reportAggregatedDAO.getCumulatedHoursPerAssignmentForUsers(users, projects, reportRange);
			}
		}
		
		return aggregates;
	}
	
	
	/**
	 * Get weekly project report
	 * @param 
	 * @return
	 */
//	private List<FlatProjectAssignmentAggregate> getWeeklyReportData(List<User> users,
//																	 List<Project> projects,
//																		DateRange reportRange)
//	{
//		List<FlatProjectAssignmentAggregate> aggregates = null;
//
//		if (users == null && projects == null)
//		{
//			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignment(reportRange);
//		}
//		else if (projects == null && users != null)
//		{		
//			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForUsers(EhourUtil.getPKsFromDomainObjects(users), reportRange);
//		}
//		else if (projects != null && users == null)
//		{
//			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForProjects(EhourUtil.getPKsFromDomainObjects(projects), reportRange);
//		}
//		else
//		{
//			aggregates = reportPerMonthDAO.getHoursPerMonthPerAssignmentForUsers(EhourUtil.getPKsFromDomainObjects(users),
//																					EhourUtil.getPKsFromDomainObjects(projects), reportRange);
//		}	
//		
//		return aggregates;
//	}	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.ReportService#getProjectManagerReport(net.rrm.ehour.data.DateRange, java.lang.Integer)
	 */
	public ProjectManagerReport getProjectManagerDetailedReport(DateRange reportRange, Integer projectId)
	{
		ProjectManagerReport	report = new ProjectManagerReport();
		SortedSet<AssignmentAggregateReportElement>	aggregates;
		List<MailLogAssignment>	sentMail;
		Project					project;
		List<Integer>			assignmentIds = new ArrayList<Integer>();
		List<ProjectAssignment>	allAssignments;
		AssignmentAggregateReportElement	emptyAggregate;
		
		// get the project
		project = getProjectDAO().findById(projectId);
		report.setProject(project);
		logger.debug("PM report for project " + project.getName());
		
		// get a proper report range
		reportRange = getReportRangeForProject(reportRange, project);
		report.setReportRange(reportRange);
		
		// get all aggregates
		List<Project>	projects = new ArrayList<Project>();
		projects.add(new Project(projectId));
		aggregates = new TreeSet<AssignmentAggregateReportElement>(reportAggregatedDAO.getCumulatedHoursPerAssignmentForProjects(projects, reportRange));

		// filter out just the id's
		for (AssignmentAggregateReportElement aggregate : aggregates)
		{
			assignmentIds.add(aggregate.getProjectAssignment().getAssignmentId());
		}
		
		// get all assignments for this period regardless whether they booked hours on it
		allAssignments = projectAssignmentService.getProjectAssignments(project, reportRange);
		
		for (ProjectAssignment assignment : allAssignments)
		{
			if (!assignmentIds.contains(assignment.getAssignmentId()))
			{
				emptyAggregate = new AssignmentAggregateReportElement();
				emptyAggregate.setProjectAssignment(assignment);
			
				aggregates.add(emptyAggregate);	
			}
		}
		
		report.setAggregates(aggregates);
		

		// get mail sent for this project
		if (assignmentIds.size() > 0)
		{
			sentMail = mailService.getSentMailForAssignment((Integer[])assignmentIds.toArray(new Integer[assignmentIds.size()]));
			report.setSentMail(new TreeSet<MailLogAssignment>(sentMail));
		}
		
		report.deriveTotals();
		
		return report;
	}
	
	/**
	 * Get report range for project
	 * @param reportRange
	 * @param project
	 * @return
	 */
	private DateRange getReportRangeForProject(DateRange reportRange, Project project)
	{
		DateRange	minMaxRange;
		
		if (reportRange.getDateStart() == null || reportRange.getDateEnd() == null)
		{
			minMaxRange = reportAggregatedDAO.getMinMaxDateTimesheetEntry(project);
			
			if (reportRange.getDateStart() == null)
			{
				reportRange.setDateStart(minMaxRange.getDateStart());
			}

			if (reportRange.getDateEnd() == null)
			{
				reportRange.setDateEnd(minMaxRange.getDateEnd());
			}
		}
		
		logger.debug("Used date range for pm report: " + reportRange + " on report " + project);
		
		return reportRange;
	}
	
	
	/**
	 *  
	 *
	 */
	public void setReportAggregatedDAO(ReportAggregatedDAO reportAggregatedDAO)
	{
		this.reportAggregatedDAO = reportAggregatedDAO;
	}

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService)
	{
		this.mailService = mailService;
	}

	/**
	 * @param projectAssignmentService the projectAssignmentService to set
	 */
	public void setProjectAssignmentService(ProjectAssignmentService projectAssignmentService)
	{
		this.projectAssignmentService = projectAssignmentService;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.report.service.AggregateReportService#getProjectManagerDashboard(net.rrm.ehour.domain.User)
	 */
	public ProjectManagerDashboard getProjectManagerDashboard(User user)
	{
//		projectSer
		// TODO Auto-generated method stub
		return null;
	}
}
