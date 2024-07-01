/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DBAccess;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import persistencia.exceptions.DBConnectionException;

/**
 *
 * @author diego
 */
public class DBConnection {
    
    // class level (singleton)
    private static DBConnection theDBConnection;
    
    public static DBConnection getInstance() throws DBConnectionException {
        if (theDBConnection == null) {
            Properties prop = new Properties();
            InputStream read; 
            String url, user, password;
            try {                   
                read = DBConnection.class.getResourceAsStream("/config.db"); // Si se usa maven como gestor del proyecto, este fichero debe estar en resources
                prop.load(read);
                url = prop.getProperty("url");
                user = prop.getProperty("user");
                password = prop.getProperty("password");
                read.close(); 
                theDBConnection = new DBConnection(url, user, password);
            } catch (FileNotFoundException e) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "DB configuration file not found.", e);
                throw new DBConnectionException("DB configuration file not found.", e);
            } catch (IOException e) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Couldn't read DB configuration file.", e);
                throw new DBConnectionException("Couldn't read DB configuration file.", e);
            }      
        }
        return theDBConnection;
    }

    // instance level
    private Connection conn;
    private String url;
    private String user;
    private String password;

    private DBConnection(String url, String user, String password) throws DBConnectionException {        
        this.url = url;
        this.user = user;
        this.password = password;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver"); 
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Derby driver not found.", ex);
            throw new DBConnectionException("Derby driver not found.", ex);
        }
    }
    
    public void openConnection() throws DBConnectionException {
        try {
            conn = DriverManager.getConnection(url, user, password);
        }
        catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Failed to open connection to the database.", ex);
            throw new DBConnectionException("Failed to open connection to the database.", ex);
        }
    }
    
    public void closeConnection() throws DBConnectionException {
        try {
            conn.close();
        } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Failed to close connection to the database.", ex);
                throw new DBConnectionException("Failed to close connection to the database.", ex);
        }
    }

    public PreparedStatement getStatement(String s) throws DBConnectionException {
        PreparedStatement stmt = null;
        try {
                stmt = conn.prepareStatement(s);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, "Failed to prepare statement.", ex);
            throw new DBConnectionException("Failed to prepare statement.", ex);
        }
        return stmt;
    }
    
}
