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
	public List<Map<String, Object>> findForType(ExportType type);

    public void deleteType(ExportType type);
}
