package net.rrm.ehour.service.project.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.rrm.ehour.domain.ProjectAssignment;

/**
 * A ProjectAssignment collection is a set of unique assignments with multiple views.
 * You can either get the assignments or get a set of user id's involved in the assignments
 * 
 * Created on Feb 12, 2010 2:39:38 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ProjectAssignmentCollection
{
	private final List<List<ProjectAssignment>> assignments;
	
	public ProjectAssignmentCollection()
	{
		assignments = new ArrayList<List<ProjectAssignment>>();
	} 
	
	public void addProjectAssignment(ProjectAssignment assignment)
	{
		boolean wasFound = addToExistingAssignments(assignment);
		
		if (!wasFound)
		{
			ArrayList<ProjectAssignment> newAssignments = new ArrayList<ProjectAssignment>();
			newAssignments.add(assignment);
			assignments.add(newAssignments);
		}
	}
	
	public List<List<ProjectAssignment>> getAssignments()
	{
		return assignments;
	}
	
	public Set<Integer> getUserIds()
	{
		Set<Integer> userIds = new HashSet<Integer>();
		
		for (List<ProjectAssignment> assignmentList : assignments)
		{
			for (ProjectAssignment projectAssignment : assignmentList)
			{
				userIds.add(projectAssignment.getUser().getUserId());
			}
		}
		
		return userIds;
	}

	private boolean addToExistingAssignments(ProjectAssignment assignment)
	{
		boolean wasFound = false;
		outer: for (List<ProjectAssignment> assignmentList : assignments)
		{
			for (ProjectAssignment projectAssignment : assignmentList)
			{
				if (projectAssignment.equalsIgnoringUser(assignment))
				{
					assignmentList.add(assignment);
					wasFound = true;
					break outer;
				}
			}
		}
		
		return wasFound;
	}
}

