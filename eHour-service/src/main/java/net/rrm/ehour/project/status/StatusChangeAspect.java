package net.rrm.ehour.project.status;

import net.rrm.ehour.domain.ProjectAssignment;

import org.apache.log4j.Logger;
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
	private	Logger				logger = Logger.getLogger(StatusChangeAspect.class);

	
	/**
	 * Determine status change
	 * @param point
	 * @param assignment
	 * @throws Throwable
	 */
	@Around("@annotation(net.rrm.ehour.project.status.StatusChanger) && args(assignment,..)")
	public void determineStatusChange(ProceedingJoinPoint point, ProjectAssignment assignment) throws Throwable
	{
		ProjectAssignmentStatus beforeStatus = projectAssignmentStatusService.getAssignmentStatus(assignment);

		logger.debug("got old status: " + beforeStatus);
		point.proceed();
		
		ProjectAssignmentStatus afterStatus = projectAssignmentStatusService.getAssignmentStatus(assignment);
		logger.debug("got new status: " + afterStatus);
		logger.debug("equal: " + afterStatus.equals(beforeStatus));
		//TODO add mail
	}

	/**
	 * @param projectAssignmentStatusService the projectAssignmentStatusService to set
	 */
	public void setProjectAssignmentStatusService(ProjectAssignmentStatusService projectAssignmentStatusService)
	{
		this.projectAssignmentStatusService = projectAssignmentStatusService;
	}
}
