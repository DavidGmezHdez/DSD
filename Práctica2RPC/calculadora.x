
/* Archivo calculadora.x */


typedef float t_vector<>;

program CALCULADORA{
    version CALCVER{
        double SUMAR(int a, int b) = 1;
        double RESTAR(int a, int b) = 2;
        double MULTIPLICAR(int a, int b) = 3;
        double DIVIDIR(int a, int b) = 4;
        t_vector SUMAVECTORES(t_vector vec1, t_vector vec2) = 5;
        t_vector RESTAVECTORES(t_vector vec1, t_vector vec2) = 6;
        t_vector PRODUCTOVECTORES(t_vector vec1, t_vector vec2) = 7;
    } = 1;
} = 0x20000001; 

