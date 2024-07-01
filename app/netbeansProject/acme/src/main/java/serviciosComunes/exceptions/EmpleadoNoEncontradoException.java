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
public class EmpleadoNoEncontradoException extends Exception {
    public EmpleadoNoEncontradoException(String message) {
        super(message);
    }
    
    public EmpleadoNoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
