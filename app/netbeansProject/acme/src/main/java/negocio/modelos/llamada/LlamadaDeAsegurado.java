package negocio.modelos.llamada;

import negocio.modelos.persona.Asegurado;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

/**
 * Clase encargada de representar la informacion de la llamada de un asegurado
 */
public class LlamadaDeAsegurado extends Llamada {

    String descripcionDeLaEmergencia;
    Asegurado asegurado;
    
     /**
     * Construccion del objeto mediante el uso de un objeto JSON en forma string
     * @param json objeto con la informacion
     */
    public LlamadaDeAsegurado(String json) {
        super(json);
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject llamadaNoCriticaJSON = reader.readObject();
        
        reader = factory.createReader(new StringReader(llamadaNoCriticaJSON.getString("LlamadaDeAsegurado")));
        JsonObject llamadaDeAseguradoJSON = reader.readObject();
        
        descripcionDeLaEmergencia = llamadaDeAseguradoJSON.getString("DescripcionDeLaEmergencia");
        asegurado = new Asegurado(llamadaDeAseguradoJSON.getString("Asegurado"));
    }

    //GETTERS & SETTERS
    public String getDescripcionDeLaEmergencia() {
        return descripcionDeLaEmergencia;
    }

    public void setDescripcionDeLaEmergencia(String descripcionDeLaEmergencia) {
        this.descripcionDeLaEmergencia = descripcionDeLaEmergencia;
    }

    public Asegurado getAsegurado() {
        return asegurado;
    }

    public void setAsegurado(Asegurado asegurado) {
        this.asegurado = asegurado;
    }
    
    
}
