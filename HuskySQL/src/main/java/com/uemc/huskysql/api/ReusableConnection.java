package com.uemc.huskysql.api;

public interface ReusableConnection {
    /**
     * Return this connection to the pool that it came from, for reuse
     * 
     * @return If the connection was successfully re-added
     */
    boolean restore();
}
