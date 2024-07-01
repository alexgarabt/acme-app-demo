package interfaz;

/**
 * Clase Lanzadera de la aplicacion
 */
public class Main {

    
    public static void main(String[] args) {
        GestorInterfazUsuario gestor = GestorInterfazUsuario.getInstance();
        //Inicializa en el CU1, en la vista para identificarse en el sistema
        gestor.mostrarVistaIdentificarseSistema();
    }
} 