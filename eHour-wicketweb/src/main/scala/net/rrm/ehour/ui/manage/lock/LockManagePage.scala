package net.rrm.ehour.ui.manage.lock

import net.rrm.ehour.domain.TimesheetLock
import net.rrm.ehour.timesheet.service.TimesheetLockService
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel
import net.rrm.ehour.ui.common.model.DateModel
import net.rrm.ehour.ui.common.panel.entryselector.{EntrySelectorListView, EntrySelectorPanel}
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.wicket.Event
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage
import net.rrm.ehour.util._
import org.apache.wicket.ajax.AjaxRequestTarget
import org.apache.wicket.event.IEvent
import org.apache.wicket.markup.head.{CssHeaderItem, IHeaderResponse}
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.markup.html.list.ListItem
import org.apache.wicket.markup.html.panel.{Fragment, Panel}
import org.apache.wicket.model.{Model, IModel, ResourceModel}
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

  var view: EntrySelectorListView[TimesheetLock] = _

  var affectedUsersShown = false

  @SpringBean
  protected var lockService: TimesheetLockService = _

  override def onInitialize() {
    super.onInitialize()

    val greyBorder = new GreyRoundedBorder(BorderId, new ResourceModel("op.lock.admin.list.header"))
    addOrReplace(greyBorder)

    val timesheetLocks = lockService.findAll()
    val existingLocksComponent = createLockListHolder(timesheetLocks)
    val entrySelectorPanel = new EntrySelectorPanel(SelectorId, existingLocksComponent)

    greyBorder.addOrReplace(entrySelectorPanel)
  }

  private def createLockListHolder(locks: List[TimesheetLock]): Fragment = {
    val fragment = new Fragment("itemListHolder", "itemListHolder", this)
    fragment.setOutputMarkupId(true)
    val ehourConfig = EhourWebSession.getEhourConfig
    implicit val locale = ehourConfig.getFormattingLocale

    view = new EntrySelectorListView[TimesheetLock]("itemList", toJava(locks)) {
      protected def onPopulate(item: ListItem[TimesheetLock], itemModel: IModel[TimesheetLock]) {
        val lock = itemModel.getObject

        item.add(new Label("detailLinkLabel", lock.getName))
        item.add(new Label("startDate", new DateModel(lock.getDateStart, ehourConfig, DateModel.DATESTYLE_FULL)))
        item.add(new Label("endDate", new DateModel(lock.getDateEnd, ehourConfig, DateModel.DATESTYLE_FULL)))
      }

      protected def onClick(item: ListItem[TimesheetLock], target: AjaxRequestTarget) {
        val id = item.getModelObject.getLockId

        lockService.find(id) match {
          case Some(lock) =>
            getTabbedPanel.setEditBackingBean(new LockAdminBackingBean(lock))
            getTabbedPanel.switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT)
          case None => getTabbedPanel.switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_ADD)
        }
      }
    }
    fragment.add(view)
    fragment
  }

  override def onEvent(event: IEvent[_]) {
    def update[T <: Event](event: T) = {
      view.setList(toJava(lockService.findAll()))
      event.refresh(view.getParent)
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

  override def renderHead(response: IHeaderResponse) {
    response.render(CssHeaderItem.forReference(Css))
  }
}


object LockManagePage {
  val FrameId = "frame"
  val ExistingLocksId = "existingLocks"
  val LockDetailsId = "lockDetails"
}
