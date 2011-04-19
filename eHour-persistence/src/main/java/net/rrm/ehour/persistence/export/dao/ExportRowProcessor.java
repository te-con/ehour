package net.rrm.ehour.persistence.export.dao;

import java.util.List;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 1/18/11 - 12:17 AM
 */
public interface ExportRowProcessor
{
    public List<Map<String, Object>> processRows(List<Map<String, Object>> rows);
}
