package negocio.modelos.turnoOperador;

import negocio.modelos.persona.Empleado;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;

/**
 * Clase encargada de representar la informacion del turno de un operador en el sistema
 */
public class TurnoDeOperador {
    LocalDate fechaTurno;
    LocalDate fechaCreacion;
    TipoDeTurnoOperador tipoTurno;
    ArrayList<Empleado> operadores;
    
    
    public TurnoDeOperador(String json) {
        operadores = new ArrayList<>();
        
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject turnoJSON = reader.readObject();
        
        reader = factory.createReader(new StringReader(turnoJSON.getString("OperadoresEnTurno")));
        JsonArray operadoresJSON = reader.readArray();
        
        fechaTurno = LocalDate.parse(turnoJSON.getString("FechaTurno"));
        fechaCreacion = LocalDate.parse(turnoJSON.getString("FechaCreacion"));
        tipoTurno = TipoDeTurnoOperador.valueOf(turnoJSON.getString("NombreTipo"));
        
        for(JsonValue v : operadoresJSON){
            Empleado e = new Empleado(v.toString());
            operadores.add(e);
        }
        
    }

    public LocalDate getFechaTurno() {
        return fechaTurno;
    }

    public void setFechaTurno(LocalDate fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public TipoDeTurnoOperador getTipoTurno() {
        return tipoTurno;
    }

    public void setTipoTurno(TipoDeTurnoOperador tipoTurno) {
        this.tipoTurno = tipoTurno;
    }

    public ArrayList<Empleado> getOperadores() {
        return operadores;
    }

    public void setOperadores(ArrayList<Empleado> operadores) {
        this.operadores = operadores;
    }
    
    
    
    
}
