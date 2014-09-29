package net.rrm.ehour.domain;


public class ActivityMother {
    public static Activity createActivity(User user, Project project) {
        Activity activity = new Activity();
        activity.setAssignedUser(user);
        activity.setProject(project);

        activity.setActive(true);
        activity.setId(1);
        activity.setCode("1");
        activity.setAllottedHours(100.0f);

        return activity;
    }

    /**
     * @param baseIds baseId[0] = baseId, baseId[1] = customerId, baseId[2] = userId
     *                baseIds[3] = projectId, baseId[4] = assignmentId
     * @return
     */
    public static Activity createActivity(int... baseIds) {
        Activity activity;
        Project prj;
        Customer cust;
        User user;
        int customerId, userId, projectId, activityId;

        int baseId = baseIds[0];

        customerId = baseId;
        userId = baseId;
        projectId = baseId * 10;
        activityId = baseId * 100;

        if (baseIds.length >= 2) {
            customerId = baseIds[1];
            userId = customerId;
        }

        if (baseIds.length >= 3) {
            userId = baseIds[2];
        }

        if (baseIds.length >= 4) {
            projectId = baseIds[3];
        }

        if (baseIds.length >= 5) {
            activityId = baseIds[4];
        }


        cust = CustomerObjectMother.createCustomer(customerId);

        prj = ProjectObjectMother.createProject(projectId, cust);

        activity = new Activity();
        activity.setProject(prj);
        activity.setId(activityId);
        activity.setCode(String.valueOf(activityId));
        activity.setAllottedHours((float) activityId);
        activity.setAvailableHours((float) activityId);
        activity.setLocked(Boolean.FALSE);

        prj.addActivity(activity);

		user = UserObjectMother.createUser();
		user.setUserId(userId);
        user.setName("last name " + userId);
		
		activity.setAssignedUser(user);
		activity.setActive(true);
		
		return activity;
	}
}
