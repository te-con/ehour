package net.rrm.ehour.ui.admin.project.assign

import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.domain.User
import java.{util => ju}
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.IModel

class AffectedUsersPanel(id: String, userModel: IModel[ju.List[User]]) extends AbstractBasePanel[ju.List[User]](id, userModel) {
  val Id = "users"

  setOutputMarkupId(true)

  override def onInitialize() {
    super.onInitialize()

    val view = new ListView[User](Id, getPanelModel) {
      def populateItem(item: ListItem[User]) {
        item.add(new Label("user", item.getModelObject.getFullName))
      }
    }

    add(view)
  }
}
