/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.persona;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import java.io.StringWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import persistencia.DBAccess.DBConnection;
import persistencia.exceptions.DBConnectionException;

/**
 *
 * @author diego
 */
public class DAODisponibilidad {
    private static final String SELECT_DISPONIBILIDAD_BY_NIF = "SELECT * FROM DISPONIBILIDADES D INNER JOIN TIPOSDEDISPONIBILIDAD TD ON D.DISPONIBILIDAD = TD.IDTIPO WHERE Empleado = ? AND FechaFin IS NULL";
    private static final String SELECT_DISPONIBILIDAD_BY_NIF_AND_FECHA = "SELECT * FROM DISPONIBILIDADES D INNER JOIN TIPOSDEDISPONIBILIDAD TD ON D.DISPONIBILIDAD = TD.IDTIPO WHERE Empleado = ? AND FECHAINICIO <= ? AND (FECHAFIN IS NULL OR FECHAFIN>=?)";
            
    public static String getDisponibilidadActual(String nif) throws DBConnectionException{
        String entryJSON = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        PreparedStatement ps = connection.getStatement(SELECT_DISPONIBILIDAD_BY_NIF);
        ResultSet rs;
        try {
            ps.setString(1, nif);
            rs = ps.executeQuery();
            if (rs.next()) {
                String nombreTipo = rs.getString("NombreTipo");
                entryJSON = mapEntryAsJSON(nombreTipo);

            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAODisponibilidad.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();

        return entryJSON;

    }
  
    public static String consultaDisponibilidadPorNifYFecha(String nif, LocalDate fecha) throws DBConnectionException {
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        PreparedStatement ps = connection.getStatement(SELECT_DISPONIBILIDAD_BY_NIF_AND_FECHA);
        ResultSet rs;
        String disponibilidad = "";
        try {
            ps.setString(1, nif);
            ps.setDate(2, Date.valueOf(fecha));
            ps.setDate(3, Date.valueOf(fecha));
            rs = ps.executeQuery();
            if (rs.next()) {
                disponibilidad = rs.getString("NombreTipo");

            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAODisponibilidad.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();

        return disponibilidad;

    }
    
    private static String mapEntryAsJSON(String nombreTipo) {
        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("NombreTipo", nombreTipo)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAODisponibilidad.class.getName()).log(Level.SEVERE, "Error creating EntryJSON.", ex);
        }
        return entryJSON;
    }
}
