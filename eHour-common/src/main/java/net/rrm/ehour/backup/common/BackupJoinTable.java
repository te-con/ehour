package net.rrm.ehour.backup.common;

public class BackupJoinTable {
    private String tableName;

    private String attributeSource;
    private String attributeTarget;

    public BackupJoinTable(String tableName, String attributeSource, String attributeTarget) {
        this.tableName = tableName.toUpperCase();
        this.attributeSource = attributeSource;
        this.attributeTarget = attributeTarget;
    }

    public String getContainer() {
        return tableName + "S";
    }

    public String getTableName() {
        return tableName;
    }

    public String getAttributeSource() {
        return attributeSource;
    }

    public String getAttributeTarget() {
        return attributeTarget;
    }
}
