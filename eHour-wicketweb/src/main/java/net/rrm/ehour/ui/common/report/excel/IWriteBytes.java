package net.rrm.ehour.ui.common.report.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public interface IWriteBytes extends Serializable {
    public void write(OutputStream stream) throws IOException;
}
