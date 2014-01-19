package net.rrm.ehour.ui.financial.lock


class AffectedUsersPanelSpec /*extends AbstractSpringWebAppSpec {
  "Affected Users Panel" should {
    val service = mock[TimesheetLockService]
    springTester.getMockContext.putBean(service)

    "render" in {
      val lockModel = new LockModel()

      when(service.findAffectedUsers(lockModel.startDate, lockModel.endDate)).thenReturn(List(AffectedUser(UserObjectMother.createUser(), 12)))

      tester.startComponentInPage(new AffectedUsersPanel("id", Model(lockModel)))
      tester.assertNoErrorMessage()
    }
  }
}
*/