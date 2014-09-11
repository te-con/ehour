package com.richemont.windchill;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.EhourUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wt.httpgw.GatewayAuthenticator;
import wt.log4j.LogR;
import wt.method.MethodAuthenticator;
import wt.method.RemoteMethodServer;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.*;

@Service("chillService")
public class WindChillServiceImpl implements WindChillService
{
    @Autowired
    private AggregateReportService aggregateReportService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectAssignmentService projectAssignmentService;

    @Autowired
    private ProjectAssignmentDao projectAssignmentDao;

    private static Logger logger = LogR.getLogger("ext.service.WindchillServiceImpl");
    private static Level levelDeTrace;


    // can be moved to log4j.properties
    static
    {
        try
        {
            // Initialisation log4j
            // WTProperties props =
            // RichemontProperties.getRichemontProperties();
            String TempLevelDeTrace = "debug";
            levelDeTrace = org.apache.log4j.Level.toLevel(TempLevelDeTrace);
            logger.setLevel(levelDeTrace);
            logger.info("ext.service.WindchillServiceImpl loade");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void checkUserExist(String username)
    {
        // Create user if it doesn't exist
        logger.debug("On vérifie si le user existe :" + username);
        User user = userService.getUser(username);

        if (user == null)
        {
            logger.debug("Le user " + username + " n'existe pas");
            User newUser = new User(username, "ism");
            newUser.setUpdatedPassword("ism");
            newUser.setLastName(username);

            UserDepartment userDepartment = new UserDepartment(1);
            newUser.setUserDepartment(userDepartment);

            Set<UserRole> userRoles = new HashSet<UserRole>();
            userRoles.add(UserRole.CONSULTANT);
            userRoles.add(UserRole.ADMIN);
            newUser.setUserRoles(userRoles);
            newUser.setActive(true);

            try
            {
                logger.debug("On persiste le user :" + username);
                userService.persistUser(newUser);
            } catch (PasswordEmptyException e)
            {
                logger.error(e.getMessage());
                logger.error(e.getStackTrace());
            } catch (ObjectNotUniqueException e)
            {
                logger.error(e.getMessage());
                logger.error(e.getStackTrace());
            }
        }
    }

    @Override
    @SuppressWarnings({})
    @Transactional
    public void initDataForUser(String username, HttpServletRequest request)
    {
        logger.info("***** Initialisation des donnees de l'utilisateur " + username + "*****");
        User user = userService.getUser(username);
        // definition du server RMI
        MethodAuthenticator auth = new GatewayAuthenticator(request);
        logger.debug("On recupere le MS distant");
        RemoteMethodServer remotemethodserver = RemoteMethodServer.getDefault();
        auth.setServer(remotemethodserver);
        logger.debug("On authentifie le MS");
        remotemethodserver.setAuthenticator(auth);

        // Create project and activities for this user.
        Vector listeDesActiviteesDepuisGrutlink;
        List<ProjectAssignment> listeDesActiviteesDepuisEhour;
        List<Customer> listeDesProjetsDepuisEhour;

        Class[] argTypes = {String.class};
        Object[] args2 = {username};

        // HashMap h;
        try
        {

            logger.debug("On recupere le liste des activitées depuis ehour");
            listeDesActiviteesDepuisEhour = getProjectsFromEhour(user);
            logger.debug("Nombre des activitées :" + listeDesActiviteesDepuisEhour.size());
            listeDesProjetsDepuisEhour = getCustomersFromEhour();
            logger.debug("Nombre des projets :" + listeDesProjetsDepuisEhour.size());

            // recuperation de toutes les activitees du user
            logger.debug("Appel de la methode getProjectActivity en RMI");
            listeDesActiviteesDepuisGrutlink = (Vector) remotemethodserver.invoke("getProjectActivity", "ext.ismts.ProjectConnection", null, argTypes, args2);
            logger.debug("Liste des activitées récupérées depuis projectlink " + listeDesActiviteesDepuisGrutlink.size());


            // on cree une liste des projets recuperes depuis projectlink au
            // format ehour
            List<Customer> customersFromProjLink = getCustomers(listeDesActiviteesDepuisGrutlink);
            Iterator<Customer> it_customersFromProjLink = customersFromProjLink.iterator();

            List<Customer> newProjetct = new ArrayList<Customer>();

            while (it_customersFromProjLink.hasNext())// liste depuis Grut
            {
                Customer customer = it_customersFromProjLink.next();
                Iterator<Customer> it_listeDesProjets = listeDesProjetsDepuisEhour.iterator();
                int nb = 0;
                while (it_listeDesProjets.hasNext())
                {// Depuis EHour
                    Customer projet = it_listeDesProjets.next();
                    if (!((customer.getCode()).equals((projet.getCode()))))
                    {
                        nb = nb + 1;

                    } else
                    {
                        if (projet.getName().equals(customer.getName()))
                        {
                            projet.setName(customer.getName());
                        }

                        projet.setDescription(customer.getDescription());

                    }
                }
                if (nb == (listeDesProjetsDepuisEhour.size()))
                {
                    newProjetct.add(customer);
                }
            }
            Date debut = new Date("01/01/2001");
            Date fin = new Date("01/01/2999");
            DateRange daterange = new DateRange(debut, fin);
            createCustomers(customersFromProjLink);// a remettre en ordre a la fin
            List<Project> projectsFromProjLink = getActivities(listeDesActiviteesDepuisGrutlink, customersFromProjLink);
            List<Project> projects = createActivities(projectsFromProjLink);
            List<ProjectAssignment> projectAssignmentFromProjLink = getProjectAssignments(listeDesActiviteesDepuisGrutlink, user, projects);
            createProjectAssignment(projectAssignmentFromProjLink);
            DesactivateActivities(listeDesActiviteesDepuisGrutlink, projectAssignmentService.getProjectAssignmentsForUser(user.getUserId(), daterange));
            logger.info("***** Fin de l'initialisation des donnees *****");
        } catch (RemoteException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void createProjectAssignment(List<ProjectAssignment> projectAssignments)
    {
        try
        {
            for (ProjectAssignment projectAssignment : projectAssignments)
            {
                projectAssignmentDao.persist(projectAssignment);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private List<Project> createActivities(List<Project> projects)
    {
        List<Project> persistedProjects = new ArrayList<Project>();
        try
        {
            for (Project project : projects)
            {
                persistedProjects.add(projectService.persistProject(project));
            }

            return persistedProjects;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private List<Customer> getCustomers(Vector v)
    {
        HashMap h;
        ArrayList<Customer> customers = new ArrayList<Customer>();
        for (int i = 0; i < v.size(); i++)
        {
            h = (HashMap) v.elementAt(i);
            Customer customer = new Customer((String) h.get("ProjectId"), (String) h.get("ProjectName"), (String) h.get("ProjectDescription"), true);
            logger.debug("Creation du projet " + h.get("ProjectId") + " " + h.get("ProjectName") + " " + h.get("ProjectDescription"));
            customers.add(customer);
        }
        return customers;
    }

    private void createCustomers(List<Customer> customers)
    {
        try
        {
            for (Customer customer : customers)
            {
                customerService.persistCustomer(customer);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getCustomersFromEhour()
    {
        List<Customer> projs = null;
        try
        {
            projs = customerService.getCustomers();
            if (logger.isDebugEnabled())
            {
                for (Customer proj : projs)
                {
                    Customer customer = proj;
                    logger.debug("Projet : " + customer.getFullName());
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return projs;
    }

    @Override
    public List<ProjectAssignment> getProjectsFromEhour(User user)
    {
        List<ProjectAssignment> projs = null;

        // TODO: projectService.getAllProjectsForUser(user); is unknown
//        try
//        {
//            projs = projectService.getAllProjectsForUser(user);
//
//            if (logger.isDebugEnabled())
//            {
//                ProjectAssignment assignment = null;
//                Iterator<ProjectAssignment> liste_projets = projs.iterator();
//                while (liste_projets.hasNext())
//                {
//                    assignment = liste_projets.next();
//                    logger.debug("tache : " + assignment.getFullName() + " - date de debut " + assignment.getDateStart() + " - date de fin " + assignment.getDateEnd() + " duree " + assignment.getAllottedHours());
//                }
//            }
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        return projs;
    }

    @Override
    public List<ProjectAssignment> getAssignmentsFromEhours(int projectID, User userID)
    {
        List<ProjectAssignment> listeDesAssignements = null;
//        try
//        {
//            // listeDesAssignements =
//            // projectAssignmentService.getProjectAssignmentOnUserAndProject(projectID,
//            // userID);
//
//            Date debut = new Date("01/01/2001");
//            Date fin = new Date("01/01/2999");
//            DateRange daterange = new DateRange(debut, fin);
//
        // TODO UNKNOWN METHOD
//            // listeDesAssignements =
//            // projectAssignmentService.getProjectAssignmentsForUser(userID,
//            // daterange);
//            listeDesAssignements = projectService.getAllProjectsForUser(userID);
//        } catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        return listeDesAssignements;
    }

    @Override
    public Float getBookedHours(ProjectAssignment assignement, User user)
    {
        List<AssignmentAggregateReportElement> aggregates = new ArrayList<AssignmentAggregateReportElement>();
        Date debut = new Date("01/01/2001");
        Date fin = new Date("01/01/2999");
        DateRange daterange = new DateRange(debut, fin);
    // TODO unknown method
//        aggregates = aggregateReportService.getHoursPerAssignmentInRange(user.getUserId(), daterange);
//        Iterator<AssignmentAggregateReportElement> assi = aggregates.iterator();
        Float retour = null;
//        while (assi.hasNext())
//        {
//            AssignmentAggregateReportElement current = assi.next();
//            if ((current.getProjectAssignment().getFullName()).equals(assignement.getFullName()))
//            {
//                retour = (Float) current.getHours();
//            }
//        }
        return retour;
    }

    private List<ProjectAssignment> getProjectAssignments(Vector v, User user, List<Project> projects)
    {
        ArrayList<ProjectAssignment> projectAssignments = new ArrayList<ProjectAssignment>();
        List<Project> projects2 = projects;
        // List<ProjectAssignment> projectAssignments =
        // projectAssignmentService.getProjectAssignmentOnUserAndProject(projects.getProjectId(),
        // user.getUserId());
        HashMap h;
        for (int i = 0; i < v.size(); i++)
        {
            h = (HashMap) v.elementAt(i);
            boolean trackEffort = false;
            if ((String) h.get("isTrackEffort") != null && ((String) h.get("isTrackEffort")).equals("1"))
            {
                trackEffort = true;
            }
            Date startDate = (Date) h.get("startDate");
            Date endDate = (Date) h.get("endDate");
            String work_str = (String) h.get("work");
            Float work;
            if (work_str != null)
            {
                work = new Float(work_str);
            } else
            {
                work = new Float(1);
            }
            Iterator<Project> it_projet = projects2.iterator();
            ProjectAssignment projectAssignment = new ProjectAssignment();
            while (it_projet.hasNext())
            {
                Project current = it_projet.next();
                if (current.getProjectCode().equals((String) h.get("ActivityId")))
                {
                    projectAssignment = ProjectAssignment.createProjectAssignment(current, user);

                    if (trackEffort)
                    {
                        // ProjectAssignmentType assignmentType = new
                        // ProjectAssignmentType(0);
                        // assignmentType.setAssignmentType("ASSIGNMENT_DATE");
                        ProjectAssignmentType assignmentType = new ProjectAssignmentType(2);
                        assignmentType.setAssignmentType("ASSIGNMENT_TIME_ALLOTTED_FIXED");
                        projectAssignment.setAssignmentType(assignmentType);
                        projectAssignment.setAllottedHours(work);
                        projectAssignment.setDateStart(startDate);
                        projectAssignment.setDateEnd(endDate);
                        logger.debug("on cree un assignement entre " + user + " et " + current.getFullName());
                        logger.debug("date debut :" + startDate + " date fin " + endDate + " duree " + work);

                    } else
                    {
                        ProjectAssignmentType assignmentType = new ProjectAssignmentType(2);
                        assignmentType.setAssignmentType("ASSIGNMENT_TIME_ALLOTTED_FIXED");
                        projectAssignment.setAssignmentType(assignmentType);
                        projectAssignment.setDateStart(startDate);
                        projectAssignment.setDateEnd(endDate);
                        projectAssignment.setAllottedHours(work);
                        logger.debug("on cree un assignement entre " + user + " et " + current.getFullName());
                        logger.debug("date debut :" + startDate + " date fin " + endDate + " duree " + work);
                    }
                    if (projectAssignment.getAllottedHours() != new Float(0))
                    {
                        projectAssignments.add(projectAssignment);
                    }
                }
            }
        }
        return projectAssignments;
    }

    private Customer createProject(String code, String name)
    {
        if (!StringUtils.isBlank(name) && !StringUtils.isBlank(code))
        {
            Customer customerFromCode = null;
            // TODO unknown method
//            Customer customerFromCode = customerService.getCustomer(code);
            if (customerFromCode != null)
            {
                if (!customerFromCode.getName().equals(name))
                {
                    customerFromCode.setName(name);
                    try
                    {
                        // customerService.updateCustomer(customerFromCode);
                        // //le project importé existe déjà, mais son nom est
                        // changé
                        return customerFromCode;
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else
                {
                    return customerFromCode; // le project importé exite déjà
                }
                // avec le même nom, on ne fait
                // rien
            } else
            {
                Customer newCustomer = new Customer(code, name, "", true);

                // customerService.persistCustomer(newCustomer); //le project
                // importé n'existe pas dans TS
                return newCustomer;
            }
        }
        return null;
    }

    private List<Project> getActivities(Vector v, List<Customer> customers)
    {
        ArrayList<Project> activities = new ArrayList<Project>();
        for (int i = 0; i < v.size(); i++)
        {
            HashMap h = (HashMap) v.elementAt(i);
            String code = (String) h.get("ActivityId");
            String name = (String) h.get("ActivityName");
            String desc = (String) h.get("ActivityDescription");
            String prop = (String) h.get("projectManager");
            Iterator<Customer> it_customer = customers.iterator();
            String hours = "";
            while (it_customer.hasNext())
            {
                Customer customer = it_customer.next();
                if ((customer.getCode()).equals((String) h.get("ProjectId")))
                {
                    Project project;
                    if (customer != null)
                    {
                        Set<Project> projects = customer.getProjects();
                        boolean isProjectExist = false;
                        if (projects != null)
                        {
                            for (Project proj : projects)
                            {
                                if (proj.getProjectCode().equals(code))
                                {
                                    isProjectExist = true;
                                    Set<ProjectAssignment> projectassign = proj.getProjectAssignments();
                                    Iterator<ProjectAssignment> it_assignment = projectassign.iterator();
                                    while (it_assignment.hasNext())
                                    {
                                        ProjectAssignment current = it_assignment.next();
                                        if (current.getProject().getProjectCode().equals(proj.getProjectCode()))
                                        {
                                            // if(!current.getDateEnd().equals((Date)h.get("endDate")))
                                            current.setDateEnd((Date) h.get("endDate"));
                                            // if(!current.getDateStart().equals((String)h.get("startDate")))
                                            current.setDateStart((Date) h.get("startDate"));
                                            hours = current.getAllottedHours().toString();
                                        }
                                    }
                                    if ((proj.getDescription() == null) || (!proj.getDescription().equals(desc)))
                                    {
                                        proj.setDescription(desc);
                                    }
                                    if (!proj.getName().equals(name))
                                    {
                                        proj.setName(name + "(" + hours + ")");
                                        activities.add(proj); // projectService.persistProject(proj);
                                        // //modifier le
                                        // nom de la
                                        // tâche

                                    } else
                                    {
                                        activities.add(proj); // la tâche existe
                                    }
                                    // déjà, on ne
                                    // fait rien

                                } else
                                {
                                    ;
                                }
                            }
                        }
                        if (!isProjectExist)
                        {
                            project = new Project();
                            project.setName(name + "(" + hours + ")");
                            ;
                            project.setDescription(desc);
                            project.setProjectCode(code);
                            project.setProjectManager(userService.getUser(prop));
                            project.setCustomer(customer);
                            project.setActive(true);
                            // projectService.persistProject(project); // créer
                            // une nouvelle tâche avec le projet affecté
                            activities.add(project);
                        }

                    } else
                    { // enregistre la tâche sans lui affecter le projet
                        project = new Project();
                        project.setName(name);
                        project.setProjectCode(code);
                        project.setDescription(desc);
                        // projectService.persistProject(project);
                        activities.add(project);
                    }
                }
            }
        }
        return activities;
    }

    private void DesactivateActivities(Vector v, List<ProjectAssignment> assignments)
    {

        Iterator<ProjectAssignment> it_assignement = assignments.iterator();
        while (it_assignement.hasNext())
        {
            ProjectAssignment current = it_assignement.next();
            int nb = 0;
            for (int i = 0; i < v.size(); i++)
            {
                HashMap h = (HashMap) v.elementAt(i);
                if (current.getProject().getProjectCode().equals((String) h.get("ActivityId")))
                {
                    nb = nb + 1;
                }
            }
            if (nb == 0)
            {
                current.setActive(false);
                current.getProject().setActive(false);
                current.getProject().setDefaultProject(false);
                current.setNotifyPm(false);
                current.setDeletable(true);
                int test = current.getProject().getProjectId();
                try
                {
                    projectAssignmentDao.delete(current.getAssignmentId());
                    projectService.deleteProject(test);
                } catch (ParentChildConstraintException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void updateAssignments(String username, HttpServletRequest r)
    {
        logger.info("***** Envoie des heures du planning vers projectLink *****");
        Vector<HashMap> result = new Vector<HashMap>();
        User user = userService.getUser(username);
        // definition du server RMI
        MethodAuthenticator auth = new GatewayAuthenticator(r);
        RemoteMethodServer remotemethodserver = RemoteMethodServer.getDefault();
        if (auth != null)
        {
            auth.setServer(remotemethodserver);
        }
        remotemethodserver.setAuthenticator(auth);
        try
        {
            logger.debug("try");
            List<Customer> listeDesProjetsDepuisEhour = getCustomersFromEhour();
            Iterator<Customer> it_listeDesProjets = listeDesProjetsDepuisEhour.iterator();
            Customer customer = it_listeDesProjets.next();
            ArrayList<ProjectAssignment> listeDesAssignements = (ArrayList<ProjectAssignment>) getAssignmentsFromEhours(customer.getCustomerId(), user);
            Iterator<ProjectAssignment> it_listeDesAssignements = listeDesAssignements.iterator();
            List<Serializable> assignmentIds = new ArrayList<Serializable>();

            assignmentIds.addAll(EhourUtil.getIdsFromDomainObjects(user.getProjectAssignments()));
            List<AssignmentAggregateReportElement> aggregates = new ArrayList<AssignmentAggregateReportElement>();
            while (it_listeDesAssignements.hasNext())
            {
                ProjectAssignment assignement = it_listeDesAssignements.next();
                HashMap<String, String> h1 = new HashMap<String, String>();
                h1.put("ActivityId", assignement.getProject().getProjectCode());// assignement.getAssignementCode()
                if (assignement.getProject() != null && listeDesAssignements.size() > 0)
                {
                    // TODO Unknown method
//                    aggregates.addAll(projectService.gethours(assignement.getProject()));
                    if (aggregates.size() > 0)
                    {
                        Iterator<AssignmentAggregateReportElement> it_aggregate = aggregates.iterator();
                        while (it_aggregate.hasNext())
                        {
                            AssignmentAggregateReportElement current = it_aggregate.next();
                            if ((current.getProjectAssignment().getProject().getProjectCode()).equals(assignement.getProject().getProjectCode()))
                            {
                                h1.put("ActivityTime", current.getHours().toString());
                                logger.debug("Ajout de l'activite :" + assignement.getProject().getProjectCode() + "avec le temps :" + current.getHours().toString());
                            }

                        }
                    }
                }
                result.add(h1);
            }

            Class[] argTypes = {Vector.class, String.class};
            Object[] args2 = {result, user.getUsername()};
            Vector v = (Vector) remotemethodserver.invoke("updateProjectActivity", "ext.ismts.ProjectConnection", null, argTypes, args2);
            return;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
