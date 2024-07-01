package negocio.modelos.llamada;

import java.io.StringReader;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;

/**
 * Clase encargada de representar la informacion de una llamada no critica hecha por un asegurado
 */
public class LlamadaNoCritica extends LlamadaDeAsegurado {
    boolean esLeve;
    ArrayList<Consejo> consejos;
    
     /**
     * Construccion del objeto mediante el uso de un objeto JSON en forma string
     * @param json objeto con la informacion
     */
    public LlamadaNoCritica(String json){
        super(json);
        consejos = new ArrayList<>();
        
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject llamadaNoCriticaJSON = reader.readObject();
        
        reader = factory.createReader(new StringReader(llamadaNoCriticaJSON.getString("Consejos")));
        JsonArray consejosJSON = reader.readArray();
        
        esLeve = Boolean.parseBoolean(llamadaNoCriticaJSON.getString("EsLeve"));
        
        for (JsonValue v : consejosJSON){
            Consejo c = new Consejo(v.toString());
            consejos.add(c);
        }
        
    }
    
    //GETTERS & SETTERS
    public boolean isEsLeve() {
        return esLeve;
    }

    public void setEsLeve(boolean esLeve) {
        this.esLeve = esLeve;
    }

    public ArrayList<Consejo> getConsejos() {
        return consejos;
    }

    public void setConsejos(ArrayList<Consejo> consejos) {
        this.consejos = consejos;
    }
    
}
