package net.rrm.ehour.ui.common.config

import java.util

import net.rrm.ehour.ui.admin.audit.AuditReportPage
import net.rrm.ehour.ui.admin.backup.BackupDbPage
import net.rrm.ehour.ui.admin.config.MainConfigPage
import net.rrm.ehour.ui.common.header.{DropdownMenu, LinkItem, MenuItem}
import net.rrm.ehour.ui.manage.assignment.AssignmentManagePage
import net.rrm.ehour.ui.manage.customer.CustomerManagePage
import net.rrm.ehour.ui.manage.department.DepartmentManagePage
import net.rrm.ehour.ui.manage.lock.LockManagePage
import net.rrm.ehour.ui.manage.project.ProjectManagePage
import net.rrm.ehour.ui.manage.user.{ImpersonateUserPage, UserManagePage}
import net.rrm.ehour.ui.pm.ProjectManagerPage
import net.rrm.ehour.ui.report.page.ReportPage
import net.rrm.ehour.ui.timesheet.export.TimesheetExportPage
import net.rrm.ehour.ui.timesheet.page.MonthOverviewPage
import org.apache.wicket.request.mapper.parameter.PageParameters
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class PageLayoutConfig {
  @Bean
  def menuDefinition:util.List[_ <: MenuItem] = {
    val params = new PageParameters()
    params.add(MonthOverviewPage.PARAM_OPEN, MonthOverviewPage.OpenPanel.TIMESHEET)

    val enterHours = LinkItem("nav.hours.enter", classOf[MonthOverviewPage], Some(params))
    val hoursOverview = LinkItem("nav.hours.overview", classOf[MonthOverviewPage])
    val monthExport = LinkItem("nav.hours.export", classOf[TimesheetExportPage])
    val hoursDropdown = DropdownMenu("nav.hours.yourHours", util.Arrays.asList(enterHours, hoursOverview, monthExport))

    val report = LinkItem("nav.report", classOf[ReportPage])
    val pm = LinkItem("nav.pm", classOf[ProjectManagerPage])

    val deptAdmin = LinkItem("nav.admin.departments", classOf[DepartmentManagePage])
    val userAdmin = LinkItem("nav.admin.users", classOf[UserManagePage])
    val customerAdmin = LinkItem("nav.admin.customers", classOf[CustomerManagePage])
    val projectAdmin = LinkItem("nav.admin.projects", classOf[ProjectManagePage])
    val assignmentAdmin = LinkItem("nav.admin.assignments", classOf[AssignmentManagePage])
    val lockAdmin = LinkItem("nav.admin.lock", classOf[LockManagePage])
    val impersonate = LinkItem("nav.admin.impersonate", classOf[ImpersonateUserPage])
    val manageDropdown = DropdownMenu("nav.admin.manage", util.Arrays.asList(deptAdmin, userAdmin, customerAdmin, projectAdmin, assignmentAdmin, lockAdmin, impersonate))

    val mainConfig = LinkItem("nav.admin.config", classOf[MainConfigPage])
    val auditReport = LinkItem("nav.admin.audit", classOf[AuditReportPage])
    val export = LinkItem("nav.admin.export", classOf[BackupDbPage])
    val systemDropdown = DropdownMenu("nav.admin", util.Arrays.asList(mainConfig, auditReport, export))

    util.Arrays.asList(hoursDropdown, report, pm, manageDropdown, systemDropdown)
  }

}
