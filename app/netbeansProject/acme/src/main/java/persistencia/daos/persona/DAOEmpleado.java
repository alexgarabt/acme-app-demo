/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.persona;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Date;
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
 * @author elena
 */
public class DAOEmpleado {

    private static final String SELECT_USUARIO_BY_NIF_AND_PASS = "SELECT * FROM EMPLEADOS WHERE Nif=? AND Password=?";
    private static final String SELECT_USUARIO_BY_NIF = "SELECT * FROM EMPLEADOS WHERE Nif=?";
    private static final String SELECT_OPERADORES = "SELECT DISTINCT E.* FROM EMPLEADOS E INNER JOIN ROLESENEMPRESA RE ON E.Nif = RE.Empleado WHERE RE.FechaInicioEnPuesto = (SELECT MAX(FechaInicioEnPuesto) FROM ROLESENEMPRESA RE2 WHERE RE2.Empleado = E.Nif) AND RE.Rol = (SELECT IdTipo FROM TIPOSDEROL WHERE NombreTipo = 'Operador')";

    public static String consultaEmpleadoPorLoginYPassword(String d, String p) throws DBConnectionException{
        String jsonString = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();

        try (
                PreparedStatement ps = connection.getStatement(SELECT_USUARIO_BY_NIF_AND_PASS);) {
            ps.setString(1, d);
            ps.setString(2, p);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String persona = DAOPersona.consultaPersonaPorNif(d);
                Date fechaInicioEnEmpresa = rs.getDate("FECHAINICIOENEMPRESA");
                String rolActual = DAORol.getRolActual(d);

                String vinculacionActual = DAOVinculacion.getVinculacionActual(d);
                String disponibilidadActual = DAODisponibilidad.getDisponibilidadActual(d);

                jsonString = mapEntryAsJSON(persona, fechaInicioEnEmpresa, rolActual, vinculacionActual, disponibilidadActual);
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOEmpleado.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return jsonString;

    }

    public static String consultaEmpleadoPorNif(String d) throws DBConnectionException {
        String jsonString = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();

        try (
                PreparedStatement ps = connection.getStatement(SELECT_USUARIO_BY_NIF);) {
            ps.setString(1, d);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String persona = DAOPersona.consultaPersonaPorNif(d);
                Date fechaInicioEnEmpresa = rs.getDate("FECHAINICIOENEMPRESA");
                String rolActual = DAORol.getRolActual(d);

                String vinculacionActual = DAOVinculacion.getVinculacionActual(d);
                String disponibilidadActual = DAODisponibilidad.getDisponibilidadActual(d);

                jsonString = mapEntryAsJSON(persona, fechaInicioEnEmpresa, rolActual, vinculacionActual, disponibilidadActual);
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOEmpleado.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return jsonString;

    }
    
    public static String consultaOperadores() throws DBConnectionException{
        String entryJSON = "";
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (PreparedStatement ps = connection.getStatement(SELECT_OPERADORES);) {
            rs = ps.executeQuery();
            while (rs.next()) {
                String nif = rs.getString("NIF");
                String persona = DAOPersona.consultaPersonaPorNif(nif);
                Date fechaInicioEnEmpresa = rs.getDate("FECHAINICIOENEMPRESA");
                String rolActual = DAORol.getRolActual(nif);

                String vinculacionActual = DAOVinculacion.getVinculacionActual(nif);
                String disponibilidadActual = DAODisponibilidad.getDisponibilidadActual(nif);
                
                String operador = mapEntryAsJSON(persona, fechaInicioEnEmpresa, rolActual, vinculacionActual, disponibilidadActual);
                arrayBuilder.add(Json.createReader(new StringReader(operador)).readObject());
                
            }
            JsonArray jsonArray = arrayBuilder.build();
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = Json.createWriter(stringWriter);
            writer.write(jsonArray);
            entryJSON = stringWriter.toString();

            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOEmpleado.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return entryJSON;  
    }
    

    private static String mapEntryAsJSON(String persona, Date fechaInicioEnEmpresa, String rolActual, String vinculacionActual, String disponibilidadActual) {

        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("Persona", persona)
                .add("FechaInicioEnEmpresa", fechaInicioEnEmpresa.toString())
                .add("RolActual", rolActual)
                .add("VinculacionActual", vinculacionActual)
                .add("DisponibilidadActual", disponibilidadActual)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAOEmpleado.class.getName()).log(Level.SEVERE, "Error creating EntryJSON.", ex);
        }
        return entryJSON;
    }


}
