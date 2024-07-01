package negocio.modelos;

import negocio.modelos.persona.Empleado;

/**
 * Clase usa el patron singleton para almacenar el empleado con el que se ha iniciado sesion
 */
public class Session {

    private static Session sesion; //Instancia
    private Empleado empleado; 	   //Empleado que se almacenara
    private Session(){}
    
    public static Session getInstance() {
        if (sesion == null) sesion = new Session();
        return sesion;
    }
  
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Empleado getEmpleado() {
        return this.empleado;
    }
}
