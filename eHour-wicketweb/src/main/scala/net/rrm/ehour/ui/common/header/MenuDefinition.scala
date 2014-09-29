package net.rrm.ehour.ui.common.header;

import java.util

import net.rrm.ehour.ui.admin.audit.AuditReportPage
import net.rrm.ehour.ui.admin.backup.BackupDbPage
import net.rrm.ehour.ui.admin.config.MainConfigPage
import net.rrm.ehour.ui.customerreviewer.CustomerReviewerPage
import net.rrm.ehour.ui.manage.customer.CustomerManagePage
import net.rrm.ehour.ui.manage.lock.LockManagePage
import net.rrm.ehour.ui.manage.project.ProjectManagePage
import net.rrm.ehour.ui.manage.user.{ImpersonateUserPage, ManageUserPage}
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

    val userAdmin = LinkItem("nav.admin.users", classOf[ManageUserPage])
    val customerAdmin = LinkItem("nav.admin.customers", classOf[CustomerManagePage])
    val projectAdmin = LinkItem("nav.admin.projects", classOf[ProjectManagePage])
    val lockAdmin = LinkItem("nav.admin.lock", classOf[LockManagePage])
    val impersonate = LinkItem("nav.admin.impersonate", classOf[ImpersonateUserPage])
    val manageDropdown = DropdownMenu("nav.admin.manage", util.Arrays.asList(userAdmin, customerAdmin, projectAdmin, lockAdmin, impersonate))

    val mainConfig = LinkItem("nav.admin.config", classOf[MainConfigPage])
    val auditReport = LinkItem("nav.admin.audit", classOf[AuditReportPage])
    val export = LinkItem("nav.admin.export", classOf[BackupDbPage])
    val systemDropdown = DropdownMenu("nav.admin", util.Arrays.asList(mainConfig, auditReport, export))

    val customerReviewer = LinkItem("nav.customerreviewer.usertimesheet", classOf[CustomerReviewerPage])
    val customerReviewerParent = DropdownMenu("nav.customerreviewer", util.Arrays.asList(customerReviewer))


    util.Arrays.asList(hoursDropdown, report, manageDropdown, systemDropdown, customerReviewerParent)
  }
}
