package com.uemc.huskysql.huskypool;

import java.util.concurrent.ExecutorService;

public class HuskyPoolFactory {
    public static HuskyPool getDefaultPool(HuskyProperties props, ExecutorService ex) {
        return newFixedSizePool(50, props, ex);
    }
    
    public static HuskyPool newPool(int size, HuskyProperties props, ExecutorService ex) {
        return new HuskyPool(size, props, false, ex);
    }
    
    public static HuskyPool newFixedSizePool(int size, HuskyProperties props, ExecutorService ex) {
        return new HuskyPool(size, props, true, ex);
    }
}
