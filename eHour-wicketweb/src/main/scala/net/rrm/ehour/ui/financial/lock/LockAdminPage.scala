package net.rrm.ehour.ui.financial.lock

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation
import net.rrm.ehour.domain.{TimesheetLock, UserRole}
import org.apache.wicket.model.{IModel, ResourceModel}
import net.rrm.ehour.ui.admin.AbstractTabbedAdminPage
import org.apache.wicket.markup.html.panel.{Fragment, Panel}
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.timesheet.service.TimesheetLockService
import org.apache.wicket.markup.html.basic.Label
import net.rrm.ehour.ui.common.panel.entryselector.{EntrySelectorPanel, EntrySelectorListView}
import org.apache.wicket.markup.html.list.ListItem
import net.rrm.ehour.util._
import org.apache.wicket.ajax.AjaxRequestTarget
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import org.apache.wicket.event.IEvent
import org.apache.wicket.markup.html.border.Border
import net.rrm.ehour.ui.common.wicket.Event

@AuthorizeInstantiation(value = Array(UserRole.ROLE_ADMIN))
class LockAdminPage extends AbstractTabbedAdminPage[LockAdminBackingBean](new ResourceModel("op.lock.admin.title"),
  new ResourceModel("op.lock.admin.addLock.header"),
  new ResourceModel("op.lock.admin.editLock.header"),
  new ResourceModel("op.lock.admin.lock.noEditEntrySelected")
) {

  val SelectorId = "lockSelector"
  val BorderId = "entrySelectorFrame"

  val self = this

  var view:EntrySelectorListView[TimesheetLock] = _

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
    implicit val locale = getEhourWebSession.getEhourConfig.getFormattingLocale

    view = new EntrySelectorListView[TimesheetLock]("itemList", toJava(locks)) {
      protected def onPopulate(item: ListItem[TimesheetLock], itemModel: IModel[TimesheetLock]) {
        val lock = itemModel.getObject

        item.add(new Label("detailLinkLabel", lock.getName))
        item.add(new Label("startDate", lock.getDateStart.toString(/*"MM/dd/YYYY"*/)))
        item.add(new Label("endDate", lock.getDateEnd.toString(/*"MM/dd/YYYY"*/)))
      }

      protected def onClick(item: ListItem[TimesheetLock], target: AjaxRequestTarget) {
        //        val projectId: Integer = item.getModelObject.getProjectId
        //        getTabbedPanel.setEditBackingBean(new ProjectAdminBackingBean(projectService.getProjectAndCheckDeletability(projectId)))
        //        getTabbedPanel.switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT)
      }
    }
    fragment.add(view)
    fragment
  }

  private def getBorderContainer = get(BorderId).asInstanceOf[Border].getBodyContainer

  override def onEvent(event: IEvent[_]) {
    def update[T <: Event](event: T) = {
      view.setList(toJava(lockService.findAll()))
      getBorderContainer.get(SelectorId).asInstanceOf[EntrySelectorPanel].refreshList(event.target)
      getTabbedPanel.succesfulSave(event.target)
    }

    event.getPayload match {
      case event: LockAddedEvent => {
        val lock = event.bean.getDomainObject
        lockService.createNew(lock.getDateStart, lock.getDateEnd)
        update(event)
      }
      case event: LockModifiedEvent => {
        val lock = event.bean.getDomainObject
        lockService.updateExisting(lock.getLockId, lock.getDateStart, lock.getDateEnd, lock.getName)
        update(event)
      }
      case event: LockDeletedEvent => {
        val lock = event.bean.getDomainObject
        lockService.deleteLock(lock.getLockId)
        update(event)
      }
      case _ =>
    }
  }

  protected def getNewAddBaseBackingBean: LockAdminBackingBean = new LockAdminBackingBean(new TimesheetLock())

  protected def getNewEditBaseBackingBean: LockAdminBackingBean = new LockAdminBackingBean(new TimesheetLock())

  protected def getBaseAddPanel(panelId: String): Panel = new LockFormContainer(panelId, getTabbedPanel.getAddBackingBean)

  protected def getBaseEditPanel(panelId: String): Panel = new LockFormContainer(panelId, getTabbedPanel.getAddBackingBean)
}


object LockAdminPage {
  val FrameId = "frame"
  val ExistingLocksId = "existingLocks"
  val LockDetailsId = "lockDetails"
}
