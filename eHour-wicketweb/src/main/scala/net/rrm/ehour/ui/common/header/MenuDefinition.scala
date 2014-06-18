package net.rrm.ehour.ui.common.header;

import java.util

import net.rrm.ehour.ui.admin.assignment.AssignmentAdminPage
import net.rrm.ehour.ui.admin.backup.BackupDbPage
import net.rrm.ehour.ui.admin.config.page.MainConfigPage
import net.rrm.ehour.ui.admin.customer.CustomerAdminPage
import net.rrm.ehour.ui.admin.department.DepartmentAdminPage
import net.rrm.ehour.ui.admin.project.ProjectAdminPage
import net.rrm.ehour.ui.admin.user.{ImpersonateUserPage, UserAdminPage}
import net.rrm.ehour.ui.audit.page.AuditReportPage
import net.rrm.ehour.ui.financial.lock.LockAdminPage
import net.rrm.ehour.ui.pm.ProjectManagementPage
import net.rrm.ehour.ui.report.page.ReportPage
import net.rrm.ehour.ui.timesheet.export.TimesheetExportPage
import net.rrm.ehour.ui.timesheet.page.MonthOverviewPage
import org.apache.wicket.request.mapper.parameter.PageParameters

object MenuDefinition {

  def createMenuDefinition:util.List[_ <: MenuItem] = {
    val params = new PageParameters()
    params.add(MonthOverviewPage.PARAM_OPEN, MonthOverviewPage.OpenPanel.TIMESHEET)

    val enterHours = LinkItem("nav.hours.enter", classOf[MonthOverviewPage], Some(params))
    val hoursOverview = LinkItem("nav.hours.overview", classOf[MonthOverviewPage])
    val monthExport = LinkItem("nav.hours.export", classOf[TimesheetExportPage])
    val hoursDropdown = DropdownMenu("nav.hours.yourHours", util.Arrays.asList(enterHours, hoursOverview, monthExport))

    val report = LinkItem("nav.report", classOf[ReportPage])
    val pm = LinkItem("nav.pm", classOf[ProjectManagementPage])

    val deptAdmin = LinkItem("nav.admin.departments", classOf[DepartmentAdminPage])
    val userAdmin = LinkItem("nav.admin.users", classOf[UserAdminPage])
    val customerAdmin = LinkItem("nav.admin.customers", classOf[CustomerAdminPage])
    val projectAdmin = LinkItem("nav.admin.projects", classOf[ProjectAdminPage])
    val assignmentAdmin = LinkItem("nav.admin.assignments", classOf[AssignmentAdminPage])
    val lockAdmin = LinkItem("nav.admin.lock", classOf[LockAdminPage])
    val impersonate = LinkItem("nav.admin.impersonate", classOf[ImpersonateUserPage])
    val manageDropdown = DropdownMenu("nav.admin.manage", util.Arrays.asList(deptAdmin, userAdmin, customerAdmin, projectAdmin, assignmentAdmin, lockAdmin, impersonate))

    val mainConfig = LinkItem("nav.admin.config", classOf[MainConfigPage])
    val auditReport = LinkItem("nav.admin.audit", classOf[AuditReportPage])
    val export = LinkItem("nav.admin.export", classOf[BackupDbPage])
    val systemDropdown = DropdownMenu("nav.admin", util.Arrays.asList(mainConfig, auditReport, export))

    util.Arrays.asList(hoursDropdown, report, pm, manageDropdown, systemDropdown)
  }
}
