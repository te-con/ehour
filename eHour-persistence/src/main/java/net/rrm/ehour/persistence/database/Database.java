package net.rrm.ehour.persistence.database;

public enum Database {
    DERBY(""),
    MYSQL("com.mysql.jdbc.jdbc2.optional.MysqlDataSource"),
    POSTGRESQL("org.postgresql.ds.PGSimpleDataSource");

    public final String defaultDriver;

    Database(String driver) {
        this.defaultDriver = driver;
    }
}
