
package negocio.modelos.persona;

import java.io.StringReader;
import java.time.LocalDate;
import javax.json.Json;

import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import negocio.modelos.general.Direccion;

/**
 *
 * clase encargada de almacenar la informacion de un asegurado en el sistema
 */
public class Persona {
    private String nif;
    private String nombre;
    private String apellidos;
    private LocalDate fechaDeNacimiento;
    private String telefono;
    private Direccion direccion;
     
    /**
     * Construccion del objeto mediante el uso de un objeto JSON en forma string
     * @param json objeto con la informacion
     */
    public Persona(String json) {
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonReader reader = factory.createReader(new StringReader(json));
        JsonObject entryJSON = reader.readObject();
        
        reader = factory.createReader(new StringReader(entryJSON.getString("Persona")));
        JsonObject personaJSON = reader.readObject();
        
        this.nif = personaJSON.getString("Nif");
        this.nombre = personaJSON.getString("Nombre");
        this.apellidos = personaJSON.getString("Apellidos");
        this.fechaDeNacimiento = LocalDate.parse(personaJSON.getString("FechaDeNacimiento"));
        this.telefono =personaJSON.getString("Telefono");
        String direccionJSON = personaJSON.getString("Direccion");
        this.direccion = new Direccion(direccionJSON); 
    }

    //GETTERS & SETTES
    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String Apellidos) {
        this.apellidos = Apellidos;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(LocalDate FechaDeNacimiento) {
        this.fechaDeNacimiento = FechaDeNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
    
	/**
	 * Devuelve si el actual objeto es igual al dado 
	 * Se compara usando el nif como identificador unico de cada persona
	 * @param obj Objeto a comparar con este
	 * @return true si es el mismo objeto o si tienen el mismo nif (son objetos Persona)
	 */
    @Override
    public boolean equals(Object obj) {

        //Comprobar si son el mismo objet
        if (this == obj) return true;
        
        if (obj == null) return false;
        
        if (getClass() != obj.getClass()) return false;

        // Castear el objeto a Persona
        Persona otraPersona = (Persona) obj;
        
        // Comparar atributos
		//Si mi nif es nulo y el de la otra persona entonces no
        if (nif == null) if(otraPersona.nif!= null) return false;
		//si tenemos nif distintos entonces tampoco
        else if (!nif.equals(otraPersona.nif)) return false;

		//Solo queda el caso en el que tiene el mismo nif
        return true;
    }
   
    
}
