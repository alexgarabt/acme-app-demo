/**
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.general;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import persistencia.DBAccess.DBConnection;
import persistencia.exceptions.DBConnectionException;

/**
 *
 * @author diego
 */
public class DAODireccion {
    
    private static final String SELECT_DIRECCION_BY_ID = "SELECT * FROM DIRECCIONES WHERE id = ? ";

    public static String getDireccionPorId(short id) throws DBConnectionException {
        String entryJSON = "";
        try {
            DBConnection connection = DBConnection.getInstance();
            connection.openConnection();
            PreparedStatement ps = connection.getStatement(SELECT_DIRECCION_BY_ID);
            ResultSet rs;

            ps.setShort(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                String nombreVia = rs.getString("NombreDeLaVia");
                short numero = rs.getShort("Numero");
                String otros = rs.getString("Otros");
                String localidad = rs.getString("Localidad");
                String provincia = rs.getString("Provincia");
                int codPostal = rs.getInt("CodigoPostal");
                entryJSON = mapEntryAsJSON(nombreVia, numero, otros, codPostal, localidad, provincia);
            }
            rs.close();
            connection.closeConnection();
        } catch (SQLException e) {
            Logger.getLogger(DAODireccion.class.getName()).log(Level.SEVERE, "Error executing query.", e);
        }
        return entryJSON;

    }

    private static String mapEntryAsJSON(String nombreVia, short numero, String otros, int codPostal, String localidad, String provincia) {
        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("NombreDeLaVia", nombreVia)
                .add("Numero", String.valueOf(numero))
                .add("Otros", otros)
                .add("CodigoPostal", String.valueOf(codPostal))
                .add("Localidad", localidad)
                .add("Provincia", provincia)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAODireccion.class.getName()).log(Level.SEVERE, "Error creating JSON entry.", ex);
        }
        return entryJSON;
    }

}
