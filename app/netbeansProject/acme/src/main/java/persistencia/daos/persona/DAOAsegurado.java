/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos.persona;

import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class DAOAsegurado {

    private static final String SELECT_ASEGURADO_BY_NIF = "SELECT * FROM ASEGURADOS WHERE Nif=?";

    public static String consultaAseguradoPorNif(String n) throws DBConnectionException{
        String jsonString = "";
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();

        try (
                PreparedStatement ps = connection.getStatement(SELECT_ASEGURADO_BY_NIF);) {
            ps.setString(1, n);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String sexo = rs.getString("Sexo");
                String numeroDeCuenta = rs.getString("NumeroDeCuenta");
                String persona = DAOPersona.consultaPersonaPorNif(n);
                String polizas = DAOPoliza.consultaPolizasPorNifAsegurado(n);

                jsonString = mapEntryAsJSON(sexo,numeroDeCuenta,persona,polizas);
            }
            rs.close();
        } catch (SQLException e) {
            Logger.getLogger(DAOAsegurado.class.getName()).log(Level.SEVERE, "Error executing query.", e);
            connection.closeConnection();
        }
        connection.closeConnection();
        return jsonString;

    }

    private static String mapEntryAsJSON(String sexo, String numeroDeCuenta, String persona, String polizas) {

        String entryJSON = "";
        JsonObject json = Json.createObjectBuilder()
                .add("Sexo", sexo)
                .add("NumeroDeCuenta", numeroDeCuenta)
                .add("Persona", persona)
                .add("Polizas", polizas)
                .build();
        try (
                StringWriter stringWriter = new StringWriter(); JsonWriter writer = Json.createWriter(stringWriter);) {
            writer.writeObject(json);
            entryJSON = stringWriter.toString();
        } catch (Exception ex) {
            Logger.getLogger(DAOAsegurado.class.getName()).log(Level.SEVERE, "Error creating EntryJSON.", ex);
        }
        return entryJSON;
    }

}
