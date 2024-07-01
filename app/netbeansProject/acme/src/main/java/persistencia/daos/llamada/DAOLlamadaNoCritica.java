/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.llamada;

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
public class DAOLlamadaNoCritica {

    private static final String SELECT_LLAMADA_BY_ID_ACTIVACION = "SELECT * FROM LLAMADASNOCRITICAS WHERE RequiereOperativo=?";
    private static final String INSERT_NOCRITICA = "INSERT INTO LLAMADASNOCRITICAS VALUES (?,?,NULL)";


    public static String consultaLlamadaPorIdActivacion(int idAct) throws DBConnectionException{
        String jsonString = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();

        try (
                PreparedStatement ps = connection.getStatement(SELECT_LLAMADA_BY_ID_ACTIVACION);) {
            ps.setInt(1, idAct);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String esLeve = rs.getString("EsLeve");
                int id = rs.getInt("Id");
                String llamadaDeAsegurado = DAOLlamadaDeAsegurado.consultaLlamadaPorId(id);
                String consejos = DAOConsejo.consultaConsejosPorLlamada(id);

                jsonString = mapEntryAsJSON(esLeve,llamadaDeAsegurado,consejos);
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOLlamadaNoCritica.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return jsonString;

    }
    
    public static int registrarLlamadaYDevolverID(String telefOrigen, String comunicante, LocalDate fechaInicio, LocalTime horaInicio, LocalDate fechaFin, LocalTime horaFin, String nifOperador,String paciente, String descripcionEmergencia, boolean esLeve) throws DBConnectionException{
        int id = DAOLlamada.consultaMaxID() + 1;

        DAOLlamadaDeAsegurado.registrarLlamada(id, telefOrigen, comunicante, fechaInicio, horaInicio, fechaFin, horaFin, nifOperador,paciente,descripcionEmergencia);

        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        try (PreparedStatement ps = connection.getStatement(INSERT_NOCRITICA);) {
            ps.setInt(1, id);
            ps.setBoolean(2, esLeve);
            ps.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(DAOLlamadaNoCritica.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return id;
    }

    private static String mapEntryAsJSON(String esLeve, String llamadaDeAsegurado, String consejos) {

        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("EsLeve", esLeve)
                .add("LlamadaDeAsegurado",llamadaDeAsegurado)
                .add("Consejos",consejos)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAOLlamadaNoCritica.class.getName()).log(Level.SEVERE, "Error creating EntryJSON.", ex);
        }
        return entryJSON;
    }

}
