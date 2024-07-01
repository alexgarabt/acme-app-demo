package interfaz;

import interfaz.paresvistacontrol.opcionesoperador.VistaOpcionesOperador;
import javax.swing.JFrame;
import interfaz.paresvistacontrol.identificarsistemaempleado.VistaIdentificarseSistema;
import interfaz.paresvistacontrol.atenderllamadaentrante.VistaAtenderLlamadaEntrante;
import interfaz.paresvistacontrol.opcionesempleadosinuso.VistaOpcionesEmpleadoSinUso;

/**
 * Clase patron singleton encargada de manegar las vistas que se muestran al empleado
 */
public class GestorInterfazUsuario {

    private static JFrame ventanaActual;
    private static GestorInterfazUsuario gestorInstancia;

    private GestorInterfazUsuario() {
    }

    public static GestorInterfazUsuario getInstance() {
        if (gestorInstancia == null) {
            gestorInstancia = new GestorInterfazUsuario();
        }
        return gestorInstancia;
    }

    public void mostrarVistaIdentificarseSistema() {
        ocultarVista();
        ventanaActual = new VistaIdentificarseSistema();
        ventanaActual.setVisible(true);
    }
    
    /**
     * Oculta la vista actual
     */
    public void ocultarVista() {
        if (ventanaActual != null) {
            ventanaActual.setVisible(false);
        }
    }

    /**
     * Se usa para mostrar la vista al empleado que no tiene casos uso asignados
     */
    public void mostrarVistaOpcionesEmpleadoSinUso() {
        ocultarVista();
        ventanaActual = new VistaOpcionesEmpleadoSinUso();
        ventanaActual.setVisible(true);
    }
    
    public void mostrarVistaOpcionesOperador(){
        ocultarVista();
        ventanaActual = new VistaOpcionesOperador();
        ventanaActual.setVisible(true);
    }

    public void mostrarVistaAtenderLlamadaEntrante(){
        ocultarVista();
        ventanaActual = new VistaAtenderLlamadaEntrante();
        ventanaActual.setVisible(true);
    }
    
    /**
     * Muestra la vista segun el tipo de rol del empleado
     * @param rol rol del empleado
     */
    public void mostrarVistaSegunRol(String rol){
        switch(rol) {
            case "Operador":
                mostrarVistaOpcionesOperador();
                break;
            case "Conductor":
            case "Médico":
            case "Gerente":
                mostrarVistaOpcionesEmpleadoSinUso();
        }
    }


}
