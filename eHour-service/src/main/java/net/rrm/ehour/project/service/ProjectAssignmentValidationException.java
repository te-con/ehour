package net.rrm.ehour.project.service;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class ProjectAssignmentValidationException extends Exception {
    public enum Issue {
        EXISTING_DATA_BEFORE_START,
        EXISTING_DATA_AFTER_END
    }

    private List<Issue> issues;

    public ProjectAssignmentValidationException(List<Issue> issues) {
        this.issues = issues;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(issues).toString();
    }
}
