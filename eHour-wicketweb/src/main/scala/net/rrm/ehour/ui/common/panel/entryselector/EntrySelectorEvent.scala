package net.rrm.ehour.ui.common.panel.entryselector

import net.rrm.ehour.ui.common.wicket.Event
import org.apache.wicket.ajax.AjaxRequestTarget

case class EntryListUpdatedEvent(override val target: AjaxRequestTarget) extends Event(target)

case class FilterChangedEvent(filter: EntrySelectorFilter, override val target: AjaxRequestTarget) extends Event(target)

case class EntrySelectedEvent(userId: Integer, override val target: AjaxRequestTarget) extends Event(target)