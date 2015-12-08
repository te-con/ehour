package net.rrm.ehour.persistence.database;

import org.apache.commons.lang.StringUtils;

public class DatabaseConfig {
    public final Database databaseType;
    public final String driver;
    public final String url;
    public final String username;
    public final String password;

    public DatabaseConfig(Database database, String driver, String url, String username, String password) {
        this.databaseType = database;
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "DatabaseConfig{" +
                "database='" + (databaseType != null ? databaseType.name() : "") + '\'' +
                ", driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", username='" + (StringUtils.isBlank(username) ? "" : "*****") + '\'' +
                ", password='" + (StringUtils.isBlank(password) ? "" : "*****") + '\'' +
                '}';
    }
}
