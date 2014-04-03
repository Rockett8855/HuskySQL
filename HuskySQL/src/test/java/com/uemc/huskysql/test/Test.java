package com.uemc.huskysql.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.uemc.huskysql.api.ConnectionPool;
import com.uemc.huskysql.api.calls.retrieval.PreparedStatementRetriever;
import com.uemc.huskysql.huskypool.HuskyPoolFactory;
import com.uemc.huskysql.huskypool.HuskyProperties;

public class Test {
    public static long startTime;
    public static long endTime;
    
    public static void main(String[] args) throws SQLException, InterruptedException, ExecutionException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        final ConnectionPool pool = HuskyPoolFactory.newFixedSizePool(1, new HuskyProperties("matthew", "matthew", "uemc", "localhost"), service);
        Thread.sleep(200L);
        for(int i = 0; i < 1000; i++) {
            PlayerData[] data = pool.getDatabaseCallExecutor().submit(new PreparedStatementRetriever<PlayerData[]>(pool.getConnection(), "SELECT * FROM t_player") {
                @Override
                public PlayerData[] retrieve() throws SQLException {
                    rs = ps.executeQuery();
                    List<PlayerData> ret = new ArrayList<PlayerData>();
                    while(rs.next()) {
                        ret.add(new PlayerData(rs.getString("vc_name"), rs.getString("vc_uuid"), rs.getString("vc_ip")));;
                    }
                    return ret.toArray(new PlayerData[ret.size()]);
                }
            }, true);
            if(System.currentTimeMillis() % 100 == 0) {
                System.out.println("Loaded: " + i);
            }
        }
        System.out.println(pool.getOpenConnections());
        System.out.println(pool.getTemporyConnectionsMade());
        System.exit(0);
    }
}
