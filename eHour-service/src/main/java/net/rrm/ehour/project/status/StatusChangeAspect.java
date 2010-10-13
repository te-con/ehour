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
