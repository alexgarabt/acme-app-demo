package negocio.modelos.persona;

import java.io.StringReader;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;

/**
 *
 * Clase encargada de representar la informacion de una persona
 */
public class Asegurado extends Persona {

    Sexo sexo;
    String numeroDeCuenta;
    ArrayList<Poliza> polizas;
    
     /**
     * Construccion del objeto mediante el uso de un objeto JSON en forma string
     * @param json objeto con la informacion
     */
    public Asegurado(String json) {
        super(json);
        polizas = new ArrayList<>();
        
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject aseguradoJSON = reader.readObject();
        
        reader = factory.createReader(new StringReader(aseguradoJSON.getString("Polizas")));
        JsonArray polizasJSON = reader.readArray();
        
        sexo = Sexo.valueOf(aseguradoJSON.getString("Sexo"));
        numeroDeCuenta = aseguradoJSON.getString("NumeroDeCuenta");
        
        for (JsonValue v : polizasJSON){
            Poliza p = new Poliza(v.toString());
            polizas.add(p);
        }
    }

    //GETTERS & SETTERS
    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public String getNumeroDeCuenta() {
        return numeroDeCuenta;
    }

    public void setNumeroDeCuenta(String numeroDeCuenta) {
        this.numeroDeCuenta = numeroDeCuenta;
    }
    
    
    
    
}
