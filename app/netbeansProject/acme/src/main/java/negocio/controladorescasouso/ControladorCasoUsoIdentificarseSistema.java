package negocio.controladorescasouso;

import serviciosComunes.exceptions.EmpleadoNoActivoException;
import serviciosComunes.exceptions.EmpleadoNoEncontradoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.modelos.persona.Empleado;
import negocio.modelos.Session;
import persistencia.daos.persona.DAOEmpleado;
import persistencia.exceptions.DBConnectionException;

/**
 * Clase controladora del caso de uso identificarse en el sistema
 */
public class ControladorCasoUsoIdentificarseSistema {

    public String obtenerRolEmpleadoSistema(String nif, String pass) throws EmpleadoNoEncontradoException, EmpleadoNoActivoException{
        try{
        String empleadoJson = DAOEmpleado.consultaEmpleadoPorLoginYPassword(nif,pass);
        //si existe
        if (!empleadoJson.equals("")){
            Empleado emp = new Empleado(empleadoJson);
            if(emp.estaEnActivo()){
                //Se guarda en la sesion actual para evitar tener que volver a identificarle
                Session.getInstance().setEmpleado(emp);
                return emp.getRolActual().toString();
            }
            
            else{
                throw new EmpleadoNoActivoException("Este empleado esta inactivo, no puede acceder");
            }
        //s no existe 
        }else{
            throw new EmpleadoNoEncontradoException("No existe ningun empleado con las credenciales introducidas");
        }
        }catch(DBConnectionException e){
            Logger.getLogger(ControladorCasoUsoIdentificarseSistema.class.getName()).log(Level.SEVERE, "Error on DBConnection from DAOEmpleado.", e);
            throw new EmpleadoNoEncontradoException("No se ha encontrado un empleado por error en DBConnection.",e);
        }
        
    }
}
