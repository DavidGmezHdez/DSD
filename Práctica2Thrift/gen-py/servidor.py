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
