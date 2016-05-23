package net.rrm.ehour.domain;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created on Feb 7, 2010 3:07:54 PM
 *
 * @author thies (www.te-con.nl)
 */
public class ProjectObjectMother {
    private ProjectObjectMother() {
    }

    public static Set<Project> createProjects(int projectCount) {
        Set<Project> projects = Sets.newHashSet();

        for (int i = 0; i < projectCount; i++) {
            projects.add(createProject(i));
        }

        return projects;
    }

    public static Project createProject(Integer id) {
        return createProject(id, CustomerObjectMother.createCustomer());
    }

    public static Project createProject(Integer id, Customer customer) {
        Project project = new Project(id);
        project.setActive(true);
        project.setProjectCode("aa" + id);
        project.setName("aa" + id);
        project.setCustomer(customer);
        customer.addProject(project);
        return project;
    }
}
