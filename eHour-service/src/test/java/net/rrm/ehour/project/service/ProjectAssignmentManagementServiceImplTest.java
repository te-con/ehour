package net.rrm.ehour.project.service;

import java.util.GregorianCalendar;

import net.rrm.ehour.AbstractServiceTest;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ProjectAlreadyAssignedException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class ProjectAssignmentManagementServiceImplTest extends AbstractServiceTest
{
	@Autowired
	private	ProjectAssignmentManagementService projectAssignmentManagementService;

	@Test	
	public void testAssignUserToProjectSuccess() throws ProjectAlreadyAssignedException
	{
		ProjectAssignment pa = new ProjectAssignment();
		
		pa.setAssignmentId(1);
		pa.setAssignmentType(new ProjectAssignmentType(0));
		Project prj = new Project(1);
		User user = new User(1);
		
		pa.setDateStart(new GregorianCalendar(2006, 1, 1).getTime());
		pa.setDateEnd(new GregorianCalendar(2006, 2, 1).getTime());
		pa.setProject(prj);
		pa.setUser(user);
		
		projectAssignmentManagementService.assignUserToProject(pa);
	}
}
