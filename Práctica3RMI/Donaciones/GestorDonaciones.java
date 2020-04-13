
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class GestorDonaciones extends UnicastRemoteObject implements IGestorDonaciones
{
    private ArrayList<Entidad> entidades;
    private double subtotal;
    private String nombreReplica;
    private int punteroEntidad;

    public GestorDonaciones(String replica) throws RemoteException{
        this.nombreReplica = replica;
        this.subtotal = 0;
        this.entidades = new ArrayList<>();
        this.punteroEntidad = 0;
    }

    @Override
    public boolean registrarse(String usuario, String password) throws RemoteException{
        //Cogemos la replica del servidor para comprobar que el cliente no esté ahí
        IGestorDonaciones rep = this.getRep();
        
        
        //Comprobamos que no existe en la réplica local, es decir, en el propio servidor
        if(this.existeEntidad(usuario)){
            System.out.println("Ya hay una entidad registrada con ese usuario");
            return false;
        }

        //Comprobamos que la entidad no está registrada en la réplica
        if(rep!=null){
            if(rep.existeEntidad(usuario)){
                System.out.println("Ya hay una entidad registrada con ese usuario");
                return false;
            }
        }

        //Añadimos la entidad a la réplica que tenga menos entidades
        if(this.getEntidades().size() < rep.getEntidades().size())
            this.aniadirEntidad(new Entidad(usuario,password));
        
        else
            rep.aniadirEntidad(new Entidad(usuario,password));

        System.out.println("Entidad con nombre de usuario: " + usuario + " añadida");
        //this.mostrarEntidades();
        return true;
    }
    
    @Override
    public boolean iniciarSesion (String usuario, String password) throws RemoteException{
        //Cogemos la replica del servidor para comprobar que el cliente no esté ahí
        IGestorDonaciones rep = this.getRep();

        //Comprobamos si está en local
        if(this.existeEntidad(usuario)){
            Entidad entidad = this.obtenerEntidad();

            if(entidad.getPassword().equals(password))
                return true;
        }

        //Comprobamos si está en la replica
        if(rep!=null){
            if(rep.existeEntidad(usuario)){
                
                Entidad entidad = rep.obtenerEntidad();

                if(entidad.getPassword().equals(password))
                    return true;
            }
        }
        //this.mostrarEntidades();
        return false;
    } 



    @Override
    public void aniadirEntidad(Entidad ent) throws RemoteException{
        this.entidades.add(ent);
    }

    @Override
    public ArrayList<Entidad> getEntidades() throws RemoteException{
        return this.entidades;
    }
    
    @Override
    public boolean existeEntidad(String usuario) throws RemoteException{
        boolean resultado = false;
        for(int i=0;i<entidades.size() && !resultado;i++){
            if(this.entidades.get(i).getUsuario().equals(usuario))
                resultado = true;
                punteroEntidad = i;
        }

        return resultado;
    }
    
    @Override
    public Entidad obtenerEntidad() throws RemoteException{
        return this.entidades.get(punteroEntidad);
    }
    
    @Override
    public IGestorDonaciones getRep() throws RemoteException{
        IGestorDonaciones rep = null;
        
        try {
            Registry mireg = LocateRegistry.getRegistry("localhost", 1099);
            rep = (IGestorDonaciones)mireg.lookup(this.nombreReplica);
        } 
        catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }

        return rep;
    }

    @Override
    public void aumentarSubtotal(double cantidad) throws RemoteException{
        this.subtotal+=cantidad;
    }

    @Override
    public boolean donar(String usuario, double cantidad) throws RemoteException{
        //Cogemos la replica del servidor para comprobar que el cliente no esté ahí
        IGestorDonaciones rep = this.getRep();



        //Comprobamos si está en local
        if(this.existeEntidad(usuario)){
            Entidad entidad = this.obtenerEntidad();
            entidad.aumentarDonacion(cantidad);
            System.out.println(entidad.getDonacion());
            this.aumentarSubtotal(cantidad);
            return true;
        }

        //Comprobamos si está en la replica
        if(rep!=null){
            if(rep.existeEntidad(usuario)){
                Entidad entidad = rep.obtenerEntidad();
                entidad.aumentarDonacion(cantidad);
                System.out.println(entidad.getDonacion());
                rep.aumentarSubtotal(cantidad);
                return true;
            }
        }


        return false;
    }

    @Override
    public double getSubTotal() throws RemoteException{
        return this.subtotal;
    }

    @Override
    public double getTotal() throws RemoteException{
        IGestorDonaciones rep = this.getRep();

        if(rep!=null)
            return this.getSubTotal() + rep.getSubTotal();
        else
            return this.getSubTotal();
    }


    public double obtenerTotalCliente(String usuario) throws RemoteException{
        //Cogemos la replica del servidor para comprobar que el cliente no esté ahí
        IGestorDonaciones rep = this.getRep();
        double resultado = 0;

        //Comprobamos si está en local
        
        if(this.existeEntidad(usuario)){
            System.out.println("Entra en local obtenerTotalCliente");
            Entidad entidad = this.obtenerEntidad();
            System.out.println(entidad.getDonacion());
            resultado = entidad.getDonacion();
        }

        //Comprobamos si está en la replica
        if(rep!=null){
            if(rep.existeEntidad(usuario)){
                System.out.println("Entra en replica obtenerTotalCliente");
                Entidad entidad = rep.obtenerEntidad();
                System.out.println(entidad.getDonacion());
                resultado = entidad.getDonacion();
            }
        }
        return resultado;
    }

    public void mostrarEntidades() throws RemoteException{

        System.out.println("Mostrando entidades en local");
        for(int i=0;i<this.entidades.size();i++){
            System.out.println("Entidad " + i + " Nombre: " + this.entidades.get(i).getUsuario() + " Contraseña: " + this.entidades.get(i).getPassword());
        }

        //Cogemos la replica del servidor para comprobar que el cliente no esté ahí
        IGestorDonaciones rep = this.getRep();
        if(rep!=null){
            System.out.println("Mostrando entidades en réplica");
            for(int i=0;i<rep.getEntidades().size();i++){
                System.out.println("Entidad " + i + " Nombre: " + this.entidades.get(i).getUsuario() + " Contraseña: " + this.entidades.get(i).getPassword());
            }
        }
    }

}
