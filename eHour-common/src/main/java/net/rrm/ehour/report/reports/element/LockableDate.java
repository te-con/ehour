package net.rrm.ehour.report.reports.element;

import java.io.Serializable;
import java.util.Date;

public class LockableDate implements Serializable {
    private final Date date;
    private final Boolean locked;

    public LockableDate(Date date, Boolean locked) {
        this.date = date;
        this.locked = locked;
    }

    public Date getDate() {
        return date;
    }

    public Boolean isLocked() {
        return locked;
    }
}
