/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.llamada;

import persistencia.daos.persona.DAOEmpleado;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import persistencia.DBAccess.DBConnection;
import persistencia.exceptions.DBConnectionException;

/**
 *
 * @author elena
 */
public class DAOLlamada {

    private static final String SELECT_LLAMADA_BY_ID = "SELECT * FROM LLAMADAS WHERE id=?";
    private static final String SELECT_MAX_ID = "SELECT MAX(id) as maxID FROM LLAMADAS";
    private static final String INSERT_LLAMADA = "INSERT INTO LLAMADAS VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public static String consultaLlamadaPorId(int id) throws DBConnectionException {
        String jsonString = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();

        try (
                PreparedStatement ps = connection.getStatement(SELECT_LLAMADA_BY_ID);) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String numeroTelefonoOrigen = rs.getString("NumeroTelefonoOrigen");
                String fechaInicio = rs.getString("FechaInicio");
                String horaInicio = rs.getString("HoraInicio");
                String fechaFin = rs.getString("FechaFin");
                String horaFin = rs.getString("HoraFin");
                String nombreComunicante = rs.getString("NombreComunicante");
                
                String atendidaPorID = rs.getString("AtendidaPor");
                String atendidaPorJSON = DAOEmpleado.consultaEmpleadoPorNif(atendidaPorID);
                

                jsonString = mapEntryAsJSON(numeroTelefonoOrigen,fechaInicio,horaInicio,fechaFin,horaFin,nombreComunicante,atendidaPorJSON);
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOLlamada.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return jsonString;

    }
    
    public static int consultaMaxID() throws DBConnectionException{
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        int maxID = -1;
        try (
                PreparedStatement ps = connection.getStatement(SELECT_MAX_ID);) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                maxID = rs.getInt("maxID");
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOLlamada.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return maxID;

    }
    
    public static void registrarLlamada(int id, String telefOrigen, String comunicante, LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, LocalTime horaFin, String nifOperador) throws DBConnectionException {
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        try (
                PreparedStatement ps = connection.getStatement(INSERT_LLAMADA);) {
            ps.setInt(1,id);
            ps.setString(2, telefOrigen);
            ps.setDate(3, Date.valueOf(fechaInicio));
            ps.setTime(4, Time.valueOf(horaInicio));
            ps.setDate(5,Date.valueOf(fechaFin));
            ps.setTime(6,Time.valueOf(horaFin));
            ps.setString(7,comunicante);
            ps.setString(8, nifOperador);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            Logger.getLogger(DAOLlamada.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();

    }

    

    private static String mapEntryAsJSON(String numeroTelefonoOrigen,String fechaInicio,String horaInicio,String fechaFin,String horaFin,String nombreComunicante,String atendidaPorJSON) {

        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("NumeroTelefonoOrigen", numeroTelefonoOrigen)
                .add("FechaInicio",fechaInicio)
                .add("HoraInicio",horaInicio)
                .add("FechaFin",fechaFin)
                .add("HoraFin",horaFin)
                .add("NombreComunicante",nombreComunicante)
                .add("AtendidaPor",atendidaPorJSON)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAOLlamada.class.getName()).log(Level.SEVERE, "Error creating EntryJSON.", ex);
        }
        return entryJSON;
    }

}
