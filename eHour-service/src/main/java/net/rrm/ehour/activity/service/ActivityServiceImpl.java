package net.rrm.ehour.activity.service;

import java.util.List;

import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.activity.dao.ActivityDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityDao activityDao;
	
	@Override
	public Activity persistActivity(Activity activity) {
		return activityDao.persist(activity);
	}

	@Override
	public void deleteActivity(Integer activityId) {
		Activity retrievedActivity = activityDao.findById(activityId);
		retrievedActivity.setActive(Boolean.FALSE);
		activityDao.merge(retrievedActivity);
	}

	@Override
	public List<Activity> getAllActivitiesForProject(Project project) {
		return activityDao.findAllActivitiesOfProject(project);
	}

	@Override
	public List<Activity> getAllActivitiesForUser(User assignedUser) {
		return activityDao.findAllActivitiesOfUser(assignedUser);
	}
}
