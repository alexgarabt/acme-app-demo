package negocio.controladorescasouso;

import serviciosComunes.exceptions.PolizaNoEncontradaException;
import serviciosComunes.exceptions.RegistrarLlamadaException;
import serviciosComunes.exceptions.TurnoDeOperadorNoEncontradoException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import negocio.modelos.Session;
import negocio.modelos.llamada.Consejo;
import negocio.controladoresCasoUso.registrarllamadastrategy.RegistrarLlamadaCriticaStrategy;
import negocio.controladoresCasoUso.registrarllamadastrategy.RegistrarLlamadaInfundadaStrategy;
import negocio.controladoresCasoUso.registrarllamadastrategy.RegistrarLlamadaNoCriticaStrategy;
import negocio.modelos.persona.Asegurado;
import negocio.modelos.persona.Empleado;
import persistencia.daos.operador.DAOTurnoDeOperador;
import negocio.modelos.turnoOperador.TipoDeTurnoOperador;
import negocio.modelos.turnoOperador.TurnoDeOperador;
import persistencia.daos.persona.DAOAsegurado;
import persistencia.daos.persona.DAOPersona;
import persistencia.daos.persona.DAOPoliza;
import persistencia.exceptions.DBConnectionException;
import negocio.controladoresCasoUso.registrarllamadastrategy.RegistrarLlamadaStrategy;

/**
 * Clase tipo parton controlador, encargada de gestionar el Caso de Uso de anteder llamda entrante
 */
public class ControladorCasoUsoAtenderLlamadaEntrante {

    String telefonoDeOrigen;
    String nombreComunicante;
    String emergenciaDescripcion;
    TurnoDeOperador turnoActual;
    Asegurado aseguradoLlamada;
    ArrayList<Consejo> consejosOtorgadosDelOperador = new ArrayList<>();
    LocalDate fechaInicioLlamada;
    LocalTime horaInicioLlamada;

    /**
     * Devuelve si el operador que esta usando la aplicacion esta en turno
     * @return True si el operador en la sesion esta en turno
     * @throws TurnoDeOperadorNoEncontradoException cuando la base de datos no se encuentra disponible
     */
    public void estaOperadorEnTurno() throws TurnoDeOperadorNoEncontradoException {
        
        
        TipoDeTurnoOperador tipoActual;
        LocalDate fechaActual = LocalDate.now();
        int horaActual = LocalTime.now().getHour();

        //Comprobamos el tipo de turno en el que se encuentra actualmente el operador
        if (horaActual > 0 && horaActual < 7) tipoActual = TipoDeTurnoOperador.DeNoche23;
        else if (horaActual >= 7 && horaActual < 15) tipoActual = TipoDeTurnoOperador.DeMañana7;
        else if (horaActual >= 15 && horaActual < 23) tipoActual = TipoDeTurnoOperador.DeTarde15;
        else tipoActual = TipoDeTurnoOperador.DeNoche23;
        
        //Obetenemos el empleado en la sesion
        Empleado empleadoSesion = Session.getInstance().getEmpleado();

        try {
            String turnoActualString = DAOTurnoDeOperador.consultaTurnoDeOperadorPorFechaYTipo(fechaActual, tipoActual.toString());
            
            //debug info throw new TurnoDeOperadorNoEncontradoException("Este operador no se encuentra en turno: "+ fechaActual+","+tipoActual);
            if (turnoActualString.equals("")) throw new TurnoDeOperadorNoEncontradoException("Este operador no se encuentra en turno: ");
            turnoActual = new TurnoDeOperador(turnoActualString);
            
            if (!turnoActual.getOperadores().contains(empleadoSesion)) throw new TurnoDeOperadorNoEncontradoException("Este operador no se encuentra en turno");
            
         } catch (DBConnectionException dbe) {
             Logger.getLogger(ControladorCasoUsoAtenderLlamadaEntrante.class.getName()).log(Level.SEVERE, "Error on DBConnection from DAOTurnoDeOperador.", dbe);
            throw new TurnoDeOperadorNoEncontradoException("No se encuentra el turno del operador debido a un error en la BD", dbe);
         }
    }

