import glob
import sys
from calculadora import Calculadora
# from calculadora . ttypes import Operation
#Lo de ttypes es si hubieramos anadido tipos en el fichero . thrift
# hay que instalar antes el paquete thrift de python
#(no confundir con el compilador thrift )
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer
import logging
logging.basicConfig ( level = logging . DEBUG )
# Esto es para imprimir cuando haya errores en el
# servidor y poder depurar !


class CalculadoraHandler :
    def __init__ ( self ):
        self . log = {}

    def ping ( self ):
        print ("Me han hecho ping")
    
    def sumar ( self , n1 , n2 ):
        print ("Sumando "  + str( n1 ) + " con "+ str ( n2 ))
        return n1 + n2
    
    def restar ( self , n1 , n2 ):
        print ("Restando "  + str( n1 ) + " con "+ str ( n2 ))
        return n1 - n2

    def multiplicar ( self , n1 , n2 ):
        print ("Multplicando "  + str( n1 ) + " con "+ str ( n2 ))
        return n1 * n2

    def dividir ( self , n1 , n2 ):
        print ("Dividiendo "  + str( n1 ) + " con "+ str ( n2 ))
        return n1 / n2

    def sumarVectores ( self , v1 , v2 ):
        print ("Sumando v1 con v2")
        resultado = []
        for i in range(len(v1)):
            resultado.append(v1[i] + v2[i])
        return resultado
    
    def restarVectores ( self , v1 , v2 ):
        print ("Restando v1 con v2")
        resultado = []
        for i in range(0,len(v1)):
            resultado.append(v1[i] - v2[i])
        return resultado

    def multiplicarVectores ( self , v1 , v2 ):
        print ("Multiplicando v1 con v2")
        resultado = []
        for i in range(0,len(v1)):
            resultado.append(v1[i] * v2[i])
        return resultado

    def productoVectorial(self, v1, v2):
        print ("Realizando el producto vectorial de v1 con v2")
        resultado = [v1[1]*v2[2] - v1[2]*v2[1], v1[2]*v2[0] - v1[0]*v2[2], v1[0]*v2[1] - v1[1]*v2[0]]
        return resultado
    
    def productoEscalar(self,v1,v2):
        print ("Realizando el producto escalar de v1 con v2")
        resultado = 0
        for i in range(0,len(v1)):
            resultado = resultado + v1[i] * v2[i]
        return resultado
    
    def productoMatrices(self,m1,m2):
        print ("Realizando el producto matricial de m1 con m2")
        filasM1 = len(m1)
        colsM1 = len(m1[0])
        filasM2 = len(m2)
        colsM2 = len(m2[0])

        if colsM1 != filasM2:
            print("No se pueden multiplicar las matrices, las dimensiones no son validas")
            return
        
        resultado = [[0 for fila in range(colsM2)] for col in range (filasM1)]

        for i in range (filasM1):
            for j in range (colsM2):
                for k in range (colsM1):
                    resultado[i][j] += m1[i][k] * m2[k][j]
        
        return resultado


if __name__ == '__main__':
    handler = CalculadoraHandler ()
    processor = Calculadora . Processor ( handler )
    transport = TSocket . TServerSocket ( host ='127.0.0.1', port =9090)
    tfactory = TTransport . TBufferedTransportFactory ()
    pfactory = TBinaryProtocol . TBinaryProtocolFactory ()
    server = TServer . TSimpleServer ( processor , transport , tfactory ,
    pfactory )
    print ("Iniciando servidor ... ")
    server . serve ()
    print ("done .")
