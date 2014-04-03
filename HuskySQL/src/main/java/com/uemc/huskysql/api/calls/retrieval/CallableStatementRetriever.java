package com.uemc.huskysql.api.calls.retrieval;

import static com.uemc.huskysql.api.calls.util.CallUtil.softClose;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.uemc.huskysql.api.calls.DatabaseRetriever;

public abstract class CallableStatementRetriever<V> implements DatabaseRetriever<V> {
    private final Connection conn;
    protected CallableStatement cs;
    protected ResultSet rs;
    private String preparedAssertion;
    
    public CallableStatementRetriever(Connection conn, String preparedAssertion) {
        this.conn = conn;
        this.preparedAssertion = preparedAssertion;
        if(!this.preparedAssertion.startsWith("{call")) {
            this.preparedAssertion = "{call " + preparedAssertion + "}";
        }
    }

    @Override
    public V call() throws Exception {
        V ret = null;
        try {
            this.cs = conn.prepareCall(preparedAssertion);
            ret = retrieve();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            softClose(conn, cs, rs);
        }
        return ret;
    }
}
