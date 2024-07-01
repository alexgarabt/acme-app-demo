/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.llamada;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.DBAccess.DBConnection;
import persistencia.exceptions.DBConnectionException;

/**
 *
 * @author diego
 */
public class DAOLlamadaCritica {
    private static final String INSERT_CRITICA = "INSERT INTO LLAMADASCRITICAS VALUES (?)";

    public static void registrarLlamada(String telefOrigen, String comunicante, LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, LocalTime horaFin, String nifOperador,String paciente, String descripcionEmergencia) throws DBConnectionException{
        int id = DAOLlamada.consultaMaxID() + 1;

        DAOLlamadaDeAsegurado.registrarLlamada(id, telefOrigen, comunicante, fechaInicio, horaInicio, fechaFin, horaFin, nifOperador,paciente,descripcionEmergencia);

        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        try (PreparedStatement ps = connection.getStatement(INSERT_CRITICA);) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(DAOLlamadaCritica.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
    }
}
