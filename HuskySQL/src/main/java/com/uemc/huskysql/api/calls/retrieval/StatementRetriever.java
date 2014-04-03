package com.uemc.huskysql.api.calls.retrieval;

import static com.uemc.huskysql.api.calls.util.CallUtil.softClose;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.uemc.huskysql.api.calls.DatabaseRetriever;

public abstract class StatementRetriever<V> implements DatabaseRetriever<V> {
    private final Connection conn;
    protected Statement s;
    protected ResultSet rs;
    
    public StatementRetriever(Connection conn) {
        this.conn = conn;
    }

    @Override
    public V call() throws Exception {
        V ret = null;
        try {
            s = conn.createStatement();
            ret = retrieve();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            softClose(conn, s, rs);
        }
        return ret;
    }
}
