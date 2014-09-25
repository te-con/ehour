package net.rrm.ehour.ui.timesheet.dto

import net.rrm.ehour.domain.Project
import scalaj.collection.Imports._
import collection.immutable.TreeMap

import collection.mutable.{ListBuffer, Map => MutableMap}
import java.lang.Iterable
import java.util.{ArrayList => JavaArrayList, List => JavaList}

protected object TimesheetProjects {
  def apply(rows: JavaList[TimesheetRow]): TimesheetProjects = {

    val projects = MutableMap[Project, ListBuffer[TimesheetRow]]()

    rows.foreach(f => {
      val project = f.getActivity.getProject
      val projectRows = projects.getOrElse(project, new ListBuffer[TimesheetRow]())
      projectRows += f
      projects.put(project, projectRows)
    })

    val projectMap = for (prj <- projects.toArray) yield (prj._1, prj._2.toList.asJava)

    val activityOrder = new Ordering[Project] {
      def compare(a: Project, b: Project) = a.getName.compareToIgnoreCase(b.getName)
    }

    new TimesheetProjects(TreeMap(projectMap: _*)(activityOrder))
  }
}

class TimesheetProjects(projectMap: Map[Project, JavaList[TimesheetRow]]) extends Serializable {
  type Predicate[T] = (T => Boolean)


  def get(activityNameFilter: Option[String] = None): JavaList[Project] = {
    val name = activityNameFilter.getOrElse("").toLowerCase
    
    println("filtering get on " + name)

    val activityNamePredicate: Predicate[Project] = (project: Project) =>
      project.getActivities.asScala.find(_.getName.toLowerCase contains name).isDefined

    new JavaArrayList(projectMap.keySet.filter(activityNamePredicate).toList.sortWith((a: Project, b: Project) => a.getName.compareToIgnoreCase(b.getName) < 0) asJava)
  }

  def getTimesheetRow(project: Project, activityNameFilter: Option[String] = None): JavaList[TimesheetRow] = {

    val name = activityNameFilter.getOrElse("").toLowerCase
    println("filtering getTimesheetRow on " + name)

    val rows = projectMap.getOrElse(project, new JavaArrayList[TimesheetRow]).asScala

    rows.filter(_.getActivity.getName.toLowerCase contains name)
      .sortWith((a: TimesheetRow, b: TimesheetRow) => a.getActivity.getName.compareToIgnoreCase(b.getActivity.getName) < 0)
      .asJava
  }

  def getTimesheetRows: Iterable[JavaList[TimesheetRow]] = projectMap.values.asJava
}