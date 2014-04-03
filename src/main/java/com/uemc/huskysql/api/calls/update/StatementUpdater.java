package com.uemc.huskysql.api.calls.update;

import static com.uemc.huskysql.api.calls.util.CallUtil.softClose;

import java.sql.Connection;
import java.sql.Statement;

import com.uemc.huskysql.api.calls.DatabaseUpdater;

public abstract class StatementUpdater implements DatabaseUpdater {
    private final Connection conn;
    protected Statement s;
    
    public StatementUpdater(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        try {
            s = conn.createStatement();
            update();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            softClose(conn, s);
        }
    }
}
