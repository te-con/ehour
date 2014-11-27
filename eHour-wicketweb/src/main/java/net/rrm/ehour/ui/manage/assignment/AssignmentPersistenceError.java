package net.rrm.ehour.ui.manage.assignment;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class AssignmentPersistenceError {
    private AjaxRequestTarget target;

    public AssignmentPersistenceError(AjaxRequestTarget target) {
        this.target = target;
    }

    public AjaxRequestTarget getTarget() {
        return target;
    }
}
