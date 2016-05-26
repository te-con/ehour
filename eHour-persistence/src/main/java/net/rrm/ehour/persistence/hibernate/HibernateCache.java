package net.rrm.ehour.persistence.hibernate;

import org.hibernate.SessionFactory;

public class HibernateCache {
    private HibernateCache() {
    }

    public static void clearHibernateCache(SessionFactory sf) {
        sf.getCache().evictAllRegions();
    }
}
