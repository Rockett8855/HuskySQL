package com.uemc.huskysql.api.calls.update;

import static com.uemc.huskysql.api.calls.util.CallUtil.softClose;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.uemc.huskysql.api.calls.DatabaseUpdater;

public abstract class PreparedStatementUpdater implements DatabaseUpdater {
    private final Connection conn;
    protected PreparedStatement ps;
    private final String preparedAssertion;
    
    public PreparedStatementUpdater(Connection conn, String preparedAssertion) {
        this.conn = conn;
        this.preparedAssertion = preparedAssertion;
    }

    @Override
    public void run() {
        try {
            this.ps = conn.prepareStatement(preparedAssertion);
            update();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            softClose(conn, ps);
        }
    }
}
