package net.rrm.ehour.ui.manage.lock

import java.text.SimpleDateFormat

import com.google.common.collect.Lists
import net.rrm.ehour.domain.TimesheetLock
import net.rrm.ehour.timesheet.service.TimesheetLockService
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel
import net.rrm.ehour.ui.common.panel.entryselector.{EntrySelectorData, EntrySelectorPanel}
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.wicket.Event
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.IEvent
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.{Model, ResourceModel}
import org.apache.wicket.request.resource.CssResourceReference
import org.apache.wicket.spring.injection.annot.SpringBean
import org.joda.time.DateTime

class LockManagePage extends AbstractTabbedManagePage[LockAdminBackingBean](new ResourceModel("op.lock.admin.title"),
  new ResourceModel("op.lock.admin.addLock.header"),
  new ResourceModel("op.lock.admin.editLock.header"),
  new ResourceModel("op.lock.admin.lock.noEditEntrySelected")
) {

  val Css = new CssResourceReference(classOf[LockManagePage], "timesheetlocks.css")

  val SelectorId = "lockSelector"
  val BorderId = "entrySelectorFrame"

  val self = this

  var affectedUsersShown = false

  var entrySelectorPanel:EntrySelectorPanel = _

  @SpringBean
  protected var lockService: TimesheetLockService = _

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = new GreyRoundedBorder(BorderId, new ResourceModel("op.lock.admin.list.header"))
    addOrReplace(greyBorder)

    val clickHandler = new EntrySelectorPanel.ClickHandler {
      def onClick(row: EntrySelectorData.EntrySelectorRow, target: AjaxRequestTarget) {
        val id = row.getId.asInstanceOf[Integer]

        lockService.find(id) match {
          case Some(lock) =>
            getTabbedPanel.setEditBackingBean(new LockAdminBackingBean(lock))
            getTabbedPanel.switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT)
          case None => getTabbedPanel.switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_ADD)
        }
      }
    }

    entrySelectorPanel = new EntrySelectorPanel(SelectorId, createSelectorData(lockService.findAll()), clickHandler)

    greyBorder.addOrReplace(entrySelectorPanel)
  }

  private def createSelectorData(locks: List[TimesheetLock]): EntrySelectorData = {
    val headers = Lists.newArrayList(new EntrySelectorData.Header("op.lock.admin.name.label"),
                                     new EntrySelectorData.Header("op.lock.admin.start.label"),
                                     new EntrySelectorData.Header("op.lock.admin.end.label"))
    val ehourConfig = EhourWebSession.getEhourConfig
    val formatter = new SimpleDateFormat("dd MMM yy", ehourConfig.getFormattingLocale)

    val rows = for (lock <- locks) yield {
      val cells = Lists.newArrayList(lock.getName, formatter.format(lock.getDateStart), formatter.format(lock.getDateEnd))
      new EntrySelectorData.EntrySelectorRow(cells, lock.getLockId)
    }

    import scala.collection.JavaConversions._
    new EntrySelectorData(headers, rows)
  }


  override def onEvent(event: IEvent[_]) {
    def update[T <: Event](event: T) = {
      entrySelectorPanel.updateData(createSelectorData(lockService.findAll()))
      entrySelectorPanel.reRender(event.target)
      getTabbedPanel.succesfulSave(event.target)
    }

    event.getPayload match {
      case event: LockAddedEvent =>
        val lock = event.lock
        lockService.createNew(Option.apply(lock.getName), lock.getDateStart, lock.getDateEnd, lock.getExcludedUsers)
        update(event)
      case event: LockEditedEvent =>
        val lock = event.lock
        lockService.updateExisting(lock.getLockId, lock.getDateStart, lock.getDateEnd, lock.getName, lock.getExcludedUsers)
        update(event)
      case event: UnlockedEvent =>
        val lock = event.lock
        lockService.deleteLock(lock.getLockId)
        update(event)
      case _ =>
    }
  }

  protected def getNewAddBaseBackingBean: LockAdminBackingBean = {
    val start = new DateTime().withDayOfMonth(1).toDate
    val end = new DateTime().withDayOfMonth(1).plusMonths(1).minusDays(1).toDate
    new LockAdminBackingBean(new TimesheetLock(start, end)).updateName(EhourWebSession.getEhourConfig.getFormattingLocale)
  }

  protected def getNewEditBaseBackingBean: LockAdminBackingBean = new LockAdminBackingBean(new TimesheetLock())

  protected def getBaseAddPanel(panelId: String): Panel = new LockFormPanel(panelId, new Model(getTabbedPanel.getAddBackingBean))

  protected def getBaseEditPanel(panelId: String): Panel = new LockFormPanel(panelId, new Model(getTabbedPanel.getEditBackingBean))

  override protected def onRenderHead(response: IHeaderResponse): Unit = response.render(CssHeaderItem.forReference(Css))
}


object LockManagePage {
  val FrameId = "frame"
  val ExistingLocksId = "existingLocks"
  val LockDetailsId = "lockDetails"
}
