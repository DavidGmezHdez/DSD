import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;
import java.rmi.*;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Cliente{
    public static void main(String[] args) {
        int servidor = 0;
        String servidor1 = "gestordonaciones1";
        String servidor2 = "gestordonaciones2";
        Scanner scan = new Scanner(System.in);
        boolean clienteRegistrado = false;

        // Crea e instala el gestor de seguridad
        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try{
            // Crea el stub para el cliente especificando el nombre del servidor
            IGestorDonaciones gestor1 = (IGestorDonaciones)Naming.lookup("gestordonaciones1");
            IGestorDonaciones gestor2 = (IGestorDonaciones)Naming.lookup("gestordonaciones2");
            boolean inicioDeSesion = true;

            String nombreUsuario = "",password = "";

            while(inicioDeSesion){
                System.out.println("Bienvenido al gestor de donaciones de DSD.SA. Seleccione una acción");
                System.out.println("1.- Registrarse");
                System.out.println("2.- Iniciar Sesión");
                System.out.println("0.- Salir");

                int opcion = scan.nextInt();

                switch(opcion){
                    case 1:
                        nombreUsuario = "";
                        password = "";
                        System.out.println("Ha solicitado usted registrarse");

                        while(nombreUsuario.equals("")){
                            System.out.println("Introduzca el nombre de usuario de la entidad");
                            nombreUsuario = scan.nextLine();
                            
                        }
                        
                        while(password.equals("")){
                            System.out.println("Introduzca una password");
                            password = scan.nextLine();
                        }

                        if(gestor1.registrarse(nombreUsuario,password))
                            System.out.println("Entidad registrada");
                        break;
                        
                    
                    case 2:

                        nombreUsuario = "";
                        password = "";
                        System.out.println("Ha solicitado usted iniciar sesión");
                        
                        while(nombreUsuario.equals("")){
                            System.out.println("Introduzca el nombre de usuario de la entidad");
                            nombreUsuario = scan.nextLine();
                            
                        }
                        
                        while(password.equals("")){
                            System.out.println("Introduzca una password");
                            password = scan.nextLine();
                        }
                        

                        if(gestor1.iniciarSesion(nombreUsuario,password)){
                            System.out.println("Inicio de sesión completada");
                            clienteRegistrado = true;          
                        }
                        else{
                            System.out.println("No tenemos ningún cliente con esos datos. Compruebe que los datos son correctos o registre una nueva entidad");
                            clienteRegistrado = false;
                        }
                        break;
                    
                    case 0:
                        System.out.println("Saliendo del gestor");
                        inicioDeSesion = false;
                        clienteRegistrado = false;
                        break;
                    
                    default:
                        System.out.println("Opción no válida");
                        System.out.println("1.- Registrarse");
                        System.out.println("2.- Iniciar Sesión");
                        System.out.println("0.- Salir");
                        break;
                }
                
                while(clienteRegistrado){
                    System.out.println("Ventaa de usuario registrado. Seleccione una opción");
                    System.out.println("1.- Donar");
                    System.out.println("2.- Consultar dinero");
                    System.out.println("0.- Cerrar sesión");
    
                    opcion = scan.nextInt();
                    switch(opcion){
                        case 1:
                            System.out.println("Introduzca la cantidad que desea donar");
                            double cantidad = scan.nextDouble();
    
                            while(cantidad < 0.0){
                                System.out.println("La cantidad debe ser mayor o igual que 0");
                                System.out.println("Introduzca la cantidad que desea donar");
                                cantidad = scan.nextDouble();
                            }
    
                            if(gestor1.donar(nombreUsuario,cantidad)){
                                System.out.println("Donacion completada. Muchas gracias");
                            }
                            break;
                        
                        case 2:
                            System.out.println("La cantidad de dinero aportado es de " + gestor1.getTotal());
                            System.out.println("Usted ha aportado una cantidad de " + gestor1.obtenerTotalCliente(nombreUsuario));
                            break;
                        case 0:
                            System.out.println("Cerrando sesión");
                            clienteRegistrado = false;
                            inicioDeSesion = true;
                            break;
                    }
                }



            }
        } 
        catch(NotBoundException | RemoteException | MalformedURLException e) {
            System.err.println("Exception del sistema: "+ e);
        }
        System.exit(0);
    }
}