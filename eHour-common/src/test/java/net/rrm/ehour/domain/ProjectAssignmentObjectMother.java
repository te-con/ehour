package net.rrm.ehour.domain;

import net.rrm.ehour.util.EhourConstants;

/**
 * Created on Feb 7, 2010 2:47:38 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ProjectAssignmentObjectMother
{
	private ProjectAssignmentObjectMother() {
	}

	public static ProjectAssignment createProjectAssignment(User user, Project project)
	{
		ProjectAssignment assignment = new ProjectAssignment(user, project);
		assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));
		assignment.setActive(true);
		assignment.setAssignmentId(1);
		
		return assignment;
	}
	
	/**
	 * 
	 * 
	 * @param baseIds baseId[0] = baseId, baseId[1] = customerId, baseId[2] = userId
	 * 			baseIds[3] = projectId, baseId[4] = assignmentId
	 * @return
	 */
	public static ProjectAssignment createProjectAssignment(int... baseIds)
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

		
		cust = CustomerObjectMother.createCustomer(customerId);
		
		prj = ProjectObjectMother.createProject(projectId, cust);
		
		prjAsg = new ProjectAssignment();
		prjAsg.setProject(prj);
		prjAsg.setAssignmentId(assignmentId);
		
		user = UserObjectMother.createUser();
		user.setUserId(userId);
		
		prjAsg.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));

		prjAsg.setUser(user);
		prjAsg.setActive(true);
		
		return prjAsg;
	}
}
