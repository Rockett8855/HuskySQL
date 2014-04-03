package com.uemc.huskysql.api.calls;

/**
 * Updater interface used for updating some data in a database, this class should be used mostly for <code>UPDATE</code> SQL statements
 */
public interface DatabaseUpdater extends DatabaseCall, Runnable {

    /**
     * Updates the database with some information, shouldn't retrieve any data, just update
     */
    void update();
    
}
