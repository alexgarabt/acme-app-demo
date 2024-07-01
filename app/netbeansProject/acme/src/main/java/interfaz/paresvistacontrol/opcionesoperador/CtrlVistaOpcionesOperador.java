package interfaz.paresvistacontrol.opcionesoperador;

import interfaz.GestorInterfazUsuario;

/**
 * Clase controlador del MVC encargada de gestionar la vista de opciones del operador
 */
public class CtrlVistaOpcionesOperador {

    private final VistaOpcionesOperador vista;

    public CtrlVistaOpcionesOperador (VistaOpcionesOperador vista) throws NullPointerException {
        this.vista = vista;
    }

    public void procesaAtenderLlamadaEntrante(){
        GestorInterfazUsuario.getInstance().mostrarVistaAtenderLlamadaEntrante();
    }

}
