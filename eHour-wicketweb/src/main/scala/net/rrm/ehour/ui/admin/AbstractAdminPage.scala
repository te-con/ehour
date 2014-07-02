package net.rrm.ehour.ui.admin

import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.ui.common.page.AbstractBasePage
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.model.{IModel, ResourceModel}

/**
 * Base page for admin adding admin nav and contextual help
 */
@AuthorizeInstantiation(value = Array(UserRole.ROLE_ADMIN))
abstract class AbstractAdminPage[T](pageTitle: ResourceModel, model: IModel[T]) extends AbstractBasePage[T](pageTitle, model) {
  def this(pageTitle: ResourceModel) {
    this(pageTitle, null)
  }
}

