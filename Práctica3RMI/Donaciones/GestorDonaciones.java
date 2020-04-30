
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

    IGestorDonaciones replica;

    public GestorDonaciones(String replica) throws RemoteException{
        this.nombreReplica = replica;
        this.subtotal = 0;
        this.entidades = new ArrayList<>();
        this.punteroEntidad = 0;
    }

    @Override
    public boolean registrarse(String usuario, String password) throws RemoteException{        
        //Comprobamos que no existe en la réplica local, es decir, en el propio servidor
        if(this.existeEntidad(usuario)){
            System.out.println("Ya hay una entidad registrada con ese usuario");
            return false;
        }

        //Comprobamos que la entidad no está registrada en la réplica
        if(this.existeRep()){
            if(this.replica.existeEntidad(usuario)){
                System.out.println("Ya hay una entidad registrada con ese usuario");
                return false;
            }
        }

        //Añadimos la entidad a la réplica que tenga menos entidades
        if(this.getEntidades().size() < this.replica.getEntidades().size())
            this.aniadirEntidad(new Entidad(usuario,password));
        else
            this.replica.aniadirEntidad(new Entidad(usuario,password));

        System.out.println("Entidad con nombre de usuario: " + usuario + " añadida");
        return true;
    }
    
    @Override
    public boolean iniciarSesion (String usuario, String password) throws RemoteException{
        //Comprobamos si está en local
        if(this.existeEntidad(usuario)){
            Entidad entidad = this.obtenerEntidad();

            if(entidad.getPassword().equals(password))
                return true;
        }

        //Comprobamos si está en la replica
        if(this.existeRep()){
            if(this.replica.existeEntidad(usuario)){
                
                Entidad entidad = this.replica.obtenerEntidad();

                if(entidad.getPassword().equals(password))
                    return true;
            }
        }
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
    public boolean existeRep() throws RemoteException{
        this.replica = null;
        boolean existeReplica = false;
        try {
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            this.replica = (IGestorDonaciones)reg.lookup(this.nombreReplica);
        } 
        catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }

        if(this.replica!=null)
            existeReplica = true;
        
        return existeReplica;

    }
    

    @Override
    public void aumentarSubtotal(double cantidad) throws RemoteException{
        this.subtotal+=cantidad;
    }

    @Override
    public boolean donar(String usuario, double cantidad) throws RemoteException{

        //Comprobamos si está en local
        if(this.existeEntidad(usuario)){
            Entidad entidad = this.obtenerEntidad();
            entidad.aumentarDonacion(cantidad);
            this.aumentarSubtotal(cantidad);
            return true;
        }

        //Comprobamos si está en la replica
        if(this.existeRep()){
            if(this.replica.existeEntidad(usuario)){
                Entidad entidad = this.replica.obtenerEntidad();
                entidad.aumentarDonacion(cantidad);
                this.replica.aumentarSubtotal(cantidad);
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
        if(this.existeRep()){
            System.out.println("Subtotal 1: " + this.getSubTotal() + " Subtotal 2: " + this.replica.getSubTotal());
            return this.getSubTotal() + this.replica.getSubTotal();
        }
        else{
            System.out.println("Subtotal 1: " + this.getSubTotal());
            return this.getSubTotal();
        }
    }


    public double obtenerTotalCliente(String usuario) throws RemoteException{
        double resultado = 0;

        //Comprobamos si está en local
        
        if(this.existeEntidad(usuario)){
            Entidad entidad = this.obtenerEntidad();
            resultado = entidad.getDonacion();
        }

        //Comprobamos si está en la replica
        if(this.existeRep()){
            if(this.replica.existeEntidad(usuario)){
                Entidad entidad = this.replica.obtenerEntidad();
                resultado = entidad.getDonacion();
            }
        }
        return resultado;
    }

    public void mostrarEntidades() throws RemoteException{

        System.out.println("Mostrando entidades en local");
        for(int i=0;i<this.entidades.size();i++){
            System.out.println("Entidad " + i + " Nombre: " + this.entidades.get(i).getUsuario() + " Contraseña: " + this.entidades.get(i).getPassword() + " Cantidad donada: "+ this.entidades.get(i).getDonacion());
        }

        //Cogemos la replica del servidor para comprobar que el cliente no esté ahí
        if(this.existeRep()){
            System.out.println("Mostrando entidades en réplica");
            for(int i=0;i<this.replica.getEntidades().size();i++){
                System.out.println("Entidad " + i + " Nombre: " + this.replica.getEntidades().get(i).getUsuario() + " Contraseña: " + this.replica.getEntidades().get(i).getPassword() + " Cantidad donada: " + this.replica.getEntidades().get(i).getDonacion());
            }
        }
    }

}
