package net.rrm.ehour.backup.service.restore;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.map.MultiValueMap;

import java.util.List;
import java.util.Map;

public class JoinTables {
    private Map<String, MultiValueMap> joinTables = Maps.newHashMap();

    public void put(String joinTableName, String source, String target) {
        String joinTableNameLower = joinTableName.toLowerCase();
        MultiValueMap joinTable = joinTables.get(joinTableNameLower);

        if (joinTable == null) {
            joinTable = new MultiValueMap();
        }

        joinTable.put(source, target);

        joinTables.put(joinTableNameLower, joinTable);
    }

    @SuppressWarnings("unchecked")
    public List<String> getTarget(String joinTableName, String source) {
        String joinTableNameLower = joinTableName.toLowerCase();

        if (joinTables.containsKey(joinTableNameLower)) {
            MultiValueMap multiValueMap = joinTables.get(joinTableNameLower);
            return (List<String>) multiValueMap.get(source);
        } else {
            return Lists.newArrayList();
        }
    }

    public int size() {
        return joinTables.size();
    }
}
