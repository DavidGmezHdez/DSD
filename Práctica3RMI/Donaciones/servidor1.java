import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
//import contador.contador;

public class servidor1 {
    public static void main(String[] args) {
        // Crea e instala el gestor de seguridad
        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try{
            // Crea una instancia de contador
            //System.setProperty("java.rmi.server.hostname","192.168.1.107");
            Registry reg=LocateRegistry.createRegistry(1099);
            GestorDonaciones gestordonaciones1 = new GestorDonaciones("gestordonaciones2");
            Naming.rebind("gestordonaciones1", gestordonaciones1);
            System.out.println("Gestor de donaciones 1 preparado");
        } catch(RemoteException | MalformedURLException e) {
            System.out.println("Exception: "+ e.getMessage());
        }
    }
}