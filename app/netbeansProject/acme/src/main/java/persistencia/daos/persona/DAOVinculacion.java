/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.persona;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import persistencia.DBAccess.DBConnection;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import persistencia.exceptions.DBConnectionException;

/**
 *
 * @author diego
 */
public class DAOVinculacion {

    private static final String SELECT_VINCULACION_BY_NIF = "SELECT * FROM VINCULACIONESCONLAEMPRESA V INNER JOIN TIPOSDEVINCULACION TV ON V.VINCULO = TV.IDTIPO WHERE Empleado = ? AND FECHAFIN IS NULL";
   
    public static String getVinculacionActual(String nif) throws DBConnectionException{
        String entryJSON = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        PreparedStatement ps = connection.getStatement(SELECT_VINCULACION_BY_NIF);
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
            Logger.getLogger(DAOVinculacion.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();

        return entryJSON;

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
            Logger.getLogger(DAOVinculacion.class.getName()).log(Level.SEVERE, "Error creating EntryJSON.", ex);
        }
        return entryJSON;
    }
}
