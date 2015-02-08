package net.rrm.ehour.backup.service;

import net.rrm.ehour.persistence.backup.dao.BackupRowProcessor;

import javax.activation.DataSource;

public interface BackupEntity {
    public String getParentName();

    public DataSource getDomainObjectClass();

    public BackupRowProcessor getProcessor();

    public String name();
}
