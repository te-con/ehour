package net.rrm.ehour.project.status;

import net.rrm.ehour.domain.ProjectAssignment;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 
 * @author Thies
 *
 */
@Aspect
public class StatusChangeAspect
{
	private ProjectAssignmentStatusService projectAssignmentStatusService;
	
	/**
	 * Determine status change
	 * @param point
	 * @param assignment
	 * @throws Throwable
	 */
	@Around("@annotation(net.rrm.ehour.project.status.StatusChanger) && args(assignment,..)")
	public void determineStatusChange(ProceedingJoinPoint point, ProjectAssignment assignment) throws Throwable
	{
		System.out.println("start on " + assignment.getAssignmentId());
		point.proceed();
		
		System.out.println("end");
	}

	/**
	 * @param projectAssignmentStatusService the projectAssignmentStatusService to set
	 */
	public void setProjectAssignmentStatusService(ProjectAssignmentStatusService projectAssignmentStatusService)
	{
		this.projectAssignmentStatusService = projectAssignmentStatusService;
	}
}
