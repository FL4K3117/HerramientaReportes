package com.savadr.HerramientaFX8.Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Yair-PC
 */
public class DBConector {

    private static Connection con;
    private static DBConector instance;
    private boolean isConnectedSurtidora;
    private boolean isConnectedAdrimar;
    // Constructor
    private DBConector() {
        isConnectedSurtidora = false;
        isConnectedAdrimar = false;
    }
    // Method to get the instance of the class
    public static DBConector getInstance() {
        if (instance == null) {
            instance = new DBConector();
        }
        return instance;
    }
    // Method to get the connection to the database
    public void getDBConn(String url, String usuario, String contrasena) {
        synchronized (DBConector.class) {
            try {
                if (con == null || con.isClosed()) {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection(url, usuario, contrasena);
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getCon() {
        return con;
    }

    public static void setCon(Connection aCon) {
        con = aCon;
    }
    // Method to close the connection to the database
    public static void closeConnection() {
        try {
            if(con != null) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Getters y Setters for the connection status and server
    public boolean isConnectedSurtidora() {
        return isConnectedSurtidora;
    }

    public void setIsConnectedSurtidora(boolean isConnectedSurtidora) {
        this.isConnectedSurtidora = isConnectedSurtidora;
    }

    public boolean isConnectedAdrimar() {
        return isConnectedAdrimar;
    }

    public void setIsConnectedAdrimar(boolean isConnectedAdrimar) {
        this.isConnectedAdrimar = isConnectedAdrimar;
    }

}
