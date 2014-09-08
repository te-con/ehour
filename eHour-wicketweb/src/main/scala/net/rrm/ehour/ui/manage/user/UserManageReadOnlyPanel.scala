package net.rrm.ehour.ui.manage.user

import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.util.WebGeo
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.{IModel, ResourceModel}

import scala.collection.convert.WrapAsScala

class UserManageReadOnlyPanel(id: String, model: IModel[UserManageBackingBean]) extends AbstractBasePanel[UserManageBackingBean](id, model) {
  val BorderId = "border"

  override def onInitialize() {
    super.onInitialize()

    setOutputMarkupId(true)

    val greyBorder = new GreySquaredRoundedBorder(BorderId, WebGeo.AUTO)
    add(greyBorder)

    greyBorder.add(new Label("user.username"))
    greyBorder.add(new Label("user.firstName"))
    greyBorder.add(new Label("user.lastName"))
    greyBorder.add(new Label("user.email"))
    greyBorder.add(new Label("user.userDepartment"))

    val scalaSet = WrapAsScala.asScalaSet(model.getObject.getUser.getUserRoles)
    val roles = scalaSet.map(role => new ResourceModel(role.getRole).getObject).mkString(", ")

    greyBorder.add(new Label("user.userRoles", roles))
    greyBorder.add(new Label("user.active"))
  }
}
