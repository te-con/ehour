package net.rrm.ehour.persistence.project.dao

import java.util

import net.rrm.ehour.domain.{Customer, Project, User}
import net.rrm.ehour.persistence.dao.GenericDao

/**
 * CRUD on project domain object
 *
 * @author Thies
 */
trait ProjectDao extends GenericDao[Integer, Project] {
  /**
   * Get all projects
   */
  def findAll(): util.List[Project]

  /**
   * Get all active projects
   */
  def findAllActive(): util.List[Project]

  /**
   * Get all active default projects
   */
  def findDefaultProjects(): util.List[Project]

  /**
   * Get projects for customer respecting the active flag
   */
  def findProjectForCustomers(customers: util.List[Customer], onlyActive: Boolean): util.List[Project]

  /**
   * Find projects where user is projectmanager
   */
  def findActiveProjectsWhereUserIsPM(user: User): util.List[Project]

  /**
   * Find all projects which have a defined projectmanager
   */
  def findAllProjectsWithPmSet(): util.List[Project]
}

