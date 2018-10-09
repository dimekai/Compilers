%{
  #include "hoc.h"
  #include <math.h>
  #include <string.h>
  #define MSDOS

  int yylex();
  void yyerror(char * s);
  void warning(char * s, char * t);
  void execerror(char * s, char * t);
  void fpecatch();
%}

%union{
  double num;
  Vector * val;
  Symbol * sym;
}

/* Declaración de YACC*/
%token <num> NUMBER
%token <sym> VAR INDEF
%type <val> vector
%type <val> expr asgn
%type <num> number

%right '='
%left '+' '-'
%left '*' '/'
%left UNARYMINUS
%right '^'

/*Seccion de Reglas Gramaticales y Acciones*/
%%
  list:
    | list '\n'
    | list asgn '\n'
    | list expr '\n' { imprimeVector($2); }
    | list number '\n' { printf("\t%.8g\n", $2); }
    | list error '\n' { yyerrork; }
    ;

  asgn: VAR '=' expr { $$ = $1->u.val = $3;
                       $1->type = VAR; }
    ;

  expr: vector { $$ = $1; }
    | VAR { if( $1->type == INDEF )
              execerror("Variable no definida", $1->name);
            $$ = $1->u.val;
          }
    | asgn
    | expr '+' expr { $$ = sumaVector ( $1, $3 ); }
    | expr '-' expr { $$ = restaVector( $1, $3 ); }
    | NUMBER '*' expr { $$ = escalarVector( $1, $3 ); }
    | exp '*' NUMBER { $$ = escalarVector( $3, $1 ); }
    | expr '#' expr { $$ = productoCruz( $1, $3 ); }
    ;

  number: NUMBER
    | expr ':' expr { $$ = productoPunto( $1, $3 ); }
    | '|' expr '|' { $$ = magnitudVector( $2 ); }
    ;

  vector: '[' NUMBER NUMBER NUMBER ']' { $$ = creaVector(3):
                                         $$->vec[0] = $2;
                                         $$->vec[1] = $3;
                                         $$->vec[2] = $4;
                                       }
    ;
%%
