package net.rrm.ehour.ui.manage.project.assign

import net.rrm.ehour.domain.ProjectAssignment
import net.rrm.ehour.ui.common.wicket.Event
import org.apache.wicket.ajax.AjaxRequestTarget

case class EditAssignmentEvent(assignment: ProjectAssignment, override val target: AjaxRequestTarget) extends Event(target)
