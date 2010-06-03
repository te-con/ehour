package net.rrm.ehour.project.dto;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentMother;
import net.rrm.ehour.domain.ProjectMother;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserMother;
import net.rrm.ehour.service.project.dto.ProjectAssignmentCollection;

import org.junit.Before;
import org.junit.Test;


/**
 * Created on Feb 12, 2010 2:54:48 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ProjectAssignmentCollectionTest
{
	
	private ArrayList<ProjectAssignment> assignments;
	private ProjectAssignmentCollection collection;

	@Before
	public void setup()
	{
		assignments = new ArrayList<ProjectAssignment>();
		
		collection = new ProjectAssignmentCollection();
	}
	
	@Test
	public void createTwoProjectsForThreeUsers()
	{
		User userA = UserMother.createUser();
		userA.setUserId(1);
		User userB = UserMother.createUser();
		userB.setUserId(2);
		User userC = UserMother.createUser();
		userC.setUserId(3);
		
		Project pA = ProjectMother.createProject(1);
		Project pB = ProjectMother.createProject(2);
		
		assignments.add(ProjectAssignmentMother.createProjectAssignment(userA, pA));
		assignments.add(ProjectAssignmentMother.createProjectAssignment(userB, pA));
		assignments.add(ProjectAssignmentMother.createProjectAssignment(userC, pB));

		int id = 1;
		
		for (ProjectAssignment assignment : assignments)
		{
			assignment.setAssignmentId(id++);
		}
		
		for (ProjectAssignment assignment : assignments)
		{
			collection.addProjectAssignment(assignment);
		}
		
		assertEquals(2, collection.getAssignments().size());
		assertEquals(3, collection.getUserIds().size());
	}
	
	@Test
	public void createThreeProjectsForTwoUsers()
	{
		User userA = UserMother.createUser();
		userA.setUserId(1);
		User userB = UserMother.createUser();
		userB.setUserId(2);
		
		Project pA = ProjectMother.createProject(1);
		Project pB = ProjectMother.createProject(2);
		Project pC = ProjectMother.createProject(3);
		
		assignments.add(ProjectAssignmentMother.createProjectAssignment(userA, pA));
		assignments.add(ProjectAssignmentMother.createProjectAssignment(userB, pB));
		assignments.add(ProjectAssignmentMother.createProjectAssignment(userA, pC));

		int id = 1;
		
		for (ProjectAssignment assignment : assignments)
		{
			assignment.setAssignmentId(id++);
		}
		
		for (ProjectAssignment assignment : assignments)
		{
			collection.addProjectAssignment(assignment);
		}
		
		assertEquals(3, collection.getAssignments().size());
		assertEquals(2, collection.getUserIds().size());
	}	
}
