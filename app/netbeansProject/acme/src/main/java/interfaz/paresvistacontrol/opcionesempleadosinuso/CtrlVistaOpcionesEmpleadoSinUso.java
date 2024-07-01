package interfaz.paresvistacontrol.opcionesempleadosinuso;

import interfaz.GestorInterfazUsuario;
import negocio.modelos.Session;

/**
 * Clase mock de como seria el area de un empleado que aun no tiene casos de uso
 */
public class CtrlVistaOpcionesEmpleadoSinUso {

    private final VistaOpcionesEmpleadoSinUso vista;

    public CtrlVistaOpcionesEmpleadoSinUso (VistaOpcionesEmpleadoSinUso vista) throws NullPointerException {
        this.vista = vista;
    }
    
    /**
     * Sale a la vista de identificarse
     */
    public void procesaAccionSalirAlLogin(){
        //Destruir el empleado almacenado
        Session.getInstance().setEmpleado(null);
        GestorInterfazUsuario.getInstance().mostrarVistaIdentificarseSistema();
    }
}
