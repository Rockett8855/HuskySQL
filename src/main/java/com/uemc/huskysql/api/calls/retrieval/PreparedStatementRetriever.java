package com.uemc.huskysql.api.calls.retrieval;

import static com.uemc.huskysql.api.calls.util.CallUtil.softClose;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.uemc.huskysql.api.calls.DatabaseRetriever;

public abstract class PreparedStatementRetriever<V> implements DatabaseRetriever<V> {
    private final Connection conn;
    protected PreparedStatement ps;
    protected ResultSet rs;
    private String preparedAssertion;
    
    public PreparedStatementRetriever(Connection conn, String preparedAssertion) {
        this.conn = conn;
        this.preparedAssertion = preparedAssertion;
    }

    @Override
    public V call() throws Exception {
        V ret = null;
        try {
            this.ps = conn.prepareStatement(preparedAssertion);
            ret = retrieve();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            softClose(conn, ps, rs);
        }
        return ret;
    }
}
