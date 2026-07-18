package dao;

import java.sql.*;


public class DBConnection
{
    public static Connection getConnectionAcc() throws Exception
    {
        //connection code-compulsary for every class
        String url = "jdbc:mysql://localhost:3306/Account";
        String user = "root";//name should be root every time
        String pass = "";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, user, pass);
        return con;
    }

    public static Connection getConnectionTransaction() throws Exception
    {
        //connection code-compulsary for every class
        String url = "jdbc:mysql://localhost:3306/Transaction";
        String user = "root";//name should be root every time
        String pass = "";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, user, pass);
        return con;
    }
}
