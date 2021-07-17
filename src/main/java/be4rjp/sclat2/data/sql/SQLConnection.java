package be4rjp.sclat2.data.sql;

import be4rjp.sclat2.Sclat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLConnection {
    private final String ip, port, database, username, password;
    
    private Connection connection = null;
    private boolean connected = false;
    
    
    public SQLConnection(String ip, String port, String database, String username, String password) throws Exception{
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    
        connection = DriverManager.getConnection("jdbc:mysql://" + this.ip + ":" + this.port + "/" + this.database + "?autoReconnect=true&useSSL=false", this.username, this.password);
        if (connection != null) {
            Sclat.getPlugin().getLogger().info("Connected to MySQL database.");
            this.connected = true;
        } else {
            Sclat.getPlugin().getLogger().warning("Failed to connect to MySQL database!");
            this.connected = false;
        }
    }
    
    
    public int getInt(String table, String column, String searchColumn, String searchColumnValue, String notExistExecute) throws Exception{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
        if(!resultSet.next()){
            execute(notExistExecute);
            resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
            resultSet.next();
        }
        int i = resultSet.getInt(column);
        resultSet.close();
        statement.close();
        return i;
    }
    
    
    public short getShort(String table, String column, String searchColumn, String searchColumnValue, String notExistExecute) throws Exception{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
        if(!resultSet.next()){
            execute(notExistExecute);
            resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
            resultSet.next();
        }
        short s = resultSet.getShort(column);
        resultSet.close();
        statement.close();
        return s;
    }
    
    
    public byte getByte(String table, String column, String searchColumn, String searchColumnValue, String notExistExecute) throws Exception{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
        if(!resultSet.next()){
            execute(notExistExecute);
            resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
            resultSet.next();
        }
        byte b = resultSet.getByte(column);
        resultSet.close();
        statement.close();
        return b;
    }
    
    
    public byte[] getByteArray(String table, String column, String searchColumn, String searchColumnValue, String notExistExecute) throws Exception{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
        if(!resultSet.next()){
            execute(notExistExecute);
            resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE " + searchColumn + " = '" + searchColumnValue + "';");
            resultSet.next();
        }
        byte[] bytes = resultSet.getBytes(column);
        resultSet.close();
        statement.close();
        return bytes;
    }
    
    
    public void updateValue(String tableName, String Value, String search) throws Exception{
        execute("UPDATE " + tableName +  " SET " + Value + " WHERE " + search + ";");
    }
    
    public void execute(String cmd) throws Exception{
        Statement statement = connection.createStatement();
        statement.execute(cmd);
        statement.close();
    }
    
    public void close() throws Exception{
        this.connection.close();
    }
    
    public boolean getConnected(){return this.connected;}
}
