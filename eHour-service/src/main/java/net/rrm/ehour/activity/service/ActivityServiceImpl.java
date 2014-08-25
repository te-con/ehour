package net.rrm.ehour.activity.service;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.activity.status.ActivityStatusService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.persistence.activity.dao.ActivityDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

	@Autowired
	private ActivityDao activityDao;

	@Autowired
	private ActivityStatusService activityStatusService;
	
	@Override
	public Activity getActivity(Integer activityid) throws ObjectNotFoundException {
		Activity result = activityDao.findById(activityid);
		if (result == null) {
			throw new ObjectNotFoundException("No Activity found for id : " + activityid);
		}
		return result;
	}

	@Override
	@Transactional
	public Activity persistActivity(Activity activity) {
		return activityDao.persist(activity);
	}

	@Override
	@Transactional
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

	@Override
	public List<Activity> getActivities() {
		return activityDao.findAll();
	}

	@Override
	public List<Activity> getActivitiesForUser(Integer userId, DateRange dateRange) {
		List<Activity> validActivities = new ArrayList<Activity>();
		
		List<Activity> allActivities = activityDao.findActivitiesForUser(userId, dateRange);
		
		for (Activity activity : allActivities) {
			if (activityStatusService.getActivityStatus(activity, dateRange).isActivityBookable()) {
				validActivities.add(activity);
			}
			
		}
		return validActivities;
	}

	@Override
	public List<Activity> getActivities(Project project, DateRange dateRange) {
		return activityDao.findActivitiesOfProject(project, dateRange);
	}

}
