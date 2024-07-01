package interfaz.paresvistacontrol.atenderllamadaentrante;

import serviciosComunes.exceptions.PolizaNoEncontradaException;
import serviciosComunes.exceptions.RegistrarLlamadaException;
import serviciosComunes.exceptions.TurnoDeOperadorNoEncontradoException;
import interfaz.GestorInterfazUsuario;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import negocio.controladorescasouso.ControladorCasoUsoAtenderLlamadaEntrante;

/**
 * Clase controlador del patron MVC encargada del CU atender llamada
 */
public class CtrlVistaAtenderLlamadaEntrante {

    private final VistaAtenderLlamadaEntrante vista;
    private final ControladorCasoUsoAtenderLlamadaEntrante control;

    public CtrlVistaAtenderLlamadaEntrante(VistaAtenderLlamadaEntrante vista) throws NullPointerException {
        control = new ControladorCasoUsoAtenderLlamadaEntrante();
        this.vista = vista;
    }

    public void procesarRegistrarUnaLlamadaEntrante() {
        try {
            control.estaOperadorEnTurno();
            vista.habilitarConfirmarDatos();
            control.registrarInicioLlamadaEntrante();
        } catch (TurnoDeOperadorNoEncontradoException e) {
            vista.muestraMensajeError(e.getMessage());
        }
    }

    public void procesaConfirmarDatosIntroducidos() {
        //Obtenemos los datos de la vista
        String nombrePaciente = vista.getNombrePaciente();
        String apellidosPaciente = vista.getApellidosPaciente();
        String telefonoOrigen = vista.getTelefOrigen();
        String nombreComunicante = vista.getComunicante();
        String numeroPoliza = vista.getPoliza();
       
        //Le damos los datos necesarios a controlado de CU
        control.setTelefonoOrigen(telefonoOrigen);
        control.setNombreComunicante(nombreComunicante);
        
        //Si no se ha introducido alguno de los datos se muestra el mensaje de error
        if (telefonoOrigen.isEmpty() || nombreComunicante.isEmpty() || numeroPoliza.isEmpty() || nombrePaciente.isEmpty() || apellidosPaciente.isEmpty()) {
            vista.muestraMensajeError("No has introducido los datos solicitados");
        } else {
            try {
                LocalDate fechaDeNacimiento = LocalDate.parse(vista.getFechaNacimiento());
                control.comprobarPolizaAsegurado(numeroPoliza, nombrePaciente, apellidosPaciente, fechaDeNacimiento);
                vista.muestraMensajeError("");
                vista.fijarDatosInicioLlamada();
                vista.habilitarDescripcionEmergencia();
            } catch (DateTimeParseException e) {
                vista.muestraMensajeError("El formato de fecha de nacimiento es incorrecto, correccion: YYYY-MM-DD");
            } catch (PolizaNoEncontradaException e) {
                vista.redirijirLlamada();
                GestorInterfazUsuario.getInstance().mostrarVistaOpcionesOperador();
            } catch (RegistrarLlamadaException rle) {
                vista.muestraMensajeError(rle.getMessage());
            }
        }
    }

    public void procesaIntroducirDescripcionLlamada() {
        String textoDescripcion = vista.getDescripcionEmergencia();
        boolean esCritica = vista.esCritica();
        if (textoDescripcion.equals("")) {
            vista.muestraMensajeError("Debes introducir la descripción sobre la emergencia");
			return;
        } 
            vista.muestraMensajeError("");
            try {
                if (control.comprobarSiEsLlamadaCritica(textoDescripcion, esCritica)) {
                    vista.redirijirLlamada();
                    GestorInterfazUsuario.getInstance().mostrarVistaOpcionesOperador();
                } else {
                    vista.habilitarConsejos();
                    vista.fijarDescripcionEmergencia();
                }
            } catch (RegistrarLlamadaException rle) {
                vista.muestraMensajeError(rle.getMessage());
            }
    }

    public void procesaGuardarConsejoIntroducido() {
        String descripcion = vista.getDescripcionConsejo();
        String resultado = vista.getResultadoConsejo();
        boolean soluciona = vista.getSolucionaConsejo();
        boolean requiereOperativo = vista.getRequiereOperativo();

        if (descripcion.equals("") || resultado.equals("")) {
            vista.setMensajeConsejos("Introduce todos los datos relativos al consejo");
			return;
        } 
            try {
				// solo si el consejo es el ultimo o se requiere operativo
                if (control.addConsejoOperador(descripcion, resultado, soluciona, requiereOperativo)) {
                    if (!requiereOperativo) {
                        vista.llamadaFinalizadaConExito();
                    } else {
                        vista.activacionOperativo();
                    }
                    GestorInterfazUsuario.getInstance().mostrarVistaOpcionesOperador();
                }
            	vista.setMensajeConsejos("Consejo añadido a los datos de la llamada");
            } catch (RegistrarLlamadaException rle) {
                vista.muestraMensajeError(rle.getMessage());
            }
            vista.resetearConsejo();
    }

}
