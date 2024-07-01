package negocio.controladoresCasoUso.registrarllamadastrategy;

import java.time.LocalDate;
import java.time.LocalTime;
import persistencia.daos.llamada.DAOLlamadaInfundada;
import persistencia.exceptions.DBConnectionException;

public class RegistrarLlamadaInfundadaStrategy implements RegistrarLlamadaStrategy {
    @Override
    public void registrarLlamada(String telefonoOrigen, LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, LocalTime horaFin, String nombreComunicante, String nifOperador) throws DBConnectionException {
       DAOLlamadaInfundada.registrarLlamada(telefonoOrigen, nombreComunicante, fechaInicio, horaInicio, fechaFin, horaFin, nifOperador);
    }
}
