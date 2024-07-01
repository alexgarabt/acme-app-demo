/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.llamada;

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
public class DAOConsejo {

    private static final String SELECT_CONSEJOS_POR_LLAMADA = "SELECT * FROM CONSEJOS WHERE Llamada = ?";
    private static final String INSERT_CONSEJO = "INSERT INTO CONSEJOS VALUES(?,?,?,?)";

    public static String consultaConsejosPorLlamada(int idLlamada) throws DBConnectionException{
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        String entryJSON = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (PreparedStatement ps = connection.getStatement(SELECT_CONSEJOS_POR_LLAMADA);) {
            ps.setInt(1, idLlamada);
            rs = ps.executeQuery();
            while (rs.next()) {
                String descripcion = rs.getString("Descripcion");
                String resultado = rs.getString("Resultado");
                String soluciona = rs.getString("Soluciona");

                String consejoJSON = mapEntryAsJSON(descripcion, resultado, soluciona);

                arrayBuilder.add(Json.createReader(new StringReader(consejoJSON)).readObject());
            }
            JsonArray jsonArray = arrayBuilder.build();
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = Json.createWriter(stringWriter);
            writer.write(jsonArray);
            entryJSON = stringWriter.toString();

            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOConsejo.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return entryJSON;
    }
    

    public static void registrarConsejo(int id, String descripcion, String resultado, boolean soluciona) throws DBConnectionException{
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        try (PreparedStatement ps = connection.getStatement(INSERT_CONSEJO);) {
            ps.setString(1,descripcion);
            ps.setString(2, resultado);
            ps.setBoolean(3,soluciona);
            ps.setInt(4, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(DAOConsejo.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
    }

    private static String mapEntryAsJSON(String descripcion, String resultado, String soluciona) {

        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("Descripcion", descripcion)
                .add("Resultado",resultado)
                .add("Soluciona",soluciona)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAOConsejo.class.getName()).log(Level.SEVERE, "Error creating EntryJSON.", ex);
        }
        return entryJSON;

    }
}
