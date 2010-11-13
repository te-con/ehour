package net.rrm.ehour.persistence.export.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("exportDao")
public class ExportDaoJbcImpl implements ExportDao
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> findForType(ExportType type)
    {
        return find("SELECT * FROM " + type.name());
    }

    public List<Map<String, Object>> find(String sql)
    {
        return jdbcTemplate.queryForList(sql);
    }
}
