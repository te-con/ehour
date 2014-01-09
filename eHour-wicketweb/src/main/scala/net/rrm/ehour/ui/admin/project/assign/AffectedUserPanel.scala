package net.rrm.ehour.ui.admin.project.assign

import net.rrm.ehour.domain.User
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import org.apache.wicket.model.Model
import org.apache.wicket.markup.html.basic.Label

class AffectedUserPanel(id: String, user: User) extends AbstractBasePanel[String](id, new Model[String](user.getFullName)) {
  setOutputMarkupId(true)

  add(new Label("name", getPanelModel))
}
