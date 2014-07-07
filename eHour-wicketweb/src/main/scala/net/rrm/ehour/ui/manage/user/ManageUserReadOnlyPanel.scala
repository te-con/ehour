package net.rrm.ehour.ui.manage.user

import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.util.WebGeo
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel

class ManageUserReadOnlyPanel(id: String, model: IModel[ManageUserBackingBean]) extends AbstractBasePanel[ManageUserBackingBean](id, model) {
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
    greyBorder.add(new Label("user.userRoles"))
    greyBorder.add(new Label("user.active"))
  }
}
