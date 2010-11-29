package net.rrm.ehour.export.service.element

import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * @author  thies (thies@te-con.nl)
 * Date: 11/30/10 12:31 AM
 */
class UserRoleParserTest {

  UserRoleParserDaoValidatorImpl daoValidator
  XMLEventReader eventReader
  UserRoleParser parser

  @Before
  void setUp()
  {
    String xmlData = """<USER_TO_USERROLES>
  <USER_TO_USERROLE>
   <ROLE>ROLE_ADMIN</ROLE>
   <USER_ID>1</USER_ID>
  </USER_TO_USERROLE>
  <USER_TO_USERROLE>
   <ROLE>ROLE_REPORT</ROLE>
   <USER_ID>1</USER_ID>
  </USER_TO_USERROLE>
  <USER_TO_USERROLE>
   <ROLE>ROLE_ADMIN</ROLE>
   <USER_ID>2</USER_ID>
  </USER_TO_USERROLE>
  <USER_TO_USERROLE>
   <ROLE>ROLE_CONSULTANT</ROLE>
   <USER_ID>2</USER_ID>
  </USER_TO_USERROLE>
  <USER_TO_USERROLE>
   <ROLE>ROLE_PROJECTMANAGER</ROLE>
   <USER_ID>2</USER_ID>
  </USER_TO_USERROLE>
  <USER_TO_USERROLE>
   <ROLE>ROLE_REPORT</ROLE>
   <USER_ID>2</USER_ID>
  </USER_TO_USERROLE>
 </USER_TO_USERROLES>"""

    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
    eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData))
    eventReader.nextTag()

    daoValidator = new UserRoleParserDaoValidatorImpl()

    parser = new UserRoleParser(daoValidator)
  }

  @Test
  void shouldParseUserRoles()
  {
    parser.parseUserRoles eventReader
    assertEquals 6, daoValidator.persistCount
  }
}
