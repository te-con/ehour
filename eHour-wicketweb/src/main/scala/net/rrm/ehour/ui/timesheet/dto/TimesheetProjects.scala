package net.rrm.ehour.ui.timesheet.dto

import net.rrm.ehour.domain.Project
import scalaj.collection.Imports._
import java.util.{ArrayList => JavaArrayList, List => JavaList}
import collection.immutable.TreeMap

import collection.mutable.{ListBuffer, Map => MutableMap}

object TimesheetProjects {
  def apply(rows: JavaList[TimesheetRow]): TimesheetProjects = {

    val projects = MutableMap[Project,  ListBuffer[TimesheetRow]]()

    rows.foreach(f => {
      val project = f.getActivity.getProject
      val projectRows = projects.getOrElse(project, new ListBuffer[TimesheetRow]())
      projectRows += f
      projects.put(project, projectRows)
    })
    
    val projectMap = for (prj <- projects.toArray) yield {
      (prj._1, prj._2.toList.sortBy(_.getActivity.getProject.getName).asJava)
    }

    new TimesheetProjects(TreeMap(projectMap: _*))
  }

}

class TimesheetProjects(projectMap: Map[Project,JavaList[TimesheetRow]]) {
  def setPredicate(predicate: ActivityPredicate): Unit = {
    //    this.predicate = predicate
  }
  
  def get(): JavaList[Project] = new JavaArrayList(projectMap.keySet.asJava)

  def getTimesheetRow(project: Project): JavaList[TimesheetRow] = projectMap.getOrElse(project, new JavaArrayList[TimesheetRow])

  def getTimesheetRows = projectMap.values.asJava
}