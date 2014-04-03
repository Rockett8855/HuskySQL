package com.uemc.huskysql.test;

import java.sql.SQLException;
import java.util.concurrent.Executors;

import com.uemc.huskysql.api.ConnectionPool;
import com.uemc.huskysql.api.calls.DatabaseCall;
import com.uemc.huskysql.api.calls.retrieval.PreparedStatementRetriever;
import com.uemc.huskysql.api.calls.update.PreparedStatementUpdater;
import com.uemc.huskysql.huskypool.HuskyPoolFactory;
import com.uemc.huskysql.huskypool.HuskyProperties;

public class Test {
    
    public static void main(String[] args) throws SQLException {
        final ConnectionPool pool = HuskyPoolFactory.newPool(10, new HuskyProperties("matthew", "matthew", "uemc", "localhost"), Executors.newFixedThreadPool(5));
        DatabaseCall nameRetriver = new PreparedStatementRetriever<String>(pool.getConnection(), "SELECT vc_name FROM t_player WHERE vc_ip = ? LIMIT 1") {
            @Override
            public String retrieve() throws SQLException {
                String theName = "";
                this.ps.setString(1, "anIp");
                this.rs = ps.executeQuery();
                if(rs.first()) {
                    theName = rs.getString(1);
                } else {
                    DatabaseCall update = new PreparedStatementUpdater(pool.getConnection(), "INSERT INTO t_player(`vc_name`, `vc_ip`, `vc_uuid`) VALUES (?,?,?);") {
                        @Override
                        public void update() throws SQLException {
                            this.ps.setString(1, "aName");
                            this.ps.setString(2, "anIp");
                            this.ps.setString(3, "aUuid");
                            this.ps.execute();
                        }
                    };
                    //Keep synchronous with this thread
                    pool.getDatabaseCallExecutor().submit(update, false);
                    //Call this again, synchronous with this thread
                    theName = pool.getDatabaseCallExecutor().submit(this, false);
                }
                return theName;
            }
        };
        String name = pool.getDatabaseCallExecutor().submit(nameRetriver, true);
        System.out.println(name);
    }

}
