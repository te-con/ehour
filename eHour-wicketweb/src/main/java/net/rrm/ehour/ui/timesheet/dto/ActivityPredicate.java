package net.rrm.ehour.ui.timesheet.dto;

import net.rrm.ehour.domain.Activity;

public class ActivityPredicate {
    private final String filter;

    public ActivityPredicate(String filter) {
        this.filter = filter;
    }

    public boolean matches(Activity activity) {
        return activity.getName().toLowerCase().indexOf(filter.toLowerCase()) > 0;
    }
}
