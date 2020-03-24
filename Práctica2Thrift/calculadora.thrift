service Calculadora {
    void ping (),
    double sumar (1: i32 num1 , 2: i32 num2 ),
    double restar (1: i32 num1 , 2: i32 num2 ),
    double multiplicar (1: i32 num1 , 2: i32 num2 ),
    double dividir (1: i32 num1 , 2: i32 num2 ),
    list<i32> sumarVectores (1: list<i32> v1 , 2: list<i32> v2 ),
    list<i32> restarVectores (1: list<i32> v1 , 2: list<i32> v2 ),
    list<i32> multiplicarVectores (1: list<i32> v1 , 2: list<i32> v2 ),
    list<i32> productoVectorial (1: list<i32> v1 , 2: list<i32> v2 ),
    double productoEscalar (1: list<i32> v1 , 2: list<i32> v2 ),
    list<list<i32>> productoMatrices (1: list<list<i32>> m1, 2: list<list<i32>> m2),

}