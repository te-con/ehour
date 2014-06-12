package net.rrm.ehour.ui.admin.impersonate

import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.ui.common.page.AbstractBasePage
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.model.ResourceModel

@AuthorizeInstantiation(value = Array(UserRole.ROLE_ADMIN))
class ImpersonatePage extends AbstractBasePage[String](new ResourceModel("report.summary.title"))