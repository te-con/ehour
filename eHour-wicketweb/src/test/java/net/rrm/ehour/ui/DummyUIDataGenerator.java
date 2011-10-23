package net.rrm.ehour.ui;

import net.rrm.ehour.domain.*;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.util.EhourConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DummyUIDataGenerator
{
	public static Project createProject()
	{
		Project prj = new Project(1);
		prj.setCustomer(createCustomer());
		prj.setActive(true);
		prj.setName("eHour");
		
		return prj;
	}

	public static Customer createCustomer()
	{
		Customer cust = new Customer(1);
		cust.setActive(true);
		cust.setName("TE-CON");
		
		return cust;
	}
	
	public static List<ProjectAssignmentType> getProjectAssignmentTypes()
	{
		List<ProjectAssignmentType> list = new ArrayList<ProjectAssignmentType>();
		
		list.add(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));
		
		return list;
	}
	
	public static ProjectAssignment getProjectAssignment(int... baseIds)
	{
		ProjectAssignment	prjAsg;
		Project				prj;
		Customer			cust;
		User				user;
		int					customerId, userId, projectId, assignmentId;
		
		int baseId = baseIds[0];
		
		customerId = baseId;
		userId = baseId;
		projectId = baseId * 10;
		assignmentId = baseId * 100;
		
		if (baseIds.length >= 2)
		{
			customerId = baseIds[1];
			userId = customerId;
		}

		if (baseIds.length >= 3)
		{
			userId = baseIds[2];
		}
		
		if (baseIds.length >= 4)
		{
			projectId = baseIds[3];
		}		

		if (baseIds.length >= 5)
		{
			assignmentId = baseIds[4];
		}		

		
		cust = getCustomer(customerId);
		
		prj = new Project(projectId);
		prj.setCustomer(cust);
		prj.setActive(true);
		prj.setName("tralala" + baseId);
		
		prjAsg = new ProjectAssignment();
		prjAsg.setProject(prj);
		prjAsg.setAssignmentId(assignmentId);
		
		user = getUser();
		user.setUserId(userId);
		
		prjAsg.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));

		prjAsg.setUser(user);
		prjAsg.setActive(true);
		
		return prjAsg;
	}
	
	public static Customer getCustomer(int customerId)
	{
		Customer cust = new Customer(customerId);
		cust.setName(customerId + "");
		cust.setActive(true);
		
		return cust;
	}

	public static AssignmentAggregateReportElement getProjectAssignmentAggregate(int baseId, int customerId, int userId)
	{
		AssignmentAggregateReportElement pag = new AssignmentAggregateReportElement();
		pag.setHours(baseId);
		pag.setProjectAssignment(getProjectAssignment(baseId, customerId, userId));
		return pag;
	}
	
	public static User getUser()
	{
		User user = new User();
		
		user.setActive(true);
		user.setEmail("thies@te-con.nl");
		user.setUsername("testmetoo");
		user.setFirstName("Dummy");
		user.setLastName("TestUser");
		user.setPassword("abc");
		
		user.setUserDepartment(new UserDepartment(1));
		
		user.setUserRoles(Arrays.asList(UserRole.ADMIN));
		
		return user;
	}
	
	public static List<User> getUserList()
	{
		ArrayList<User> list = new ArrayList<User>();
		list.add(getUser());
		
		return list;
	}
	
	public static TimesheetEntry getTimesheetEntry(int prjId, Date date, float hours)
	{
		TimesheetEntry 	entry;
		TimesheetEntryId id;
		
		id = new TimesheetEntryId();
		id.setEntryDate(date);
		id.setProjectAssignment(getProjectAssignment(prjId, prjId, 1));
		
		entry = new TimesheetEntry();
		entry.setEntryId(id);
		entry.setHours(hours);
		
		return entry;
	}
	

}
