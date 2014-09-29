package net.rrm.ehour.backup.service.restore

import net.rrm.ehour.backup.domain.ParseSession
import net.rrm.ehour.domain.Audit
import net.rrm.ehour.domain.AuditActionType
import net.rrm.ehour.domain.User
import net.rrm.ehour.domain.UserObjectMother
import net.rrm.ehour.persistence.backup.dao.BackupEntityType
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import javax.xml.stream.XMLEventReader
import javax.xml.stream.XMLInputFactory

import static org.junit.Assert.assertEquals

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: Nov 16, 2010 - 11:54:54 PM
 */
class DomainObjectParserTest {
    DomainObjectParserDaoTestValidator daoValidator;
    PrimaryKeyCache keyCache
    ParseSession status

    @Mock
    DomainObjectParserDao parserDao

    @Before
    void setUp() {
        keyCache = new PrimaryKeyCache()
        status = new ParseSession()

        MockitoAnnotations.initMocks this
    }

    private DomainObjectParser createResolver(String xmlData) {
        return createResolver(xmlData, null, null)
    }

    private DomainObjectParser createResolver(String xmlData, def returnOnFind, def onFind) {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = inputFactory.createXMLEventReader(new StringReader(xmlData))

        // skip the startdoc
        eventReader.nextTag()

        daoValidator = (returnOnFind == null) ? new DomainObjectParserDaoValidatorImpl() : new DomainObjectParserDaoTestValidator(returnOnFind, onFind)

        return new DomainObjectParser(eventReader, daoValidator, keyCache);
    }

    @Test
    void shouldParseEnum() {
        def resolver = createResolver(""" <AUDITS CLASS="net.rrm.ehour.domain.Audit"><AUDIT>
   <AUDIT_ID>173</AUDIT_ID>
   <USER_ID>2</USER_ID>
   <USER_FULLNAME>Edeling, Thies</USER_FULLNAME>
   <AUDIT_DATE>2010-01-12 16:20:51.0</AUDIT_DATE>
   <SUCCESS>Y</SUCCESS>
   <AUDIT_ACTION_TYPE>LOGIN</AUDIT_ACTION_TYPE>
  </AUDIT></AUDITS>
""", UserObjectMother.createUser(), 2)

        def type = BackupEntityType.AUDIT

        List<Audit> result = resolver.parse(type.getDomainObjectClass(), status);

        assertEquals 1, result.size()

        assertEquals AuditActionType.LOGIN, result[0].auditActionType
    }

    @Test
    void shouldRetrieveManyToOne() {
        def user = UserObjectMother.createUser()

        def resolver = createResolver(""" <AUDITS CLASS="net.rrm.ehour.domain.Audit"><AUDIT>
   <AUDIT_ID>173</AUDIT_ID>
   <USER_ID>2</USER_ID>
   <USER_FULLNAME>Edeling, Thies</USER_FULLNAME>
   <AUDIT_DATE>2010-01-12 16:20:51.0</AUDIT_DATE>
   <SUCCESS>Y</SUCCESS>
   <AUDIT_ACTION_TYPE>LOGIN</AUDIT_ACTION_TYPE>
  </AUDIT></AUDITS>
""", user, 2)

        def type = BackupEntityType.AUDIT

        keyCache.putKey(User.class, 2, 2)

        List<Audit> result = resolver.parse(type.getDomainObjectClass(), status);

        assertEquals 1, result.size()

        assertEquals AuditActionType.LOGIN, result[0].auditActionType
        assertEquals 1, daoValidator.totalPersistCount
        assertEquals user, result[0].user
    }

    private class DomainObjectParserDaoTestValidator<T> extends DomainObjectParserDaoValidatorImpl {
        private T returnObject;
        private Serializable primaryKey;

        DomainObjectParserDaoTestValidator(T returnObject, Serializable primaryKey) {
            this.primaryKey = primaryKey;
            this.returnObject = returnObject;
        }

        @Override
        public <T extends Serializable> T find(Serializable pk, Class<T> type) {
            return pk.equals(this.primaryKey) ? returnObject : null as T
        }
    };
}
