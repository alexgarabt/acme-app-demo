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
public class DAOLlamadaInfundada {

    private static final String INSERT_INFUNDADA = "INSERT INTO LLAMADASINFUNDADAS VALUES (?)";

    public static void registrarLlamada(String telefOrigen, String comunicante, LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, LocalTime horaFin, String nifOperador) throws DBConnectionException {
        int id = DAOLlamada.consultaMaxID() + 1;

        DAOLlamada.registrarLlamada(id, telefOrigen, comunicante, fechaInicio, horaInicio, fechaFin, horaFin, nifOperador);

        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        try (PreparedStatement ps = connection.getStatement(INSERT_INFUNDADA);) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(DAOLlamadaInfundada.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
    }
}
