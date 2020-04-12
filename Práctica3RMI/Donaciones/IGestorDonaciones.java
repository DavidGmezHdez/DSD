
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface IGestorDonaciones extends Remote{
    public boolean registrarse(String usuario, String contraseña) throws RemoteException;
    
    public boolean iniciarSesion (String usuario, String contraseña) throws RemoteException;
    
    public boolean existeEntidad(String usuario) throws RemoteException;
    
    public Entidad obtenerEntidad() throws RemoteException;
    
    public IGestorDonaciones getRep() throws RemoteException;

    public boolean donar(String usuario, double cantidad) throws RemoteException;

    public void aniadirEntidad(Entidad ent) throws RemoteException;

    public ArrayList<Entidad> getEntidades() throws RemoteException;

    public void aumentarSubtotal(double cantidad) throws RemoteException;




    
}
