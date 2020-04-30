
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface IGestorDonaciones extends Remote{
    public boolean registrarse(String usuario, String password) throws RemoteException;
    
    public boolean iniciarSesion (String usuario, String password) throws RemoteException;
    
    public boolean existeEntidad(String usuario) throws RemoteException;
    
    public Entidad obtenerEntidad() throws RemoteException;

    public boolean existeRep() throws RemoteException;

    public boolean donar(String usuario, double cantidad) throws RemoteException;

    public void aniadirEntidad(Entidad ent) throws RemoteException;

    public ArrayList<Entidad> getEntidades() throws RemoteException;

    public void aumentarSubtotal(double cantidad) throws RemoteException;

    public double getSubTotal() throws RemoteException;

    public double getTotal() throws RemoteException;

    public double obtenerTotalCliente(String usuario) throws RemoteException;

    public void mostrarEntidades() throws RemoteException;

}


