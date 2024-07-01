package negocio.modelos.persona;

import java.io.StringReader;
import java.time.LocalDate;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

/**
 * Clase encargada de almacenar la informacion de la poliza 
 */
public class Poliza {
    String numeroPoliza;
    LocalDate fechaInicio;
    LocalDate fechaVencimiento;

    /**
     * Construccion del objeto mediante el uso de un objeto JSON en forma string
     * @param json objeto con la informacion
     */
    public Poliza(String json) {
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject polizaJSON = reader.readObject();
        
        numeroPoliza = polizaJSON.getString("NumeroPoliza");
        fechaInicio = LocalDate.parse(polizaJSON.getString("FechaInicio"));
        fechaVencimiento = LocalDate.parse(polizaJSON.getString("FechaVencimiento"));
    }

    //GETTERS & SETTERS
    public String getNumeroPoliza() {
        return numeroPoliza;
    }

    public void setNumeroPoliza(String numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    
}
