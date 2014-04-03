package com.uemc.huskysql.huskypool;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import com.uemc.huskysql.api.ConnectionPool;
import com.uemc.huskysql.api.ReusableConnection;

public class HuskyConnection implements Connection, ReusableConnection {
    private final Connection base;
    private final ConnectionPool owner;
    private boolean isInUse;
    private static int UUID_TRACKER = 0;
    private final String name;
    
    HuskyConnection(Connection base, ConnectionPool owner) {
        this.owner = owner;
        this.base = base;
        this.name = "HuskyConnection$" + UUID_TRACKER++;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return base.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return base.isWrapperFor(iface);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return base.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return base.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return base.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return base.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        base.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return base.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        base.commit();
    }

    @Override
    public void rollback() throws SQLException {
        base.rollback();
    }

    @Override
    public void close() throws SQLException {
        //TODO: Return to HuskyPool
        this.isInUse = false;
        this.restore();
    }

    @Override
    public boolean isClosed() throws SQLException {
        //TODO: WTF to do here?
        return base.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return base.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        base.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return base.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        base.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return base.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        base.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return base.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return base.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        base.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return base.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return base.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return base.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return base.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        base.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        base.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return base.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return base.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return base.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        base.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        base.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return base.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return base.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return base.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return base.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return base.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return base.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return base.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return base.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return base.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return base.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return base.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        base.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        base.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return base.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return base.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return base.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return base.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        base.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return base.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        base.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        base.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return base.getNetworkTimeout();
    }

    public boolean checkOpen() throws SQLException {
        return (isInUse && isValid(1));
    }

    @Override
    public boolean restore() {
        return owner.returnResource(this);
    }
    
    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof HuskyConnection)) {
            return false;
        }
        HuskyConnection other = (HuskyConnection) o;
        return other.name.equals(this.name);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public void superClose() throws SQLException {
        base.close();
    }
}
