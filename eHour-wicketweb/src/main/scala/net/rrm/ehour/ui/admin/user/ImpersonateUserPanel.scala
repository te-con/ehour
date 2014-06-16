package net.rrm.ehour.ui.admin.user

import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel
import net.rrm.ehour.ui.common.util.WebGeo
import org.apache.wicket.model.IModel

class ImpersonateUserPanel(id: String, userModel: IModel[UserAdminBackingBean]) extends AbstractFormSubmittingPanel[UserAdminBackingBean](id, userModel) {
  final val BorderId = "border"

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = new GreySquaredRoundedBorder(BorderId, WebGeo.AUTO)
    add(greyBorder)
    setOutputMarkupId(true)
  }
}
