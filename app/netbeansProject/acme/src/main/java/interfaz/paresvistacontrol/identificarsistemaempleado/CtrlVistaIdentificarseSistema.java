package interfaz.paresvistacontrol.identificarsistemaempleado;

import serviciosComunes.exceptions.EmpleadoNoActivoException;
import serviciosComunes.exceptions.EmpleadoNoEncontradoException;
import interfaz.GestorInterfazUsuario;
import negocio.controladorescasouso.ControladorCasoUsoIdentificarseSistema;

/**
 * Clase controladora del patron modelo vista controlador del caso de uso identificar empleado en el sistema
 */
public class CtrlVistaIdentificarseSistema {

    private final VistaIdentificarseSistema vista;
    private final ControladorCasoUsoIdentificarseSistema controlador;

    public CtrlVistaIdentificarseSistema(VistaIdentificarseSistema vista) throws NullPointerException {
        controlador = new ControladorCasoUsoIdentificarseSistema();
        this.vista = vista;
    }

    public void intentaIdentificarse() {
        
        //obtener el nif y pass de la vista
        String nif, passwd;
        nif = vista.getNif();
        passwd = vista.getPassword();
        
        //comprobacion de no este vacios
        if (nif.isEmpty()) {
            vista.muestraMensajeError("Introduzca el NIF");
            return;
        }
        if (passwd.isEmpty()) {
            vista.muestraMensajeError("Introduzca la contraseña");
            return;
        }
        //intentar obtener el rol
        try {
            String role= controlador.obtenerRolEmpleadoSistema(nif, passwd);
            //Muestra la vista segun el tipo de empleado q sea
            GestorInterfazUsuario.getInstance().mostrarVistaSegunRol(role);
        } catch (EmpleadoNoEncontradoException | EmpleadoNoActivoException e) {
            vista.muestraMensajeError(e.getMessage());
        }


}


}
