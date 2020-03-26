import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import Calculadora.*;

class CalculadoraHandler implements Calculadora.Iface {
    public void ping (){ System . out . println ("Me han hecho ping " );}
    
    public double sumar ( int a , int b ){ 
        System.out.println("Sumando " + a + " con " + b);
        return a + b;
    }
    
    public double restar ( int a , int b ){ 
        System.out.println("Restando " + a + " con " + b);
        return a - b;
    }
    
    public double multiplicar ( int a , int b ){ 
        System.out.println("Multiplicando " + a + " con " + b);
        return a * b;
    }
    
    public double dividir ( int a , int b ){ 
        System.out.println("Dividiendo " + a + " con " + b);
        return a / b;
    }

    public int[] sumarVectores(int[] v1, int[] v2){
        System.out.println("Sumando v1 con v2");
        int[] resultado = new int[v1.length];
        for(int i=0;i<v1.length;i++){
            resultado[i] = v1[i] + v2[i];
        }
        return resultado;
    }

    public int[] restarVectores(int[] v1, int[] v2){
        System.out.println("Restando v1 con v2");
        int[] resultado = new int[v1.length];
        for(int i=0;i<v1.length;i++){
            resultado[i] = v1[i] - v2[i];
        }
        return resultado;
    }

    public int[] multiplicarVectores(int[] v1, int[] v2){
        System.out.println("Restando v1 con v2");
        int[] resultado = new int[v1.length];
        for(int i=0;i<v1.length;i++){
            resultado[i] = v1[i] * v2[i];
        }
        return resultado;
    }

    public int[] productoVectorial(int[] v1, int[] v2){
        System.out.println("Realizando el producto vectorial de v1 con v2");
        int[] resultado = new int[v1.length];
        resultado[0] = (v1[1] * v2[2]) - (v1[2] * v2[1]);
        resultado[1] = (v1[2] * v2[0]) - (v1[0] * v2[2]);
        resultado[0] = (v1[0] * v2[1]) - (v1[1] * v2[0]);
        return resultado;
    }

    public double productoEscalar(int[] v1, int[] v2){
        System.out.println("Realizando el producto escalar de v1 con v2");
        double resultado = 0;
        for(int i=0;i<v1.length;i++)
            resultado += v1[i] * v2[i];
        return resultado;
    }

    public int[][] productoMatrices(int[][] m1, int[][] m2){
        System.out.println("Realizando el producto de matrices de m1 con m2");
        int[][] resultado = new int[m1.length][m2[0].length];
        if(m1[0].length == m2.length){
            for(int i= 0; i < m1.length; i++){
                for(int j = 0; j < m2[0].length; j++){
                    for(int k = 0; k < m1[0].length; k++){
                        resultado[i][j] += m1[i][k] * m2[k][j];
                    }
                }
            }
        }
        return resultado;
    }


}
// Lanzar el servidor en el static void main ()
try {
    TServerTransport serverTransport = new TServerSocket (9090);
    TServer server = new TSimpleServer ( new Args ( serverTransport ).
    processor ( processor ));
    System .out.println (" Iniciando servidor ... ");
    server.serve();
} catch ( Exception e) { e. printStackTrace (); }