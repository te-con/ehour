package net.rrm.ehour.backup.service.restore;

import net.rrm.ehour.backup.config.EhourBackupConfig;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JoinTableParserTest {
    @Test
    public void should_parse_join_table() throws IOException, XMLStreamException {
        EhourBackupConfig locator = new EhourBackupConfig();

        XMLEventReader reader = BackupFileUtil.createXmlReaderFromFile("src/test/resources/import/join_table.xml");

        JoinTableParser parser = new JoinTableParser(reader, locator);

        reader.nextTag();

        JoinTables joinTables = parser.parseJoinTables(new JoinTables());

        assertEquals(2, joinTables.size());

        assertEquals(4, joinTables.getTarget("USER_TO_USERROLE", "1").size());
        assertEquals(3, joinTables.getTarget("USER_TO_USERROLE", "2").size());
        assertEquals(2, joinTables.getTarget("USER_TO_USERROLE", "3").size());

        assertEquals(2, joinTables.getTarget("USER_TO_DEPARTMENT", "1").size());
        assertEquals(1, joinTables.getTarget("USER_TO_DEPARTMENT", "2").size());
    }
}