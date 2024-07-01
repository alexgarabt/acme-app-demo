package negocio.modelos.llamada;

import negocio.modelos.persona.Empleado;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

/**
 * Clase encargada de representar la informacion completa de una llamada
 */
public class Llamada {
    
    String numeroTelefonoOrigen;
    LocalDate fechaInicio;
    LocalDate fechaFin;
    LocalTime horaInicio;
    LocalTime horaFin;
    String nombreComunicante;
    Empleado atendidaPor;
    
     /**
     * Construccion del objeto mediante el uso de un objeto JSON en forma string
     * @param json objeto con la informacion
     */
    public Llamada(String json){
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject llamadaNoCriticaJSON = reader.readObject();
        
        reader = factory.createReader(new StringReader(llamadaNoCriticaJSON.getString("LlamadaDeAsegurado")));
        JsonObject llamadaDeAseguradoJSON = reader.readObject();
        
        reader = factory.createReader(new StringReader(llamadaDeAseguradoJSON.getString("Llamada")));
        JsonObject llamadaJSON = reader.readObject();
        
        numeroTelefonoOrigen = llamadaJSON.getString("NumeroTelefonoOrigen");
        fechaInicio = LocalDate.parse(llamadaJSON.getString("FechaInicio"));
        fechaFin = LocalDate.parse(llamadaJSON.getString("FechaFin"));
        horaInicio = LocalTime.parse(llamadaJSON.getString("HoraInicio"));
        horaFin = LocalTime.parse(llamadaJSON.getString("HoraFin"));
        nombreComunicante = llamadaJSON.getString("NombreComunicante");
        atendidaPor = new Empleado(llamadaJSON.getString("AtendidaPor"));
    }

    //GETTERS & SETTERS
    public String getNumeroTelefonoOrigen() {
        return numeroTelefonoOrigen;
    }

    public void setNumeroTelefonoOrigen(String numeroTelefonoOrigen) {
        this.numeroTelefonoOrigen = numeroTelefonoOrigen;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public String getNombreComunicante() {
        return nombreComunicante;
    }

    public void setNombreComunicante(String nombreComunicante) {
        this.nombreComunicante = nombreComunicante;
    }

    public Empleado getAtendidaPor() {
        return atendidaPor;
    }

    public void setAtendidaPor(Empleado atendidaPor) {
        this.atendidaPor = atendidaPor;
    }
    
    
}
