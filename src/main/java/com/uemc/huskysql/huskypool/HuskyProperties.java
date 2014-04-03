package com.uemc.huskysql.huskypool;

import java.util.HashMap;
import java.util.Map;

public class HuskyProperties {
    private final Map<String, String> vals = new HashMap<String, String>();
    
    private final String KEY_UN = "user";
    private final String KEY_PW = "pass";
    private final String KEY_DB = "databse";
    private final String KEY_IP = "ip";
    
    public HuskyProperties(String username, String password, String database, String ip) {
        this.vals.put(KEY_UN, username);
        this.vals.put(KEY_PW, password);
        this.vals.put(KEY_DB, database);
        this.vals.put(KEY_IP, ip);
    }
    
    public String put(String key, String value) {
        return this.put(key, value, true);
    }
    
    public String put(String key, String value, boolean force) {
        if(force) {
            this.vals.put(key, value);
        } else {
            if(!this.vals.containsKey(key)) {
                this.vals.put(key, value);
            }
        }
        return key;
    }
    
    public String get(String key) {
        return this.vals.get(key);
    }
    
    public String getUsername() {
        return this.vals.get(KEY_UN);
    }
    
    public String getPassword() {
        return this.vals.get(KEY_PW);
    }
    
    public String getDatabase() {
        return this.vals.get(KEY_DB);
    }
    
    public String getIP() {
        return this.vals.get(KEY_IP);
    }
}
