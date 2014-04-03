package com.uemc.huskysql.api;

import com.uemc.huskysql.api.calls.DatabaseCall;

public interface DatabaseCallExecutor {
    
    /**
     * Submits a database call to be processed. An updater that is submitted to this method will
     * return null, even if the call was successfully executed.
     * 
     * @param call DatabaseCall to be process
     * @param async If the DatabaseCall should be executed asynchronously
     * @return The value after the DatabaseCall has been processed, can be <code>null</code>
     */
    public <V> V submit(DatabaseCall call, boolean async);
}
