import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;


public class servidor2 {
    public static void main(String[] args) {
        // Crea e instala el gestor de seguridad
        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try{
            // Crea una instancia de contador
            //System.setProperty("java.rmi.server.hostname","192.168.1.107");
            GestorDonaciones gestordonaciones1 = new GestorDonaciones("gestordonaciones1");
            Naming.rebind("gestordonaciones2", gestordonaciones1);
            System.out.println("Gestor de donaciones 2 preparado");
        } catch(RemoteException | MalformedURLException e) {
            System.out.println("Exception: "+ e.getMessage());
        }
    }
}