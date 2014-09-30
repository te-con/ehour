package com.richemont.jira;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.project.service.ProjectService;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author laurent.linck
 */
@Component
public class EhourProjectutils {

    @Autowired
    private static ProjectService projectService;

    private static Logger LOGGER = Logger.getLogger("com.richemont.jira.EhourProjectHelper");
    static {
        LOGGER.setLevel(Level.DEBUG);
    }

    /**
     * §
     * @return
     */
    public static List<Project> getAllProjects() {
        List<Project> allProjects;
        allProjects = projectService.getProjects();
        return allProjects;
    }

    /**
     * Tous les projets e-Hour
     * @return
     */
    /**
     public HashMap<String, Project> getAllProjectsByCode() {
     HashMap<String, Project> HmAllProjectsByCode = new HashMap<String, Project>();
     Iterator<Project> it = getAllProjects().iterator();
     Project aProject;
     while (it.hasNext()) {
     aProject = (Project) it.next();
     HmAllProjectsByCode.put(aProject.getProjectCode(), aProject);
     }
     return HmAllProjectsByCode;
     }
     **/

    /**
     *
     * @param allProjects
     */
    public static void displayAllProjects(List<Project> allProjects) {
        Iterator<Project> it = allProjects.iterator();
        Project aProject;
        LOGGER.debug("displayAllProjects:");
        while (it.hasNext()) {
            aProject = (Project) it.next();
            LOGGER.debug("\t" + aProject.getFullName());
        }
    }

    /**
     *
     * @param projectCode
     * @param hmAllProjectsByCode
     * @return
     */
    public static Project isProjectExist(String projectCode,
                                         HashMap<String, Project> hmAllProjectsByCode) {
        if (hmAllProjectsByCode.containsKey(projectCode))
            return hmAllProjectsByCode.get(projectCode);
        return null;
    }


    /**
     *
     * @param projectCode
     * @param projectName
     * @param customerCode
     * @param customerName
     * @param hmAllCustomerByCode
     * @return
     */
    public static Project createProject(String projectCode, String projectName,
                                        String customerCode, String customerName,
                                        HashMap<String, Customer> hmAllCustomerByCode) {
        Project newProject = new Project();
/*        Customer customer = EhourCustomerHelper.isCustomerExist(customerCode, hmAllCustomerByCode);
        newProject.setProjectCode(projectCode);
        newProject.setName(projectName);
        if (customer == null) {
            customer = EhourCustomerHelper.createCustomer(customerCode, customerName, hmAllCustomerByCode);
        }
        newProject.setCustomer(customer);
        projectService.persistProject(newProject);*/
        return newProject;
    }

    /**
     *
     * @param projectCode
     * @param newProjectName
     * @return
     */
    public static Project checkProject (String projectCode, String newProjectName){
        Project prj = projectService.getProject(projectCode);
        if ( prj == null){

        } else {
            // on s assure que le projet est actif
            LOGGER.debug("\tActive projet: " + prj.getName() );
            prj.setActive(true);

            // verifie si le projet PJl a ete renomme depuis la derniere mise a jour dans e-Hour
            if (! newProjectName.equals( prj.getName() ) ) {
                LOGGER.debug("\tMise a jour du nom du projet: " + prj.getName() + " --> " + newProjectName );
                prj.setName(newProjectName);
            }

            LOGGER.debug("\tproject name=ok");
            prj = projectService.updateProject(prj);
        }
        return prj;
    }

}
