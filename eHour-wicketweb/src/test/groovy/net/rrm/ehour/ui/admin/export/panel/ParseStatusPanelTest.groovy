package net.rrm.ehour.ui.admin.export.panel

import net.rrm.ehour.export.service.ParseStatus
import net.rrm.ehour.persistence.export.dao.ExportType
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester
import org.apache.wicket.markup.html.list.ListView
import org.apache.wicket.markup.html.panel.Panel
import org.apache.wicket.model.Model
import org.apache.wicket.util.tester.TestPanelSource
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 12/5/10 - 12:32 AM
 */
class ParseStatusPanelTest extends AbstractSpringWebAppTester
{
  @Test
  public void shouldDisplayErrors()
  {
    ParseStatus status = new ParseStatus()
    status.addError ExportType.USERS, "failed"
    status.addError ExportType.USERS, "failed again"

    startPanel status

    tester.assertNoErrorMessage()
    tester.assertComponent "panel:errors", ListView.class
    tester.assertComponent "panel:errors:0:msgs", ListView.class
    def msg = tester.getComponentFromLastRenderedPage("panel:errors:0:msgs:0:msg")
    assertEquals "failed", msg.defaultModelObject
  }

  @Test
  public void shouldDisplayInsertions()
  {
    ParseStatus status = new ParseStatus()
    status.addInsertion ExportType.USERS
    status.addInsertion ExportType.USERS

    startPanel status

    tester.assertNoErrorMessage()
    tester.assertNoInfoMessage()
    tester.assertComponent "panel:insertions", ListView.class
    tester.assertModelValue "panel:insertions:0:insertions", "2"
    tester.assertModelValue "panel:insertions:0:key", "USERS"

  }


  private void startPanel(final ParseStatus status)
  {
    tester.startPanel(new TestPanelSource()
    {
      @Override
      Panel getTestPanel(String panelId)
      {
        return new ParseStatusPanel(panelId, new Model<ParseStatus>(status))
      }
    })
  }
}
