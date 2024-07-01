package negocio.modelos.general;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

/**
 * Clase representa la una direccion fisica mediante sus atributos
 */
public class Direccion {

    private String nombreVia;
    private short numero;
    private String otros;
    private int codPostal;
    private String localidad;
    private String provincia;

    /**
     * Construccion de la direccion mediante el uso de un objeto JSON en forma string
     * @param json objeto de la direccion
     */
    public Direccion(String json) {
        
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject direccionJSON = reader.readObject();
        this.nombreVia = direccionJSON.getString("NombreDeLaVia");
        this.numero = Short.parseShort(direccionJSON.getString("Numero"));
        this.otros = direccionJSON.getString("Otros");
        this.codPostal = Integer.parseInt(direccionJSON.getString("CodigoPostal"));
        this.localidad = direccionJSON.getString("Localidad");
        this.provincia = direccionJSON.getString("Provincia");
    }

    
    // GETTERS & SETTERS
    public String getNombreVia() {
        return nombreVia;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public short getNumero() {
        return numero;
    }

    public void setNumero(short numero) {
        this.numero = numero;
    }

    public String getOtros() {
        return otros;
    }

    public void setOtros(String otros) {
        this.otros = otros;
    }

    public int getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(int codPostal) {
        this.codPostal = codPostal;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
}
