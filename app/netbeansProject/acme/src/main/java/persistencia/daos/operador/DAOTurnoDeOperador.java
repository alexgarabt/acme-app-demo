/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.operador;

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
import persistencia.daos.general.DAOEmpleadoEnTurno;
import persistencia.exceptions.DBConnectionException;

/**
 *
 * @author diego
 */
public class DAOTurnoDeOperador {
    private static final String SELECT_TURNO_DE_OPERADOR_BY_FECHA_AND_TIPO = "SELECT * FROM TurnosDeOperador INNER JOIN TIPOSDETURNODEOPERADOR ON idTipo = TipoDeTurno WHERE FechaTurno = ? AND nombreTipo = ?";
    private static final String SELECT_TURNO_ANTERIOR_BY_FECHA_AND_NIF = "SELECT FechaTurno, nombreTipo FROM TurnosDeOperador INNER JOIN TIPOSDETURNODEOPERADOR ON idTipo = TipoDeTurno NATURAL JOIN OPERADORESENTURNO WHERE FechaTurno < ? and operador = ? ORDER BY FechaTurno DESC FETCH FIRST 1 ROW ONLY";
    private static final String SELECT_IDTURNO_BY_FECHA_AND_TIPO = "SELECT id FROM TurnosDeOperador INNER JOIN TIPOSDETURNODEOPERADOR ON idTipo = TipoDeTurno WHERE FechaTurno = ? AND nombreTipo = ?";
    
    public static void cambiarOperadorEnTurno(LocalDate date,String tipoTurno, String nifAntiguo, String nifActual) throws DBConnectionException {
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (PreparedStatement ps = connection.getStatement(SELECT_IDTURNO_BY_FECHA_AND_TIPO);) {
            ps.setDate(1, Date.valueOf(date));
            ps.setString(2,tipoTurno);
            rs = ps.executeQuery();
            if(rs.next()){
                int idTurno = rs.getInt("Id");
                DAOEmpleadoEnTurno.cambiarOperadorEnTurno(idTurno,nifAntiguo,nifActual);
            }
        } catch (SQLException e) {
            Logger.getLogger(DAOTurnoDeOperador.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
    } 
    
    public static String consultaTurnoDeOperadorPorFechaYTipo(LocalDate date, String tipo) throws DBConnectionException{
        String entryJSON = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (PreparedStatement ps = connection.getStatement(SELECT_TURNO_DE_OPERADOR_BY_FECHA_AND_TIPO);) {
            ps.setDate(1, Date.valueOf(date));
            ps.setString(2, tipo);
            rs = ps.executeQuery();
            if (rs.next()) {
                String fechaCreacion = rs.getString("FechaCreacion");
                int id = rs.getInt("Id");
                String nombreTipo = rs.getString("NombreTipo");
                String operadoresEnTurno = DAOEmpleadoEnTurno.conseguirOperadoresEnTurnoPorIdTurno(id);
                
                String fechaTurno = date.toString();
                entryJSON = mapEntryAsJSON(id,nombreTipo,fechaTurno, fechaCreacion,operadoresEnTurno);
                
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOTurnoDeOperador.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();

        return entryJSON;

    } 
    
   public static String consultaTipoDelTurnoAnteriorPorFechaYNif(LocalDate date, String nif) throws DBConnectionException {
        String entryJSON = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();
        ResultSet rs;
        try (PreparedStatement ps = connection.getStatement(SELECT_TURNO_ANTERIOR_BY_FECHA_AND_NIF);) {
            ps.setDate(1, Date.valueOf(date));
            ps.setString(2, nif);
            rs = ps.executeQuery();
            if (rs.next()) {
                String fechaTurnoAnterior = rs.getString("FechaTurno");
                String tipoTurnoAnterior = rs.getString("nombreTipo");
                entryJSON = mapEntryAsJSON(fechaTurnoAnterior,tipoTurnoAnterior);
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOTurnoDeOperador.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();

        return entryJSON;

    } 
    
    
    private static String mapEntryAsJSON(int id, String nombreTipo,String fechaTurno, String fechaCreacion, String operadoresEnTurno) {
        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("Id", String.valueOf(id))
                .add("NombreTipo",nombreTipo)
                .add("FechaTurno",fechaTurno)
                .add("FechaCreacion",fechaCreacion)
                .add("OperadoresEnTurno", operadoresEnTurno)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAOTurnoDeOperador.class.getName()).log(Level.SEVERE, "Error creating EntryJSON.", ex);
        }
        return entryJSON;
    }
    
    private static String mapEntryAsJSON(String fechaTurnoAnterior, String tipoTurnoAnterior){
        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("FechaTurnoAnterior",fechaTurnoAnterior)
                .add("TipoTurnoAnterior", tipoTurnoAnterior)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAOTurnoDeOperador.class.getName()).log(Level.SEVERE, "Error creating EntryJSON.", ex);
        }
        return entryJSON;
    }
}
