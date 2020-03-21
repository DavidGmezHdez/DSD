/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include <memory.h> /* for memset */
#include "calculadora.h"

/* Default timeout can be changed using clnt_control() */
static struct timeval TIMEOUT = { 25, 0 };

double *
sumar_1(int a, int b,  CLIENT *clnt)
{
	sumar_1_argument arg;
	static double clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	arg.a = a;
	arg.b = b;
	if (clnt_call (clnt, SUMAR, (xdrproc_t) xdr_sumar_1_argument, (caddr_t) &arg,
		(xdrproc_t) xdr_double, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}

double *
restar_1(int a, int b,  CLIENT *clnt)
{
	restar_1_argument arg;
	static double clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	arg.a = a;
	arg.b = b;
	if (clnt_call (clnt, RESTAR, (xdrproc_t) xdr_restar_1_argument, (caddr_t) &arg,
		(xdrproc_t) xdr_double, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}

double *
multiplicar_1(int a, int b,  CLIENT *clnt)
{
	multiplicar_1_argument arg;
	static double clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	arg.a = a;
	arg.b = b;
	if (clnt_call (clnt, MULTIPLICAR, (xdrproc_t) xdr_multiplicar_1_argument, (caddr_t) &arg,
		(xdrproc_t) xdr_double, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}

double *
dividir_1(int a, int b,  CLIENT *clnt)
{
	dividir_1_argument arg;
	static double clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	arg.a = a;
	arg.b = b;
	if (clnt_call (clnt, DIVIDIR, (xdrproc_t) xdr_dividir_1_argument, (caddr_t) &arg,
		(xdrproc_t) xdr_double, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}

t_vector *
sumavectores_1(t_vector vec1, t_vector vec2,  CLIENT *clnt)
{
	sumavectores_1_argument arg;
	static t_vector clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	arg.vec1 = vec1;
	arg.vec2 = vec2;
	if (clnt_call (clnt, SUMAVECTORES, (xdrproc_t) xdr_sumavectores_1_argument, (caddr_t) &arg,
		(xdrproc_t) xdr_t_vector, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}

t_vector *
restavectores_1(t_vector vec1, t_vector vec2,  CLIENT *clnt)
{
	restavectores_1_argument arg;
	static t_vector clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	arg.vec1 = vec1;
	arg.vec2 = vec2;
	if (clnt_call (clnt, RESTAVECTORES, (xdrproc_t) xdr_restavectores_1_argument, (caddr_t) &arg,
		(xdrproc_t) xdr_t_vector, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}

t_vector *
productovectores_1(t_vector vec1, t_vector vec2,  CLIENT *clnt)
{
	productovectores_1_argument arg;
	static t_vector clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	arg.vec1 = vec1;
	arg.vec2 = vec2;
	if (clnt_call (clnt, PRODUCTOVECTORES, (xdrproc_t) xdr_productovectores_1_argument, (caddr_t) &arg,
		(xdrproc_t) xdr_t_vector, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}
