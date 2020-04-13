import java.io.Serializable;


public class Entidad implements Serializable {
    private static final long serialVersionUID = 1;
    private String usuario, contraseña;
    private double dineroDonado;


    Entidad(String usu, String contra){
        this.usuario = usu;
        this.contraseña = contra;
        this.dineroDonado = 0;
    }

    public String getUsuario(){
        return this.usuario;
    }

    public String getPassword(){
        return this.contraseña;
    }

    public double getDonacion(){
        return this.dineroDonado;
    }

    public void aumentarDonacion(double d){
        this.dineroDonado+=d;
    }

}