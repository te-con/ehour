package net.rrm.ehour.persistence.datasource;

public enum Database {
    DERBY(""),
    MYSQL("com.mysql.jdbc.jdbc2.optional.MysqlDataSource"),
    POSTGRESQL("org.postgresql.ds.PGSimpleDataSource");

    public final String driver;

    Database(String driver) {
        this.driver = driver;
    }
}
