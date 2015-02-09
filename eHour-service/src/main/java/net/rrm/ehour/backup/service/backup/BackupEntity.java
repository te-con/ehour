package net.rrm.ehour.backup.service.backup;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.persistence.backup.dao.BackupRowProcessor;

import java.io.Serializable;

public interface BackupEntity extends Comparable<BackupEntity> {
    public String getParentName();

    public Class<? extends DomainObject<? extends Serializable, ?>> getDomainObjectClass();

    public BackupRowProcessor getProcessor();

    public String name();

    public int getOrder();

    @Override
    public int compareTo(BackupEntity o);
}
