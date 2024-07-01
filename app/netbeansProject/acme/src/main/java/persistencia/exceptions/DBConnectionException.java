/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.exceptions;

/**
 *
 * @author diego
 *
 * */
public class DBConnectionException extends Exception {
    public DBConnectionException(String message) {
        super(message);
    }
    
    public DBConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
