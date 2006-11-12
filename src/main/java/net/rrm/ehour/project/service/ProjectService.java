package net.rrm.ehour.project.service;

import java.util.Calendar;
import java.util.List;

public interface ProjectService
{
	public List getActiveProjectsForUser(Integer userId, Calendar dateStart, Calendar dateEnd);
}
