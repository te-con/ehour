package net.rrm.ehour.ui.timesheet.dto

import java.lang.Iterable
import java.util

import net.rrm.ehour.domain.Project

import scala.collection.JavaConversions._
import scala.collection.convert.WrapAsJava
import scala.collection.immutable
import scala.collection.mutable.{ListBuffer, Map => MutableMap}

protected object TimesheetProjects {
  def apply(rows: util.List[TimesheetRow]): TimesheetProjects = {

    val projects = MutableMap[Project, ListBuffer[TimesheetRow]]()

    rows.foreach(f => {
      val project = f.getActivity.getProject
      val projectRows = projects.getOrElse(project, new ListBuffer[TimesheetRow]())
      projectRows += f
      projects.put(project, projectRows)
    })

    val projectMap = for (prj <- projects.toArray) yield (prj._1, prj._2.toList)

    def compare(a: (Project, _), b: (Project, _)) = a._1.getName < b._1.getName

    new TimesheetProjects(projectMap.toSeq.sortWith(compare).toMap)
  }
}

class TimesheetProjects(projectMap: Map[Project, immutable.List[TimesheetRow]]) extends Serializable {
  type Predicate[T] = (T => Boolean)

  def get(): util.List[Project] = {
    new util.ArrayList(projectMap.keySet
      .toList
      .sortWith((a: Project, b: Project) => a.getName.compareToIgnoreCase(b.getName) < 0)
      )
  }

  def getTimesheetRow(project: Project): util.List[TimesheetRow] = {
    val rows = projectMap.getOrElse(project, List())

    rows.sortWith((a: TimesheetRow, b: TimesheetRow) => a.getActivity.getName.compareToIgnoreCase(b.getActivity.getName) < 0)
  }

  def getTimesheetRows: Iterable[util.List[TimesheetRow]] = projectMap.toSeq.map(p => WrapAsJava.seqAsJavaList(p._2))
}