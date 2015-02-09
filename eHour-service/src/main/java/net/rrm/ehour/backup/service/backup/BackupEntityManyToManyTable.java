package net.rrm.ehour.backup.service.backup;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.persistence.backup.dao.BackupRowProcessor;

import java.io.Serializable;
import java.util.Optional;

public class BackupEntityManyToManyTable implements BackupEntity {
    private String parentName;

    private String name;

    private String attributeA;
    private String attributeB;

    private PersistManyToMany persister;

    private int order;

    public BackupEntityManyToManyTable(String name, String attributeA, String attributeB, PersistManyToMany persister, int order) {
        this.name = name;
        this.attributeA = attributeA;
        this.attributeB = attributeB;
        this.persister = persister;
        this.order = order;
    }

    @Override
    public String getParentName() {
        return name + "S";
    }

    @Override
    public Class<? extends DomainObject<? extends Serializable, ?>> getDomainObjectClass() {
        return null;
    }

    @Override
    public BackupRowProcessor getProcessor() {
        return null;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public PersistManyToMany getPersister() {
        return persister;
    }

    @Override
    public int compareTo(BackupEntity o) {
        return o.getOrder() - order;
    }

    public interface PersistManyToMany {
        Optional<String> persist(Serializable attributeA, Serializable attributeB);
    }
}
