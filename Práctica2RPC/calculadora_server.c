/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "calculadora.h"
double *
sumar_1_svc(int arg1, int arg2,  struct svc_req *rqstp)
{
	static double  result;
	result = arg1 + arg2;
	return &result;
}

double *
restar_1_svc(int arg1, int arg2,  struct svc_req *rqstp)
{
	static double  result;
	result = arg1 - arg2;
	return &result;
}

double *
multiplicar_1_svc(int arg1, int arg2,  struct svc_req *rqstp)
{
	static double  result;
	result = arg1 * arg2;
	return &result;
}

double *
dividir_1_svc(int arg1, int arg2,  struct svc_req *rqstp)
{
	static double  result;
	result = arg1 / arg2;
	return &result;
}

t_vector *
sumavectores_1_svc(t_vector vec1, t_vector vec2,  struct svc_req *rqstp)
{
	static t_vector  result;
	result.t_vector_len = vec1.t_vector_len;
	result.t_vector_val = (float*) malloc(vec1.t_vector_len * sizeof(float));

	for(int i = 0;i<vec1.t_vector_len;i++){
		result.t_vector_val[i] = vec1.t_vector_val[i] + vec2.t_vector_val[i];
	}

	return &result;
}

t_vector *
restavectores_1_svc(t_vector vec1, t_vector vec2,  struct svc_req *rqstp)
{
	static t_vector  result;
	
	result.t_vector_len = vec1.t_vector_len;
	result.t_vector_val = (float*) malloc(vec1.t_vector_len * sizeof(float));

	for(int i = 0;i<vec1.t_vector_len;i++){
		result.t_vector_val[i] = vec1.t_vector_val[i] - vec2.t_vector_val[i];
	}

	return &result;
}

t_vector *
productovectores_1_svc(t_vector vec1, t_vector vec2,  struct svc_req *rqstp)
{
	static t_vector  result;
	result.t_vector_len = vec1.t_vector_len;
	result.t_vector_val = (float*) malloc(vec1.t_vector_len * sizeof(float));

	for(int i = 0;i<vec1.t_vector_len;i++){
		result.t_vector_val[i] = vec1.t_vector_val[i] * vec2.t_vector_val[i];
	}

	return &result;
}
