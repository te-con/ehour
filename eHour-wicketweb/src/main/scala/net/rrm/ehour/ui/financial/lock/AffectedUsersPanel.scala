package net.rrm.ehour.ui.financial.lock


class AffectedUsersPanel(id: String/*, lockModel: IModel[LockModel]*/) /*extends AbstractAjaxPanel[LockModel](id, lockModel) {
  @SpringBean
  protected var lockService: TimesheetLockService = _

  override def onBeforeRender(): Unit = {
    super.onBeforeRender()

    val blueBorder = new GreyBlueRoundedBorder(AffectedUsersPanel.BorderId)
    blueBorder.setOutputMarkupId(true)
    addOrReplace(blueBorder)

    val affectedUsers = lockService.findAffectedUsers(lockModel.getObject.startDate, lockModel.getObject.endDate)

    val repeater = new ListView[AffectedUser](AffectedUsersPanel.AffectedUsersId, toJava(affectedUsers)) {
      def populateItem(item: ListItem[AffectedUser]) {
        val affectedUser = item.getModelObject

        item.add(new Label("user", affectedUser.user.getFullName))
        item.add(new Label("hours", affectedUser.hoursBooked))
      }
    }
    
    blueBorder.add(repeater)
  }
}

object AffectedUsersPanel {
  val AffectedUsersId = "affectedUsers"
  val BorderId = "border"
}

  
  
*/