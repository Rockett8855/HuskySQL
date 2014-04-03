package com.uemc.huskysql.huskypool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.uemc.huskysql.api.DatabaseCallExecutor;
import com.uemc.huskysql.api.calls.DatabaseCall;
import com.uemc.huskysql.api.calls.DatabaseRetriever;
import com.uemc.huskysql.api.calls.DatabaseUpdater;

public class HuskyDatabaseExecutor implements DatabaseCallExecutor {
    private final ExecutorService asyncExecutorService;
    
    public HuskyDatabaseExecutor(int nThreads) {
        asyncExecutorService = Executors.newFixedThreadPool(nThreads);
    }
    
    public HuskyDatabaseExecutor(ExecutorService service) {
        this.asyncExecutorService = service;
    }
    
    @Override
    public <V> V submit(DatabaseCall call, boolean async) {
        if(call == null) throw new NullPointerException("The call can't be null");
        if(call instanceof DatabaseRetriever<?>) {
            @SuppressWarnings("unchecked")
            DatabaseRetriever<V> retriever = (DatabaseRetriever<V>) call;
            V ret = executeRetriever(retriever, async);
            return ret;
        } else if(call instanceof DatabaseUpdater) {
            executeUpdater((DatabaseUpdater) call, async);
            return null;
        } else {
            throw new IllegalArgumentException("The call is not able to be processed");
        }
    }

    private <V> V executeRetriever(DatabaseRetriever<V> call, boolean async) {
        V ret = null;
        if(async) {
            try { ret = asyncExecutorService.submit(call).get(); } catch (InterruptedException | ExecutionException ignore) { }
        } else {
            try { ret = call.call(); } catch (Exception ignore) { }
        }
        return ret;
    }
    
    private void executeUpdater(DatabaseUpdater call, boolean async) {
        if(async) {
            asyncExecutorService.execute(call);
        } else {
            try { call.run(); } catch (Exception ignore) { }
        }
    }
}
