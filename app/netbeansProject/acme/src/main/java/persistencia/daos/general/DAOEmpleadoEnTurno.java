/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.general;

import persistencia.daos.persona.DAOEmpleado;
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
import javax.json.JsonWriter;
import persistencia.DBAccess.DBConnection;
import persistencia.exceptions.DBConnectionException;

/**
 *
 * @author diego
 */
public class DAOEmpleadoEnTurno {

    private static final String SELECT_MEDICOS_EN_TURNO_BY_IDTURNO = "SELECT Medico FROM MedicosEnTurno WHERE Turno = ?";
    private static final String SELECT_CONDUCTORES_EN_TURNO_BY_IDTURNO = "SELECT Conductor FROM ConductoresEnTurno WHERE Turno = ?";
    private static final String SELECT_OPERADORES_EN_TURNO_BY_IDTURNO = "SELECT * FROM OPERADORESENTURNO WHERE Turno = ?";
    private static final String UPDATE_OPERADOR_EN_TURNO_BY_IDTURNO_AND_NIFS = "UPDATE OPERADORESENTURNO SET OPERADOR = ? WHERE turno = ? AND Operador = ?";

    public static void cambiarOperadorEnTurno(int idTurno, String nifAntiguo, String nifActual) throws DBConnectionException {
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        try (PreparedStatement ps = connection.getStatement(UPDATE_OPERADOR_EN_TURNO_BY_IDTURNO_AND_NIFS);) {
            ps.setString(1, nifActual);
            ps.setInt(2, idTurno);
            ps.setString(3, nifAntiguo);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(DAOEmpleadoEnTurno.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
    }

    public static String consultaMedicosEnTurno(int idTurno) throws DBConnectionException {
        String entryJSON = "";
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (PreparedStatement ps = connection.getStatement(SELECT_MEDICOS_EN_TURNO_BY_IDTURNO);) {
            ps.setInt(1, idTurno);
            rs = ps.executeQuery();
            while (rs.next()) {
                String medico = DAOEmpleado.consultaEmpleadoPorNif(rs.getString("Medico"));

                arrayBuilder.add(Json.createReader(new StringReader(medico)).readObject());

            }
            JsonArray jsonArray = arrayBuilder.build();
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = Json.createWriter(stringWriter);
            writer.write(jsonArray);
            entryJSON = stringWriter.toString();

            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOEmpleadoEnTurno.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return entryJSON;
    }

    public static String consultaConductoresEnTurno(int idTurno) throws DBConnectionException {
        String entryJSON = "";
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (PreparedStatement ps = connection.getStatement(SELECT_CONDUCTORES_EN_TURNO_BY_IDTURNO);) {
            ps.setInt(1, idTurno);
            rs = ps.executeQuery();
            while (rs.next()) {
                String conductor = DAOEmpleado.consultaEmpleadoPorNif(rs.getString("Conductor"));

                arrayBuilder.add(Json.createReader(new StringReader(conductor)).readObject());

            }
            JsonArray jsonArray = arrayBuilder.build();
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = Json.createWriter(stringWriter);
            writer.write(jsonArray);
            entryJSON = stringWriter.toString();

            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOEmpleadoEnTurno.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return entryJSON;
    }

    public static String conseguirOperadoresEnTurnoPorIdTurno(int id) throws DBConnectionException{
        String entryJSON = "";
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (PreparedStatement ps = connection.getStatement(SELECT_OPERADORES_EN_TURNO_BY_IDTURNO);) {
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                String operador = DAOEmpleado.consultaEmpleadoPorNif(rs.getString("Operador"));

                arrayBuilder.add(Json.createReader(new StringReader(operador)).readObject());

            }
            JsonArray jsonArray = arrayBuilder.build();
            StringWriter stringWriter = new StringWriter();
            JsonWriter writer = Json.createWriter(stringWriter);
            writer.write(jsonArray);
            entryJSON = stringWriter.toString();

            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOEmpleadoEnTurno.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return entryJSON;
    }

}
