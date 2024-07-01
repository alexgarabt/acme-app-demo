/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.llamada;

import persistencia.daos.persona.DAOAsegurado;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class DAOLlamadaDeAsegurado {

    private static final String SELECT_LLAMADA_BY_ID = "SELECT * FROM LLAMADASDEASEGURADOS WHERE id=?";
    private static final String INSERT_LLAMADAASEGURADO = "INSERT INTO LLAMADASDEASEGURADOS VALUES (?,?,?)";

    public static String consultaLlamadaPorId(int id) throws DBConnectionException{
        String jsonString = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();

        try (
                PreparedStatement ps = connection.getStatement(SELECT_LLAMADA_BY_ID);) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String descripcionDeLaEmergencia = rs.getString("DescripcionDeLaEmergencia");
                
                String paciente = rs.getString("Paciente");
                String asegurado = DAOAsegurado.consultaAseguradoPorNif(paciente);
                
                String llamada = DAOLlamada.consultaLlamadaPorId(id);
                

                jsonString = mapEntryAsJSON(descripcionDeLaEmergencia,asegurado,llamada);
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOLlamadaDeAsegurado.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return jsonString;

    }

    public static void registrarLlamada(int id, String telefOrigen, String comunicante, LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, LocalTime horaFin, String nifOperador,String paciente, String descripcionEmergencia) throws DBConnectionException{

        DAOLlamada.registrarLlamada(id, telefOrigen, comunicante, fechaInicio, horaInicio, fechaFin, horaFin, nifOperador);

        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        try (PreparedStatement ps = connection.getStatement(INSERT_LLAMADAASEGURADO);) {
            ps.setInt(1, id);
            ps.setString(2,descripcionEmergencia);
            ps.setString(3, paciente);
            ps.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(DAOLlamadaDeAsegurado.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
    }

    private static String mapEntryAsJSON(String descripcionDeLaEmergencia,String asegurado, String llamada) {

        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("DescripcionDeLaEmergencia", descripcionDeLaEmergencia)
                .add("Asegurado",asegurado)
                .add("Llamada",llamada)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAOLlamadaDeAsegurado.class.getName()).log(Level.SEVERE, "Error creating EntryJSON.", ex);
        }
        return entryJSON;
    }

}