    public void comprobarPolizaAsegurado(String poliza, String nombrePaciente, String apellidosPaciente, LocalDate fechaNacimiento) throws PolizaNoEncontradaException,RegistrarLlamadaException {
        try {
            String nif = DAOPersona.consultaNifPorNombreYFecha(nombrePaciente, apellidosPaciente, fechaNacimiento);
            String polizaJSON = DAOPoliza.consultaPolizaPorAseguradoYNumero(nif, poliza);
            if (polizaJSON.equals("")) {
                RegistrarLlamadaStrategy llamadaInfundada = new RegistrarLlamadaInfundadaStrategy();
                registrarLlamadaEntrante(llamadaInfundada);
                throw new PolizaNoEncontradaException("No se ha encontrado ninguna poliza para el asegurado introducido");
            }
            //Si existe una poliza asociada a el nif entoces es aseguradoLlamada
            aseguradoLlamada = new Asegurado(DAOAsegurado.consultaAseguradoPorNif(nif));
        } catch (DBConnectionException dbe) {
            Logger.getLogger(ControladorCasoUsoAtenderLlamadaEntrante.class.getName()).log(Level.SEVERE, "Error on DBConnection from DAOPersona or DAOPoliza.", dbe);
            throw new PolizaNoEncontradaException("No se ha podido comprobar si existe poliza por error en conexión a base de datos", dbe);
        }
    }

    public void registrarInicioLlamadaEntrante() {
        fechaInicioLlamada = LocalDate.now();
        horaInicioLlamada = LocalTime.now();
    }

    public void registrarLlamadaEntrante(RegistrarLlamadaStrategy strategy) throws RegistrarLlamadaException{
        LocalDate fechaFinLlamada = LocalDate.now();
        LocalTime horaFinLlamada = LocalTime.now();
        String nifDelOperador = Session.getInstance().getEmpleado().getNif();
        try {
            strategy.registrarLlamada(telefonoDeOrigen, fechaInicioLlamada, horaInicioLlamada, fechaFinLlamada, horaFinLlamada, nombreComunicante, nifDelOperador);
        } catch (DBConnectionException dbe) {
            Logger.getLogger(ControladorCasoUsoAtenderLlamadaEntrante.class.getName()).log(Level.SEVERE, "Error on DBConnection when inserting", dbe);
            throw new RegistrarLlamadaException("No se pudo registrar llamada debido a un",dbe);
        }

    }

    public void setTelefonoOrigen(String telefOrigen) {
        this.telefonoDeOrigen = telefOrigen;
    }

    public void setNombreComunicante(String comunicante) {
        this.nombreComunicante = comunicante;
    }

    public boolean comprobarSiEsLlamadaCritica(String descripcion, boolean critica) throws RegistrarLlamadaException {
        String nifDelAsegurado = aseguradoLlamada.getNif();
        emergenciaDescripcion = descripcion;
        if (critica) {
            RegistrarLlamadaStrategy strat = new RegistrarLlamadaCriticaStrategy(nifDelAsegurado, descripcion);
            registrarLlamadaEntrante(strat);
        }
        return critica;
    }

    //devuelve true si soluciona la emergencia
    public boolean addConsejoOperador(String descripcion, String resultado, boolean soluciona, boolean requiereOperativo) throws RegistrarLlamadaException {
        Consejo consejo = new Consejo(descripcion, soluciona, resultado);
        consejosOtorgadosDelOperador.add(consejo);
        if (soluciona || requiereOperativo) {
            RegistrarLlamadaStrategy strat = new RegistrarLlamadaNoCriticaStrategy(consejosOtorgadosDelOperador, aseguradoLlamada.getNif(), emergenciaDescripcion, !requiereOperativo);
            registrarLlamadaEntrante(strat);
            return true;
        }
        return false;
    }
}
