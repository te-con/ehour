package net.rrm.ehour.persistence.export.dao;

import java.util.List;
import java.util.Map;

/**
 *
 * @author thies
 *
 */
public interface ExportDao
{
	/**
	 * Find config elements
	 * @return
	 */
	public List<Map<String, Object>> findConfig();

	/**
	 * Find all timesheet entries
	 * @return List with a map of columns
	 */
	public List<Map<String, Object>> findAllTimesheetEntries();
}
