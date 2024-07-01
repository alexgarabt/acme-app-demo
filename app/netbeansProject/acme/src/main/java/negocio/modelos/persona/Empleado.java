package negocio.modelos.persona;

import java.time.LocalDate;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import java.io.StringReader;

/**
 * Representa a un Empleado concreto con todos sus atributos y usos
 */
public class Empleado extends Persona {

    private LocalDate fechaInicioEnPuesto;
    private LocalDate fechaPermisoConduccion = null;
    private String numeroColegiadoSiMedico;
    private LocalDate fechaInicioEmpresa;
    private TipoDeRol rol;
    private TipoDeDisponibilidad disponibilidad;
    private TipoDeVinculacion vinculacion;
    
     /**
     * Construccion del objeto mediante el uso de un objeto JSON en forma string
     * @param json objeto con la informacion
     */
    public Empleado(String json) {
        super(json);
        
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject empleadoJSON = reader.readObject();

        reader = factory.createReader(new StringReader(empleadoJSON.getString("RolActual")));
        JsonObject rolActualJSON = reader.readObject();

        reader = factory.createReader(new StringReader(empleadoJSON.getString("DisponibilidadActual")));
        JsonObject disponibilidadActualJSON = reader.readObject();
        
        reader = factory.createReader(new StringReader(empleadoJSON.getString("VinculacionActual")));
        JsonObject vinculacionActualJSON = reader.readObject();

        fechaInicioEmpresa = LocalDate.parse(empleadoJSON.getString("FechaInicioEnEmpresa"));
        vinculacion = TipoDeVinculacion.valueOf(vinculacionActualJSON.getString("NombreTipo"));
        rol = TipoDeRol.valueOf(rolActualJSON.getString("NombreTipo"));
        disponibilidad = TipoDeDisponibilidad.valueOf(disponibilidadActualJSON.getString("NombreTipo"));
        String aux = rolActualJSON.getString("FechaPermisoConduccion");
        if (!aux.equals("")) {
            fechaPermisoConduccion = LocalDate.parse(aux);
        }
        numeroColegiadoSiMedico = rolActualJSON.getString("NumeroColegiadoMedico");
    }
    
    // GETTERS AND SETTER for the atributtes of the class

    public LocalDate getFechaInicioEnEmpresa() {
        return fechaInicioEmpresa;
    }

    public final void setFechaInicioEnEmpresa(LocalDate fechaInicioEnEmpresa) {
        this.fechaInicioEmpresa = fechaInicioEnEmpresa;
    }

    public TipoDeRol getRolActual() {
        return rol;
    }

    public void setRolActual(TipoDeRol rolActual) {
        this.rol = rolActual;
    }

    public LocalDate getFechaInicioEnPuesto() {
        return fechaInicioEnPuesto;
    }

    public void setFechaInicioEnPuesto(LocalDate fechaInicioEnPuesto) {
        this.fechaInicioEnPuesto = fechaInicioEnPuesto;
    }

    public TipoDeDisponibilidad getDisponibilidadActual() {
        return disponibilidad;
    }

    public void setDisponibilidadActual(TipoDeDisponibilidad disponibilidadActual) {
        this.disponibilidad = disponibilidadActual;
    }

    public TipoDeVinculacion getVinculacionActual() {
        return vinculacion;
    }

    public void setVinculacionActual(TipoDeVinculacion vinculacionActual) {
        this.vinculacion = vinculacionActual;
    }

    public LocalDate getFechaPermisoConduccion() {
        return fechaPermisoConduccion;
    }

    public void setFechaPermisoConduccion(LocalDate fechaPermisoConduccion) {
        this.fechaPermisoConduccion = fechaPermisoConduccion;
    }

    public String getNumeroColegiadoMedico() {
        return numeroColegiadoSiMedico;
    }

    public void setNumeroColegiadoMedico(String numeroColegiadoMedico) {
        this.numeroColegiadoSiMedico = numeroColegiadoMedico;
    }

    public boolean estaEnActivo() {
        return vinculacion == TipoDeVinculacion.ConNormalidad && disponibilidad == TipoDeDisponibilidad.Disponible;
    }
    

}
