/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serviciosComunes.exceptions;


/**
 *
 * @author diego
 *
 * */
public class EmpleadoNoActivoException extends Exception {
    public EmpleadoNoActivoException(String message) {
        super(message);
    }
    
    public EmpleadoNoActivoException(String message, Throwable cause) {
        super(message, cause);
    }
}
