package com.uemc.huskysql.huskypool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import com.uemc.huskysql.api.ConnectionPool;
import com.uemc.huskysql.api.DatabaseCallExecutor;
import com.uemc.huskysql.util.FixedSet;

public class HuskyPool implements ConnectionPool {
    private int size;
    private final boolean fixedSize;
    private final Set<Connection> openConnections;
    private final Set<String> inUseConnectionNames;
    private final HuskyProperties props;
    private final HuskyDatabaseExecutor ex;
    private int tempConnsMade = 0;
    
    HuskyPool(int size, HuskyProperties props, boolean fixed, ExecutorService ex) {
        this.size = size;
        this.fixedSize = fixed;
        this.props = props;
        this.ex = new HuskyDatabaseExecutor(ex);
        this.openConnections = new FixedSet<Connection>(size);
        this.inUseConnectionNames = new HashSet<String>();
        populate(0, size);
    }

    @Override
    public Connection getConnection() throws SQLException {
        Iterator<Connection> it = openConnections.iterator();
        while(it.hasNext()) {
            Connection next = it.next();
            it.remove();
            if(next.isClosed()) {
                
            }
            this.inUseConnectionNames.add(next.toString());
            return next;
        }
        this.tempConnsMade++;
        return createConnection();
    }
    
    @Override
    public boolean isFixed() {
        return this.fixedSize;
    }
    
    @Override
    public int getOpenConnections() {
        return this.openConnections.size();
    }

    @Override
    public int getMaxSize() {
        return size;
    }
    
    @Override
    public int setMaxSize(int size) {
        if(this.fixedSize) {
            throw new UnsupportedOperationException("This connection pool is a fixed sized pool");
        } else {
            final int oldSize = this.size;
            this.size = size;
            this.populate(oldSize, size);
        }
        return size;
    }

    @Override
    public boolean returnResource(Connection resource) {
        if(!(resource instanceof HuskyConnection)) {
            return false;
        }
        this.inUseConnectionNames.remove(resource.toString());
        return openConnections.add(resource);
    }

    @Override
    public HuskyProperties getProperties() {
        return props;
    }
    
    private synchronized void addResource(Connection conn) {
        this.openConnections.add(new HuskyConnection(conn, this));
    }
    
    private void populate(final int oldSize, final int newSize) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < newSize - oldSize; i++) {
                    addResource(createConnection());
                }
            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }
    
    private Connection createConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Properties cp = new Properties();
            cp.put("user", props.getUsername());
            cp.put("password", props.getPassword());
            cp.put("autoReconnect", "false");
            cp.put("zeroDateTimeBehavior", "convertToNull");
            try {
                conn = DriverManager.getConnection("jdbc:mysql://" + props.getIP() + ":" + 3306 + "/" + props.getDatabase(), cp);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
        }
        return conn;
    }

    @Override
    public Set<String> getUsedConnectionNames() {
        return Collections.unmodifiableSet(this.inUseConnectionNames);
    }

    @Override
    public int getTemporyConnectionsMade() {
        return tempConnsMade;
    }

    @Override
    public DatabaseCallExecutor getDatabaseCallExecutor() {
        return ex;
    }
}
