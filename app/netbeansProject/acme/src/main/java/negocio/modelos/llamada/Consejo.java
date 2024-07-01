package negocio.modelos.llamada;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

/**
 * Clase encargada de representar un consejo dado en una llamada
 */
public class Consejo {
    String descripcion;
    boolean soluciona;
    String resultado;
    
     /**
     * Construccion del objeto mediante el uso de un objeto JSON en forma string
     * @param json objeto con la informacion
     */
    public Consejo(String json) {
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject consejoJSON = reader.readObject();
        
        descripcion = consejoJSON.getString("Descripcion");
        soluciona = Boolean.parseBoolean(consejoJSON.getString("Soluciona"));
        resultado = consejoJSON.getString("Resultado");
    }
    
    public Consejo(String descripcion, boolean soluciona, String resultado){
        this.descripcion = descripcion;
        this.resultado = resultado;
        this.soluciona = soluciona;
    }

    //GETTERS & SETTERS
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean soluciona() {
        return soluciona;
    }

    public void setSoluciona(boolean soluciona) {
        this.soluciona = soluciona;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    
    
}
