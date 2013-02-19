package net.rrm.ehour.persistence.backup.dao;

import java.util.List;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/18/11 - 12:19 AM
 */
public class TimesheetEntryRowProcessor implements BackupRowProcessor
{
    private static final String UPDATE__DATE = "UPDATE_DATE";
    private static final String ENTRY_DATE = "ENTRY_DATE";

    @Override
    public List<Map<String, Object>> processRows(List<Map<String, Object>> rows)
    {
        for (Map<String, Object> row : rows)
        {
            if (!row.containsKey(UPDATE__DATE) || row.get(UPDATE__DATE) == null) {
                row.put(UPDATE__DATE, row.get(ENTRY_DATE));
            }
        }

        return rows;
    }
}
