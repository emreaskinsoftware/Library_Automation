
package com.mycompany.libraryautomation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DataBaseHelper {
  private static String DB_URL = "jdbc:mysql://localhost:3306/LibraryAutomation";
    private static String DB_USER = "YOUR USERNAME";
    private static String DB_PASS = "YOURPASSWORD";
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(getDB_URL(), getDB_USER(), getDB_PASS());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
 /**
     * @return the DB_URL
     */
    public static String getDB_URL() {
        return DB_URL;
    }

    /**
     * @param aDB_URL the DB_URL to set
     */
    public static void setDB_URL(String aDB_URL) {
        DB_URL = aDB_URL;
    }

    /**
     * @return the DB_USER
     */
    public static String getDB_USER() {
        return DB_USER;
    }

    /**
     * @param aDB_USER the DB_USER to set
     */
    public static void setDB_USER(String aDB_USER) {
        DB_USER = aDB_USER;
    }

    /**
     * @return the DB_PASS
     */
    public static String getDB_PASS() {
        return DB_PASS;
    }

    /**
     * @param aDB_PASS the DB_PASS to set
     */
    public static void setDB_PASS(String aDB_PASS) {
        DB_PASS = aDB_PASS;
    }
}
