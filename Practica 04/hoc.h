#include "vector_cal.h"
#include <string.h>

typedef struct Symbol{    /*Entrada de la tabla de símbolos*/
    char * name;
    short type;           /* VAR , BLTIN , UNDEF */

    union{
      Vector * val;       /* Si es VAR */
      double num;
    }u;

    struct Symbol * next; /* Es para ligarse a otro */
}Symbol;

Symbol * install(char * s, int t, Vector * d);
Symbol * lookup(char * s);
