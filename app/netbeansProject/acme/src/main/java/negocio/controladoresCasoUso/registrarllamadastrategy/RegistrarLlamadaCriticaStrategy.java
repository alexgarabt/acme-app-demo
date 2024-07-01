package negocio.controladoresCasoUso.registrarllamadastrategy;

import java.time.LocalDate;
import java.time.LocalTime;
import persistencia.daos.llamada.DAOLlamadaCritica;
import persistencia.exceptions.DBConnectionException;

public class RegistrarLlamadaCriticaStrategy implements RegistrarLlamadaStrategy{
    private final String nombrePaciente;
    private final String emergenciaDescripcion;
    
    public RegistrarLlamadaCriticaStrategy(String paciente, String descripcionEmergencia){
        this.nombrePaciente = paciente;
        this.emergenciaDescripcion = descripcionEmergencia;
    }
    
    @Override
    public void registrarLlamada(String telefOrigen, LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, LocalTime horaFin, String comunicante, String nifOperador)throws DBConnectionException {
       DAOLlamadaCritica.registrarLlamada(telefOrigen, comunicante, fechaInicio, horaInicio, fechaFin, horaFin, nifOperador,nombrePaciente,emergenciaDescripcion);
    }
}
