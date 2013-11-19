package net.rrm.ehour.report.service

import net.rrm.ehour.domain.{User, Project, Customer}
import java.{util => ju}
import scala.collection.convert.WrapAsScala

object ProjectFilter {
  type ProjectPredicate = (Project) => Boolean

  val activePredicate: ProjectPredicate = (p: Project) => p.isActive

  val billablePredicate: ProjectPredicate = (p: Project) => p.isBillable

  def pmPredicate(user: User): ProjectPredicate = (p: Project) => p.getProjectManager != null && p.getProjectManager.equals(user)

  def filter(customers: List[Customer], predicate: ProjectPredicate): List[Customer] = {
    val projects = customers.flatMap(c => WrapAsScala.asScalaSet(c.getProjects))
    projects.filter(predicate)

    val filteredProjects = projects.filter(predicate)

    filteredProjects.map(p => p.getCustomer)
  }
}
