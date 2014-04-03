HuskySQL
========

This is a ConnectionPool for Java and MySQL, I created this because I wanted to see if I could make my own ConnectionPool project other than using others.

Features
--------

* Easy instantiation, There are many ways to instantiate the HuskyPool with the HuskyPoolFactory
```java
public static final ConnectionPool CONNECTION_POOL = HuskyPoolFactory.newPool(10, new HuskyProperties("un", "pw", "db", "ip"), yourExecutorService);
```
* Has DatabaseCalls's, abstract classes that are used to interact with the database. This class is very similar to the native Runnable and Callable class in Java. These classes are especially useful because they automatically close/return the resources used back to ConnectionPool even without being called, so it is impossible to forget to close a Connection:
```java
DatabaseCall nameRetriver = new PreparedStatementRetriever<String>(pool.getConnection(), "SELECT vc_name FROM t_player WHERE vc_ip = ? LIMIT 1") {
    @Override
    public String retrieve() throws SQLException {
        String theName = "";
        this.ps.setString(1, "anIp");
        this.rs = ps.executeQuery();
        if(rs.first()) {
            theName = rs.getString(1);
        } else {
            theName = "Doesn't exist";
        }
        return theName;
    }
};
String name = pool.getDatabaseCallExecutor().submit(nameRetriver, true);
```
* Using the DatabaseCallExecutor the user can choose whether to run their tasks asynchronously
* Allows users to call DatabaseCalls within database calls, allowing users to get data, and update if need be, Expanding on the first example:
```java
DatabaseCall nameRetriver = new PreparedStatementRetriever<String>(pool.getConnection(), "SELECT vc_name FROM t_player WHERE vc_ip = ? LIMIT 1") {
    @Override
    public String retrieve() throws SQLException {
        String theName = "";
        this.ps.setString(1, "anIp");
        this.rs = this.ps.executeQuery();
        if(this.rs.first()) {
            theName = this.rs.getString(1);
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
//Calls the whole task asynchronously
String name = pool.getDatabaseCallExecutor().submit(nameRetriver, true);
```
