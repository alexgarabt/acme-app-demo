package negocio.controladoresCasoUso.registrarllamadastrategy;

import java.time.LocalDate;
import java.time.LocalTime;
import persistencia.exceptions.DBConnectionException;

public interface RegistrarLlamadaStrategy {
    public void registrarLlamada(String telefonoOrigen, LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, LocalTime horaFin, String nombreComunicante, String nifOperador) throws DBConnectionException;
}
