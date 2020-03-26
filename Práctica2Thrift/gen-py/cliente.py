from calculadora import Calculadora
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

transport = TSocket.TSocket ('localhost', 9090)
transport = TTransport.TBufferedTransport ( transport )
protocol = TBinaryProtocol.TBinaryProtocol ( transport )
# creamos el cliente
client = Calculadora.Client ( protocol )

transport . open ()
print (" Hacemos ping al server ")
client.ping ()

print("Hacemos la suma 1+1")
resultado = client.sumar (1 , 1)
print (" 1+1= "+ str( resultado ))

print("Hacemos la resta 1+1")
resultado = client.restar (1 , 1)
print ("1 -1="+ str( resultado ))

print("Hacemos la multiplicacion 5x2")
resultado = client.multiplicar(5, 2)
print ("5x2="+ str(resultado))

print("Hacemos la division 10x2")
resultado = client.dividir(10, 2)
print ("10/2="+ str(resultado))

v1 = [1,2,3,4,5]
v2 = [5,4,3,2,1]

print("Mostramos los vectores")
print("v1")
print(v1)

print("v2")
print(v2)

print("Realizamos la suma de v1 y v2")
resultado = [0,0,0,0,0]
resultado = client.sumarVectores(v1,v2)
print(resultado)

print("Realizamos la resta de v1 y v2")
resultado = [0,0,0,0,0]
resultado = client.restarVectores(v1,v2)
print(resultado)

print("Realizamos la multiplicacion de v1 y v2")
resultado = [0,0,0,0,0]
resultado = client.multiplicarVectores(v1,v2)
print(resultado)


transport . close ()
