package com.lqh.ebuyplt_1001p.Controller.DBTools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil
{
    private static String url="jdbc:kingbase8://localhost:54321/db_ebuyplt2";
    private static String user="system";
    private static String password="qh20050908";
    private static String driver="com.kingbase8.Driver";

    public static Connection getConnection()
    {
        Connection con=null;
        try
        {
            Class.forName(driver);
            con= DriverManager.getConnection(url,user,password);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return con;
    }
}
