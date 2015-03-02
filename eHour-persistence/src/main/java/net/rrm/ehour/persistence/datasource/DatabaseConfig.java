package net.rrm.ehour.persistence.datasource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

public class DatabaseConfig {
    public final String databaseType;
    public final String driver;
    public final String url;
    public final String username;
    public final String password;


    public DatabaseConfig(String databaseType, String driver, String url, String username, String password) {
        this.databaseType = databaseType;
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "DatabaseConfig{" +
                "databaseType='" + databaseType + '\'' +
                ", driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", username='" + (StringUtils.isBlank(username) ? "" : "*****") + '\'' +
                ", password='" + (StringUtils.isBlank(password) ? "" : "*****") + '\'' +
                '}';
    }
}
