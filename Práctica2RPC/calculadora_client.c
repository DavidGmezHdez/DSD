/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "calculadora.h"


void
calculadora_1(char *host)
{
	CLIENT *clnt;
	double  *result_1;
	int sumar_1_arg1;
	int sumar_1_arg2;
	double  *result_2;
	int restar_1_arg1;
	int restar_1_arg2;
	double  *result_3;
	int multiplicacion_1_arg1;
	int multiplicacion_1_arg2;
	double  *result_4;
	int dividir_1_arg1;
	int dividir_1_arg2;

#ifndef	DEBUG
	clnt = clnt_create (host, CALCULADORA, CALCVER, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */

	result_1 = sumar_1(sumar_1_arg1, sumar_1_arg2, clnt);
	if (result_1 == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_2 = restar_1(restar_1_arg1, restar_1_arg2, clnt);
	if (result_2 == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_3 = multiplicacion_1(multiplicacion_1_arg1, multiplicacion_1_arg2, clnt);
	if (result_3 == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
	result_4 = dividir_1(dividir_1_arg1, dividir_1_arg2, clnt);
	if (result_4 == (double *) NULL) {
		clnt_perror (clnt, "call failed");
	}
#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	 /* DEBUG */
}


int
main (int argc, char *argv[])
{
	char *host;

	if (argc < 2) {
		printf ("usage: %s server_host\n", argv[0]);
		exit (1);
	}
	host = argv[1];
	calculadora_1 (host);
exit (0);
}
