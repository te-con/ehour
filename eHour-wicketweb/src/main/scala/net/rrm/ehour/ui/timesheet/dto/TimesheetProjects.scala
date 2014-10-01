package net.rrm.ehour.ui.timesheet.dto

import java.lang.Iterable
import java.util

import net.rrm.ehour.domain.{Activity, Project}

import scala.collection.JavaConversions._
import scala.collection.convert.WrapAsJava
import scala.collection.mutable

protected object TimesheetProjects {
  def apply(rows: util.List[TimesheetRow]): TimesheetProjects = {

    // get all projects defined in provided activities
    val activities = rows.map(_.getActivity)
    val projectActivityMap: Map[Project, Seq[Activity]] = activities.groupBy(_.getProject)

    // group the activities back in the projects. There might have been multiple instances of the same project, this
    // makes sure that there is only instance per project having a reference to all its activities
    for (project <- projectActivityMap) yield {
      val activities: util.List[Activity] = project._2
      project._1.setActivities(new util.HashSet(activities))
    }

    // get all projects the timesheet rows, again unique
    val projects: Map[Project, mutable.Buffer[TimesheetRow]] = rows.groupBy(_.getActivity.getProject)

    // construct
    val projectMap: Array[(Project, util.List[TimesheetRow])] = for (prj <- projects.toArray) yield (prj._1, WrapAsJava.seqAsJavaList(prj._2))

//    val projectOrder = new Ordering[Project] {
//      def compare(a: Project, b: Project) = a.getName.compareToIgnoreCase(b.getName)
//    }
// TODO re-add orderin
//    val map: TreeMap[Project, util.List[TimesheetRow]] = TreeMap(projectMap: _*)
    new TimesheetProjects(projectMap.toSeq.toMap)
  }
}

class TimesheetProjects(projectMap: Map[Project, util.List[TimesheetRow]]) extends Serializable {
  type Predicate[T] = (T => Boolean)

  def get(): util.List[Project] = {
    new util.ArrayList(projectMap.keySet
      .toList
      .sortWith((a: Project, b: Project) => a.getName.compareToIgnoreCase(b.getName) < 0)
      )
  }

  def getTimesheetRow(project: Project): util.List[TimesheetRow] = {

    val rows = projectMap.getOrElse(project, new util.ArrayList[TimesheetRow])
    rows.sortWith((a: TimesheetRow, b: TimesheetRow) => a.getActivity.getName.compareToIgnoreCase(b.getActivity.getName) < 0)
  }

  def getTimesheetRows: Iterable[util.List[TimesheetRow]] = WrapAsJava.asJavaCollection(projectMap.values)
}