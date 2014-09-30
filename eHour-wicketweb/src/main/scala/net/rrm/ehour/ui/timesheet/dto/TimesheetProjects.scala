package net.rrm.ehour.ui.timesheet.dto

import java.lang.Iterable
import java.util
import java.util.{ArrayList => JavaArrayList, List => JavaList}

import net.rrm.ehour.domain.{Activity, Project}
import scala.collection.JavaConversions._
import scala.collection.immutable.TreeMap

protected object TimesheetProjects {
  def apply(rows: JavaList[TimesheetRow]): TimesheetProjects = {

    val xs: Seq[TimesheetRow] = rows

    val activities = xs.map(_.getActivity)
    val projectActivityMap: Map[Project, Seq[Activity]] = activities.groupBy(_.getProject)

    for (project <- projectActivityMap) yield {
      val activities: JavaList[Activity] = project._2
      project._1.setActivities(new util.HashSet(activities))
    }

    val projects = xs.groupBy(_.getActivity.getProject)

    val projectMap: Array[(Project, Seq[TimesheetRow])] = for (prj <- projects.toArray) yield (prj._1, prj._2)

    val projectOrder = new Ordering[Project] {
      def compare(a: Project, b: Project) = a.getName.compareToIgnoreCase(b.getName)
    }

    new TimesheetProjects(TreeMap(projectMap: _*)(projectOrder))
  }
}

class TimesheetProjects(projectMap: Map[Project, JavaList[TimesheetRow]]) extends Serializable {
  type Predicate[T] = (T => Boolean)

  def get(): JavaList[Project] = {
    new JavaArrayList(projectMap.keySet
      .toList
      .sortWith((a: Project, b: Project) => a.getName.compareToIgnoreCase(b.getName) < 0)
      )
  }

  def getTimesheetRow(project: Project): JavaList[TimesheetRow] = {

    val rows = projectMap.getOrElse(project, new JavaArrayList[TimesheetRow])
    rows.sortWith((a: TimesheetRow, b: TimesheetRow) => a.getActivity.getName.compareToIgnoreCase(b.getActivity.getName) < 0)
  }

  def getTimesheetRows: Iterable[JavaList[TimesheetRow]] = projectMap.values
}