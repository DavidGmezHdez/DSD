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
            IGestionDonaciones gestor1 = (IGestionDonaciones)Naming.lookup(servidor1);
            IGestionDonaciones gestor2 = (IGestionDonaciones)Naming.lookup(servidor2);
            boolean inicioDeSesion = true;

            String nombreUsuario,contraseña;

            while(inicioDeSesion){
                System.out.println("Bienvenido al gestor de donaciones de DSD.SA. Seleccione una acción");
                System.out.println("1.- Registrarse");
                System.out.println("2.- Iniciar Sesión");
                System.out.println("0.- Salir");

                int opcion = scan.nextInt();

                switch(opcion){
                    case 1:
                        System.out.println("Ha solicitado usted registrarse");
                        System.out.println("Introduzca el nombre de usuario de la entidad");
                        nombreUsuario = scan.nextLine();

                        System.out.println("Introduzca una contraseña");
                        contraseña = scan.nextLine();

                        System.out.println("Elija en que servidor desea registarse (1 o 2)");
                        servidor = scan.nextInt();

                        while(servidor!=1 || servidor!=2){
                            System.out.println("¿Valor incorrecto (solo 1 o 2)");
                            servidor = scan.nextInt();
                        }

                        switch(servidor){
                            case 1:
                                if(gestor1.registrarse(nombreUsuario,contraseña)
                                    System.out.println("Entidad registrada en el servidor 1");
                                else
                                    System.out.println("Esta entidad ya está registrada en el otro servidor");
                                break;
                            case 2:
                                if(gestor2.registrarse(nombreUsuario,contraseña)
                                    System.out.println("Entidad registrada en el servidor 2");
                                else
                                    System.out.println("Esta entidad ya está registrada en el otro servidor");
                        }
                        break;
                    
                    case 2:
                        System.out.println("Ha solicitado usted iniciar sesión");
                        System.out.println("Introduzca el nombre de usuario de la entidad");
                        nombreUsuario = scan.nextLine();

                        System.out.println("Introduzca una contraseña");
                        contraseña = scan.nextLine();

                        if(gestor1.iniciarSesion(nombreUsuario,contraseña)){
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
            }

        } catch(NotBoundException | RemoteException e) {
            System.err.println("Exception del sistema: "+ e);
        }
        System.exit(0);
    }
}