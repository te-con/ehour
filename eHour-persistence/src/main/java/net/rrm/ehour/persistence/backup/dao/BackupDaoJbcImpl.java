package net.rrm.ehour.persistence.backup.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("exportDao")
public class BackupDaoJbcImpl implements BackupDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> findForType(BackupEntityType type) {
        return find("SELECT * FROM " + type.name());
    }

    public List<Map<String, Object>> find(String sql) {
        return jdbcTemplate.queryForList(sql);
    }
}

