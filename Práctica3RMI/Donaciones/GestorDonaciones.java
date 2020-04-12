
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class GestorDonaciones extends UnicastRemoteObject implements IGestorDonaciones
{
    private ArrayList<Entidad> entidades;
    private double subtotal;
    private String nombreReplica;
    private int punteroEntidad;

    GestorDonaciones(String replica){
        this.nombreReplica = replica;
        this.subtotal = 0;
        this.entidades = new ArrayList<>();
        this.punteroEntidad = 0;
    }

    @Override
    public boolean registrarse(String usuario, String contraseña) throws RemoteException{
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
            this.aniadirEntidad(new Entidad(usuario,contraseña));
        
        else
            rep.aniadirEntidad(new Entidad(usuario,contrasela));

        System.out.println("Entidad con nombre de usuario: " + usuario + " añadida");
        return true;
    }
    
    @Override
    public boolean iniciarSesion (String usuario, String contraseña) throws RemoteException{
        //Cogemos la replica del servidor para comprobar que el cliente no esté ahí
        IGestorDonaciones rep = this.getRep();

        //Comprobamos que está en el local
        if(this.existeEntidad(usuario)){
            Entidad encontrada = this.obtenerEntidad();

            if(encontrada.getContraseña().equals(contraseña))
                return true;
        }

        //Comprobamos que está en la replica
        if(rep!=null){
            if(rep.existeEntidad(usuario)){
                Entidad encontrada = this.obtenerEntidad();

                if(encontrada.getContraseña().equals(contraseña))
                    return true;
            }
        }
        return false;
    } 



    @Override
    public void aniadirEntidad(Entidad ent) throws RemoteException{
        this.entidades.push(ent);
    }

    @Override
    public ArrayList<Entidad> getEntidades() throws RemoteException{
        return this.entidades;
    }
    

    
    @Override
    public boolean existeEntidad(String usuario) throws RemoteException{
        boolean resultado = false;
        for(int i=0;i<entidades.size() && !resultado;i++){
            if(this.entidades.get(i).getUsuario() == usuario)
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
            rep = (IDonaciones)mireg.lookup(this.nombreReplica);
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

        //Comprobamos si está en la replica
        if(rep!=null){
            if(rep.existeEntidad(usuario)){
                Entidad encontrada = this.obtenerEntidad();
                encontrada.aumentarDonacion(cantidad);
                rep.aumentarSubtotal(cantidad);
                return true;
            }
        }
        //Comprobamos si está en local
        if(this.existeEntidad(usuario)){
            Entidad encontrada = this.obtenerEntidad();
            encontrada.aumentarDonacion(cantidad);
            this.aumentarSubtotal(cantidad);
            return true;
        }

        return false;
    }

}
