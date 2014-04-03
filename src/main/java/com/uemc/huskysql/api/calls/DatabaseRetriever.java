package com.uemc.huskysql.api.calls;

import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * Retriever class to get data from a database, almost always returns a ResultSet
 * and should be used to get data, it wouldn't be invalid to update data, but
 * DatabaseUpdater would be a more understandable way to update data in the database
 *
 * @param <V> Object that will be retieved from the database
 */
public interface DatabaseRetriever<V> extends DatabaseCall, Callable<V> {
    
    /**
     * Very similar to {@link Callable} where this run method is called on run
     * @return Value after running
     * @throws SQLException if an error occurs while the method is interacting with the database
     */
    V retrieve() throws SQLException;
    
}
