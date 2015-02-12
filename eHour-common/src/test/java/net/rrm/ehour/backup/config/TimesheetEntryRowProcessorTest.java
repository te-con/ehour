package net.rrm.ehour.backup.config;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/18/11 - 12:28 AM
 */
public class TimesheetEntryRowProcessorTest {
    @SuppressWarnings("unchecked")
    @Test
    public void processRow() {
        Map<String, Object> map = new LinkedHashMap<>(1);
        map.put("ENTRY_DATE", "12");
        Map<String, Object> map1 = new LinkedHashMap<>(2);
        map1.put("ENTRY_DATE", "13");
        map1.put("UPDATE_DATE", "14");
        List<Map<String, Object>> rows = Arrays.asList(map, map1);

        new TimesheetEntryRowProcessor().processRows(rows);

        assertEquals("12", rows.get(0).get("UPDATE_DATE"));
        assertEquals("14", rows.get(1).get("UPDATE_DATE"));
    }
}
