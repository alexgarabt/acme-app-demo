/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.persona;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import persistencia.DBAccess.DBConnection;
import persistencia.exceptions.DBConnectionException;

/**
 *
 * @author diego
 */
public class DAOPoliza {
    private static final String SELECT_POLIZAS_BY_NIF = "SELECT P.* FROM POLIZAS P INNER JOIN POLIZASCONTRATADAS PC ON PC.POLIZA = P.NUMEROPOLIZA WHERE PC.ASEGURADO = ?";
    private static final String SELECT_POLIZA_BY_NIF_Y_NUMERO = "SELECT P.* FROM POLIZAS P NATURAL JOIN POLIZASCONTRATADAS PC WHERE PC.Asegurado = ? AND PC.Poliza = ? AND FechaInicio <= current_date AND FechaVencimiento >= current_date";
    
    public static String consultaPolizasPorNifAsegurado(String nif) throws DBConnectionException {
        String entryJSON = "";
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (PreparedStatement ps = connection.getStatement(SELECT_POLIZAS_BY_NIF);) {
            ps.setString(1, nif);
            rs = ps.executeQuery();
            while (rs.next()) {
                String numeroPoliza = rs.getString("NumeroPoliza");
                String fechaInicio = rs.getString("FechaInicio");
                String fechaVencimiento = rs.getString("FechaVencimiento");
                
                String poliza = mapEntryAsJSON(numeroPoliza,fechaInicio,fechaVencimiento);
                
                arrayBuilder.add(Json.createReader(new StringReader(poliza)).readObject());
                
            }
            JsonArray jsonArray = arrayBuilder.build();
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = Json.createWriter(stringWriter);
            writer.write(jsonArray);
            entryJSON = stringWriter.toString();

            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOPoliza.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return entryJSON;
    }
    
    public static String consultaPolizaPorAseguradoYNumero(String nif, String poliza) throws DBConnectionException{
        String entryJSON = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (PreparedStatement ps = connection.getStatement(SELECT_POLIZA_BY_NIF_Y_NUMERO);) {
            ps.setString(1, nif);
            ps.setString(2,poliza);
            rs = ps.executeQuery();
            if (rs.next()) {
                String numeroPoliza = rs.getString("NumeroPoliza");
                String fechaInicio = rs.getString("FechaInicio");
                String fechaVencimiento = rs.getString("FechaVencimiento");
                
                entryJSON = mapEntryAsJSON(numeroPoliza,fechaInicio,fechaVencimiento);
                
                
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOPoliza.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return entryJSON;
    }
    
    private static String mapEntryAsJSON(String numeroPoliza, String fechaInicio, String fechaVencimiento) {

        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("NumeroPoliza", numeroPoliza)
                .add("FechaInicio", fechaInicio)
                .add("FechaVencimiento", fechaVencimiento)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAOPoliza.class.getName()).log(Level.SEVERE, "Error creatin EntryJSON.", ex);
        }
        return entryJSON;
    }
}
