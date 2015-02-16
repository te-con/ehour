package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.common.BackupEntityType;
import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 11/30/10 12:31 AM
 */
public class UserRoleParserTest {
    private UserRoleParserDaoValidatorImpl daoValidator;
    private XMLEventReader eventReader;
    private UserRoleParser parser;
    private ParseSession status;
    private BackupEntityType userRoleBackupEntityType = new BackupEntityType(UserRole.class, "USER_ROLE", 0);

    @Before
    public void setUp() throws XMLStreamException {
        String xmlData = "<USER_TO_USERROLES>\n  <USER_TO_USERROLE>\n   <ROLE>ROLE_ADMIN</ROLE>\n   <USER_ID>1</USER_ID>\n  </USER_TO_USERROLE>\n  <USER_TO_USERROLE>\n   <ROLE>ROLE_REPORT</ROLE>\n   <USER_ID>1</USER_ID>\n  </USER_TO_USERROLE>\n  <USER_TO_USERROLE>\n   <ROLE>ROLE_ADMIN</ROLE>\n   <USER_ID>2</USER_ID>\n  </USER_TO_USERROLE>\n  <USER_TO_USERROLE>\n   <ROLE>ROLE_CONSULTANT</ROLE>\n   <USER_ID>2</USER_ID>\n  </USER_TO_USERROLE>\n  <USER_TO_USERROLE>\n   <ROLE>ROLE_PROJECTMANAGER</ROLE>\n   <USER_ID>2</USER_ID>\n  </USER_TO_USERROLE>\n  <USER_TO_USERROLE>\n   <ROLE>ROLE_REPORT</ROLE>\n   <USER_ID>2</USER_ID>\n  </USER_TO_USERROLE>\n </USER_TO_USERROLES>";

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData));
        eventReader.nextTag();

        daoValidator = new UserRoleParserDaoValidatorImpl();
        status = new ParseSession();

        PrimaryKeyCache cache = new PrimaryKeyCache();
        cache.putKey(User.class, 1, 1);
        cache.putKey(User.class, 2, 2);

        parser = new UserRoleParser(daoValidator, cache, userRoleBackupEntityType);
    }

    @Test
    public void shouldParseUserRoles() throws XMLStreamException {
        parser.parseUserRoles(eventReader, status);
        assertEquals(6, daoValidator.getPersistCount());
        assertEquals(6, status.getInsertions().get(userRoleBackupEntityType).intValue());
    }
}
