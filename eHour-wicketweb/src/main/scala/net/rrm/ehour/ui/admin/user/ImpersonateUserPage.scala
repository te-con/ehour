package net.rrm.ehour.ui.admin.impersonate

import net.rrm.ehour.domain.UserRole
import net.rrm.ehour.ui.admin.user.{AbstractUserAdminPage, ImpersonateUserPanel, UserAdminBackingBean}
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.{Model, ResourceModel}

@AuthorizeInstantiation(value = Array(UserRole.ROLE_ADMIN))
class ImpersonateUserPage extends AbstractUserAdminPage(new ResourceModel("admin.impersonate.title"), new ResourceModel("admin.user.addUser"), new ResourceModel("admin.user.editUser"), new ResourceModel("admin.user.noEditEntrySelected")) {
  val Self = this

  override protected def getBaseAddPanel(panelId: String): Panel = new ImpersonateUserPanel(panelId, new Model(getTabbedPanel.getAddBackingBean))

  override protected def getNewAddBaseBackingBean = new UserAdminBackingBean()

  override protected def getNewEditBaseBackingBean: UserAdminBackingBean = new UserAdminBackingBean()

  override protected def getBaseEditPanel(panelId: String): Panel = ???
}


