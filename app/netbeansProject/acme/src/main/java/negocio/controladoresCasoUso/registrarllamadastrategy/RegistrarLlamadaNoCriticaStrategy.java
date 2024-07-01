package negocio.controladoresCasoUso.registrarllamadastrategy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import negocio.modelos.llamada.Consejo;
import persistencia.daos.llamada.DAOConsejo;
import persistencia.daos.llamada.DAOLlamadaNoCritica;
import persistencia.exceptions.DBConnectionException;

public class RegistrarLlamadaNoCriticaStrategy implements RegistrarLlamadaStrategy {
    private final ArrayList<Consejo> consejosDados;
    private final String nombrePaciente;
    private final String descripcionEmergencia;
    private final boolean esLeve;
    
    public RegistrarLlamadaNoCriticaStrategy(ArrayList<Consejo> consejos, String paciente, String descripcionEmergencia,boolean esLeve){
        this.nombrePaciente = paciente;
        this.descripcionEmergencia = descripcionEmergencia;
        this.consejosDados = consejos;
        this.esLeve = esLeve;
    }
    
    @Override
    public void registrarLlamada(String telefOrigen, LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, LocalTime horaFin, String comunicante, String nifOperador) throws DBConnectionException {
        int id = DAOLlamadaNoCritica.registrarLlamadaYDevolverID(telefOrigen, comunicante, fechaInicio, horaInicio, fechaFin, horaFin, nifOperador,nombrePaciente,descripcionEmergencia,esLeve);
        
        for(Consejo c : consejosDados){
            DAOConsejo.registrarConsejo(id, c.getDescripcion(),c.getResultado(),c.soluciona());
        }
    }

}
