package com.uemc.huskysql.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import com.uemc.huskysql.huskypool.HuskyProperties;

public interface ConnectionPool {
    
    /**
     * Gets an unused connection from a pool of connections. <br>
     * If the pool has no open connections, it will create another connection that won't be added when it's done, 
     * This temporary connection will be disposed on close
     * 
     * @return A thread safe connection
     */
    Connection getConnection();
    
    /**
     * Gets the number of open connections ready to be used
     * 
     * @return Number of unused connections
     */
    int getOpenConnections();
    
    /**
     * Gets the max amount of connections this pool can hold
     * 
     * @return Max number of connections
     */
    int getMaxSize();
    
    /**
     * Sets the max amount of open connections that can be used
     * 
     * @param size New size
     * @return New size
     * @throws UnsupportedOperationException If the pool is a fixed sized pool
     */
    int setMaxSize(int size);
    
    /**
     * Gets the properties of the pool
     * 
     * @return Properties of the pool
     */
    HuskyProperties getProperties();
    
    /**
     * Return a resource after it has be relieved of it's duty
     * 
     * @param resource Resource that just opened
     * @return True if the resource was re-added, false otherwise
     */
    boolean returnResource(Connection resource);
    
    /**
     * If the pool is a fixed size pool, or can be adjusted
     * 
     * @return True if the pool is of a fixed size, false otherwise
     */
    boolean isFixed();
    
    /**
     * Gets a Set of names of connections that are in use
     * 
     * @return Set of names of connections that are in use
     */
    Set<String> getUsedConnectionNames();
    
    /**
     * Gets the amount of temporary connections made
     * 
     * @return Amount of temporary connections created
     */
    int getTemporyConnectionsMade();
    
    /**
     * Gets the executor that will call all the {@link DatabaseCall} subclasses
     * 
     * @return Executor to submit DatabaseCalls to
     */
    DatabaseCallExecutor getDatabaseCallExecutor();
    
    /**
     * Close the connection pool, closes all connections
     * 
     * @throws SQLException if something goes wrong closing the connections
     */
    void close() throws SQLException;
}
