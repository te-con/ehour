package net.rrm.ehour.ui.report.summary

import net.rrm.ehour.ui.common.page.AbstractBasePage
import org.apache.wicket.model.ResourceModel
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import net.rrm.ehour.domain.UserRole

@AuthorizeInstantiation(value = Array(UserRole.ROLE_REPORT))
class ProjectSummaryPage extends AbstractBasePage[String](new ResourceModel("report.summary.title")) {

}
