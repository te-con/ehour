package net.rrm.ehour.project.dao;

import java.util.Calendar;
import java.util.List;

public interface ProjectDAO
{
	public List	getProjectsForUserBetweenDates(Integer userId, Calendar dateStart, Calendar dateEnd);
}
