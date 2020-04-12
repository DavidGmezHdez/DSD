
public class Entidad {

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

    public String getContraseña(){
        return this.contraseña;
    }

    public double getDonacion(){
        return this.dineroDonado;
    }

    public aumentarDonacion(double d){
        this.dineroDonado+=d;
    }

}