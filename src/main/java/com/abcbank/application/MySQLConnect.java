
package com.abcbank.application;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnect{
    public static Connection connectDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/saif12", "root", "admin");
        }
        catch(Exception e){
            return null;
        }            
    }
}