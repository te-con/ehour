package net.rrm.ehour.ui.admin.project.assign

import org.apache.wicket.markup.html.basic.Label
import net.rrm.ehour.domain.User

class AffectedUserLabel(id: String, user: User) extends Label(id, user.getFullName) {
  setOutputMarkupId(true)
}
