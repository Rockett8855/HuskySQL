package com.uemc.huskysql.api.calls.util;

/**
 * Holds a list of the possible statement types to be used in DatabaseCall
 */
public enum StatementType {
    /**
     * @see java.sql.Statement
     */
    STATEMENT,
    /**
     * @see java.sql.PreparedStatement
     */
    PREPARED_STATEMENT,
    /**
     * @see java.sql.CallableStatement
     */
    CALLABLE_STATEMENT;
}
