package net.rrm.ehour.ui.common.menu

import net.rrm.ehour.ui.timesheet.page.MonthOverviewPage
import org.apache.wicket.PageParameters
import net.rrm.ehour.ui.timesheet.export.ExportMonthSelectionPage
import net.rrm.ehour.ui.report.page.GlobalReportPage
import net.rrm.ehour.ui.pm.page.ProjectManagement
import net.rrm.ehour.ui.admin.user.page.UserAdmin
import net.rrm.ehour.ui.admin.customer.page.CustomerAdmin
import net.rrm.ehour.ui.admin.project.page.ProjectAdmin
import net.rrm.ehour.ui.admin.config.page.MainConfigPage
import net.rrm.ehour.ui.audit.page.AuditReportPage
import net.rrm.ehour.ui.admin.export.page.ExportPage
import java.util.Arrays
import java.util.{List => JList}
import net.rrm.ehour.ui.admin.department.page.DepartmentAdmin
import net.rrm.ehour.ui.admin.assignment.page.AssignmentAdmin

object MenuDefinition {

  def createMenuDefinition:JList[_ <: MenuItem] = {
    val params = new PageParameters()
    params.put(MonthOverviewPage.PARAM_OPEN, MonthOverviewPage.OpenPanel.TIMESHEET)

    val enterHours = new LinkItem("nav.hours.enter", classOf[MonthOverviewPage], new Some(params))
    val hoursOverview = new LinkItem("nav.hours.overview", classOf[MonthOverviewPage])
    val monthExport = new LinkItem("nav.hours.export", classOf[ExportMonthSelectionPage])
    val hoursDropdown = new DropdownMenu("nav.hours.yourHours", Arrays.asList(enterHours, hoursOverview, monthExport))

    val report = new LinkItem("nav.report", classOf[GlobalReportPage])
    val pm = new LinkItem("nav.pm", classOf[ProjectManagement])

    val deptAdmin = new LinkItem("nav.admin.departments", classOf[DepartmentAdmin])
    val userAdmin = new LinkItem("nav.admin.users", classOf[UserAdmin])
    val customerAdmin = new LinkItem("nav.admin.customers", classOf[CustomerAdmin])
    val projectAdmin = new LinkItem("nav.admin.projects", classOf[ProjectAdmin])
    val assignmentAdmin = new LinkItem("nav.admin.assignments", classOf[AssignmentAdmin])
    val adminDropdown = new DropdownMenu("nav.admin.manage", Arrays.asList(deptAdmin, userAdmin, customerAdmin, projectAdmin, assignmentAdmin))

    val mainConfig = new LinkItem("nav.admin.config", classOf[MainConfigPage])
    val auditReport = new LinkItem("nav.admin.audit", classOf[AuditReportPage])
    val export = new LinkItem("nav.admin.export", classOf[ExportPage])
    val configDropdown = new DropdownMenu("nav.admin", Arrays.asList(mainConfig, auditReport, export))

    Arrays.asList(hoursDropdown, report, pm, adminDropdown, configDropdown)
  }
}
