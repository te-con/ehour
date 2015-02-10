package net.rrm.ehour.backup.service.restore;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.map.MultiValueMap;

import java.util.List;
import java.util.Map;

public class JoinTables {
    private Map<String, MultiValueMap> joinTables = Maps.newHashMap();

    public void put(String joinTableName, String source, String target) {
        MultiValueMap joinTable = joinTables.get(joinTableName);

        if (joinTable == null) {
            joinTable = new MultiValueMap();
        }

        joinTable.put(source, target);

        joinTables.put(joinTableName, joinTable);
    }

    @SuppressWarnings("unchecked")
    public List<String> getTarget(String joinTableName, String source) {
        if (joinTables.containsKey(joinTableName)) {
            MultiValueMap multiValueMap = joinTables.get(joinTableName);
            return (List<String>) multiValueMap.get(source);
        } else {
            return Lists.newArrayList();
        }
    }

    public int size() {
        return joinTables.size();
    }
}
