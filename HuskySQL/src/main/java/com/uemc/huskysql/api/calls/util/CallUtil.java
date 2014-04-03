package com.uemc.huskysql.api.calls.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CallUtil {
    public static void softClose(Connection conn, Statement s) {
        softClose(conn, s, null);
    }
    
    public static void softClose(Connection conn, Statement s, ResultSet rs) {
        softClose(conn, s, null, null, rs);
    }
    
    public static void softClose(Connection conn, Statement s, PreparedStatement ps, CallableStatement cs, ResultSet rs) {
        if(rs != null) try { rs.close(); } catch (SQLException ignore) {}
        if(ps != null) try { ps.close(); } catch (SQLException ignore) {}
        if(cs != null) try { cs.close(); } catch (SQLException ignore) {}
        if(s != null) try { s.close(); } catch (SQLException ignore) {}
        if(conn != null) try { conn.close(); } catch (SQLException ignore) {}
    }
}
