package net.rrm.ehour.report.service;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;

import java.util.List;

public class UsersAndProjects {
    private List<User> users;
    private List<Project> projects;

    public UsersAndProjects() {
        this(Lists.<User>newArrayList(), Lists.<Project>newArrayList());
    }

    public UsersAndProjects(List<User> users, List<Project> projects) {
        this.users = users;
        this.projects = projects;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
