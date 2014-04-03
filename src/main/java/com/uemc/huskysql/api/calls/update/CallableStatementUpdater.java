package com.uemc.huskysql.api.calls.update;

import static com.uemc.huskysql.api.calls.util.CallUtil.softClose;

import java.sql.CallableStatement;
import java.sql.Connection;

import com.uemc.huskysql.api.calls.DatabaseUpdater;

public abstract class CallableStatementUpdater implements DatabaseUpdater {
    private final Connection conn;
    protected CallableStatement cs;
    private String preparedAssertion;
    
    public CallableStatementUpdater(Connection conn, String preparedAssertion) {
        this.conn = conn;
        this.preparedAssertion = preparedAssertion;
        //If the user just supplied the call with parameters and not the correct call syntax
        if(!this.preparedAssertion.startsWith("{call")) {
            this.preparedAssertion = "{call " + preparedAssertion + "}";
        }
    }

    @Override
    public void run() {
        try {
            this.cs = conn.prepareCall(preparedAssertion);
            update();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            softClose(conn, cs);
        }
    }
}
