package net.rrm.ehour.persistence.export.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("exportDao")
public class ExportDaoJbcImpl implements ExportDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> findAllTimesheetEntries()
	{
		String sql = "SELECT * FROM TIMESHEET_ENTRY";

		return jdbcTemplate.queryForList(sql);
	}
}
