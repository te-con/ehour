package net.rrm.ehour.backup.config;

import net.rrm.ehour.backup.common.BackupRowProcessor;

import java.util.List;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/18/11 - 12:19 AM
 */
public class TimesheetEntryRowProcessor implements BackupRowProcessor
{
    private static final String UPDATE_DATE = "UPDATE_DATE";
    private static final String ENTRY_DATE = "ENTRY_DATE";

    @Override
    public List<Map<String, Object>> processRows(List<Map<String, Object>> rows)
    {
        for (Map<String, Object> row : rows)
        {
            if (!row.containsKey(UPDATE_DATE) || row.get(UPDATE_DATE) == null) {
                row.put(UPDATE_DATE, row.get(ENTRY_DATE));
            }
        }

        return rows;
    }
}
