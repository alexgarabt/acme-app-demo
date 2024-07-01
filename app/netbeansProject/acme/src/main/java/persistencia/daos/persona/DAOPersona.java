/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.persona;

import java.io.StringWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import persistencia.DBAccess.DBConnection;
import persistencia.daos.general.DAODireccion;
import persistencia.exceptions.DBConnectionException;

/**
 *
 * @author elena
 */
public class DAOPersona {

    private static final String SELECT_PERSONA_BY_NIF = "SELECT * FROM PERSONAS WHERE Nif=?";
    private static final String SELECT_NIF_BY_NOMBRE_AND_FECHA = "SELECT NIF FROM PERSONAS WHERE nombre = ? AND apellidos = ? and fechaDeNacimiento = ?";

    public static String consultaPersonaPorNif(String n) throws DBConnectionException{
        String jsonString = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (
                PreparedStatement ps = connection.getStatement(SELECT_PERSONA_BY_NIF);) {
            ps.setString(1, n);
            rs = ps.executeQuery();
            if (rs.next()) {
                String nif = rs.getString("NIF");
                String nombre = rs.getString("NOMBRE");
                String apellidos = rs.getString("APELLIDOS");
                Date fechaDeNacimiento = rs.getDate("FECHADENACIMIENTO");
                String telefono = rs.getString("TELEFONO");
                short idDireccion = rs.getShort("DIRECCIONPOSTAL");
                String direccion = DAODireccion.getDireccionPorId(idDireccion);

                jsonString = mapEntryAsJSON(nif, nombre, apellidos, fechaDeNacimiento, telefono, direccion);
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOPersona.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return jsonString;

    }

    public static String consultaNifPorNombreYFecha(String nombre, String apellidos, LocalDate fecha) throws DBConnectionException {
        String nif = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (
                PreparedStatement ps = connection.getStatement(SELECT_NIF_BY_NOMBRE_AND_FECHA);) {
            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setDate(3,Date.valueOf(fecha));
            rs = ps.executeQuery();
            if (rs.next()) {
                nif = rs.getString("NIF");
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOPersona.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return nif;

    }
    
    
    
    private static String mapEntryAsJSON(String nif, String nombre, String apellidos, Date fechaDeNacimiento, String telefono, String direccion) {

        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("Nif", nif)
                .add("Nombre", nombre)
                .add("Apellidos", apellidos)
                .add("FechaDeNacimiento", fechaDeNacimiento.toString())
                .add("Telefono", telefono)
                .add("Direccion", direccion)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAOPersona.class.getName()).log(Level.SEVERE, "Error creating EntryJSON.", ex);
        }
        return entryJSON;
    }

}
