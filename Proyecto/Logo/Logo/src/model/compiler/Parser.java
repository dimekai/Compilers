//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";

package model.compiler;

//#line 2 "P2.y"
    import java.lang.Math;
    import java.io.*;
    import java.util.StringTokenizer;
    import model.Initialize;
//#line 22 "Parser.java"

public class Parser {
    boolean yydebug;        //do I want debug output?
    int yynerrs;            //number of errors so far
    int yyerrflag;          //was there an error?
    int yychar;             //the current working character
    
    //########## MESSAGES ##########
    //###############################################################
    // method: debug
    //###############################################################
    void debug(String msg){
        if (this.yydebug)
            System.out.println(msg);        
    }
    
    //########## STATE STACK ##########
    final static int YYSTACKSIZE = 500;     //maximum stack size
    int statestk[] = new int[YYSTACKSIZE];  //state stack
    int stateptr;
    int stateptrmax;                        //highest index of stackptr
    int statemax;                           //state when highest index reached
    //###############################################################
    // methods: state stack push,pop,drop,peek
    //###############################################################
    
    final void state_push(int state){
        try {
            this.stateptr++;
            this.statestk[stateptr] = state;
        } catch (ArrayIndexOutOfBoundsException e) {
            int oldsize = this.statestk.length;
            int newsize = oldsize * 2;
            int [] newstack = new int[newsize];
            System.arraycopy(this.statestk, 0, newstack, 0, oldsize);
            this.statestk = newstack;
            this.statestk[this.stateptr] = state;
        }
    }
    
    final int state_pop(){
        return this.statestk[this.stateptr--];
    }
    
    final void state_drop(int counter){
        this.stateptr -= counter;
    }
    
    final int state_peek(int relative){
        return this.statestk[this.stateptr - relative];
    }
    
    //###############################################################
    // method: init_stacks : allocate and prepare stacks
    //###############################################################
    final boolean init_stacks(){
        this.stateptr = -1;
        val_init();
        return true;
    }
    
    //###############################################################
    // method: dump_stacks : show n levels of the stacks
    //###############################################################
    void dump_stacks(int count){
        int i;
        System.out.println("=index==state====value=     s:"+this.stateptr+"  v:"+this.valptr);
        for (i=0;i<count;i++)
            System.out.println(" "+i+"    "+this.statestk[i]+"      "+this.valstk[i]);
        System.out.println("======================");
    }
    
    //########## SEMANTIC VALUES ##########
    //public class ParserVal is defined in ParserVal.java
    String   yytext;        //user variable to return contextual strings
    ParserVal yyval;        //used to return semantic vals from action routines
    ParserVal yylval;       //the 'lval' (result) I got from yylex()
    ParserVal valstk[];
    int valptr;
    
    //###############################################################
    // methods: value stack push,pop,drop,peek.
    //###############################################################
    void val_init(){
        this.valstk = new ParserVal[YYSTACKSIZE];
        this.yyval = new ParserVal();
        this.yylval = new ParserVal();
        this.valptr = -1;
    }
    
    void val_push(ParserVal val){
        if (this.valptr >= this.YYSTACKSIZE)
            return;
        this.valstk[++this.valptr] = val;
    }
    
    ParserVal val_pop(){
        if (this.valptr < 0)
            return new ParserVal();
        return this.valstk[this.valptr--];
    }
    
    void val_drop(int count){
        int ptr = this.valptr - count;
        if (ptr < 0)
            return;
        this.valptr = ptr;
    }
    
    ParserVal val_peek(int relative){
        int ptr = this.valptr - relative;
        if (ptr<0)
            return new ParserVal();
        return this.valstk[ptr];
    }
    
    final ParserVal dup_yyval(ParserVal val){
        ParserVal dup = new ParserVal();
        dup.ival = val.ival;
        dup.dval = val.dval;
        dup.sval = val.sval;
        dup.obj = val.obj;
        return dup;
    }
    //#### end semantic value section ####
    
    public final static short YYERRCODE = 256;
    /*======== STATEMENTS ========*/
    public final static short IF = 257;
    public final static short ELSE = 258;
    public final static short WHILE = 259;
    public final static short FOR = 260;
    /*======== CONDITIONAL OPERATORS ========*/
    public final static short EQ = 261;
    public final static short NEQ = 262;
    public final static short GT = 263;
    public final static short LT = 264;
    public final static short GET = 265;
    public final static short LET = 266;
    public final static short FNCT = 267;
    public final static short NUMBER = 268;
    public final static short VAR = 269;
    /*======== LOGICAL OPERATORS ========*/
    public final static short AND = 270;
    public final static short OR = 271;
    public final static short FUNC = 272;
    public final static short RETURN = 273;
    public final static short PARAMETRO = 274;
    public final static short PROC = 275;
    final static short yylhs[] = {                           -1,
        0,    0,    0,    1,    1,    1,    1,    2,    2,    2,
        2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
        2,    2,    2,    2,    2,    2,    2,    5,    5,    5,
        6,    3,    3,    3,    3,    3,    3,    3,   15,   14,
       12,    4,   13,    8,    7,    9,   10,   11,   11,   11,
    };
    final static short yylen[] = {                            2,
        0,    2,    3,    2,    1,    3,    2,    1,    2,    1,
        3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
        3,    3,    3,    2,    2,    1,    4,    0,    1,    3,
        0,   14,   11,   10,   16,    8,    8,    5,    1,    1,
        1,    1,    0,    0,    1,    1,    1,    0,    1,    3,
    };
    final static short yydefred[] = {                         1,
        0,   45,   46,   47,   39,   10,    0,   41,    0,   26,
       40,    0,    0,    2,    0,    0,    0,    5,    0,    0,
        0,    0,    0,    0,    0,    0,   25,    0,    0,    0,
        3,    0,    7,    0,    0,    0,    0,    0,    0,    0,
        0,    0,    0,    0,    4,    0,    0,    0,    0,   42,
        0,    0,    0,    0,   15,    6,    0,    0,    0,    0,
        0,    0,    0,   23,    0,    0,    0,    0,    0,    0,
        0,    0,    0,    0,    0,    0,   27,    0,    0,    0,
        0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
        0,    0,   38,    0,    0,    0,    0,    0,    0,    0,
        0,    0,    0,    0,    0,    0,   36,   37,    0,   44,
        0,    0,   44,   34,    0,    0,   33,    0,    0,    0,
        0,    0,   32,    0,   44,   35,
    };
    final static short yydgoto[] = {                          1,
       16,   17,   18,   19,   69,  113,   20,   79,   21,   22,
       73,   23,  102,   24,   25,
    };
    final static short yysindex[] = {                         0,
        9,    0,    0,    0,    0,    0,  -49,    0,  -13,    0,
        0,  -13,  -13,    0,  -13,   28,   75,    0,  -24,   -9,
       -4,   10, -255, -255,  -48,  -13,    0,  108, -242,   54,
        0,   86,    0,  -13,  -13,  -13,  -13,  -13,  -13,  -13,
      -13,  -13,  -13,  -13,    0,  -13,  -13,  -13,  -13,    0,
       17,   19,  -13,   97,    0,    0,  217,  152,  190, -194,
     -225, -242, -190,    0,  108,  108,  -57,   97,    3,   97,
       97,   97,   45,   49,   57,  -35,    0,  -13,   70,   71,
      -13,   62,   14,   15,   73,   97,   18,   20,   97,  -13,
      -32,  -32,    0,  -32,  -32,   97,  -32,  -32,  -32,  -32,
       76,   23,   24,   27,   29,  -13,    0,    0, -114,    0,
       45,   32,    0,    0,  105,  -32,    0,   33,  -32,  -32,
       40,  -32,    0,   52,    0,    0,
    };
    final static short yyrindex[] = {                         0,
        0,    0,    0,    0,    0,    0,   43,    0,    0,    0,
        0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
        0,    0,    0,    0,    0,    0,    0,  -19,  119,    0,
        0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
        0,    0,    0,    0,    0,   38,    0,    0,  -42,    0,
        0,    0,  -33,  -26,    0,    0,  -38,  158,  151,  131,
      138,  125,   65,    0,   60,  230,   21,    7,    0,  116,
      116,   -7,  100,    0,    0,    0,    0,    0,    0,    0,
        0,    0,    0,    0,    0,   34,    0,    0,   12,    0,
        0,    0,    0,    0,    0,  100,   61,   61,   63,   63,
        0,    0,    0,    0,    0,   89,    0,    0,  -10,    0,
      116,    0,    0,    0,    0,    0,    0,    0,   63,    0,
        0,   63,    0,    0,    0,    0,
    };
    final static short yygindex[] = {                         0,
       31,  395,   -6,   46,  118,    0,    0,  353,    0,    0,
       79,    0,   91,    0,    0,
    };
    final static int YYTABLESIZE=517;
    static short yytable[];
    static { yytable();}
    static void yytable(){
        yytable = new short[]{                                   31,
           13,   48,   16,   16,   16,   16,   16,   15,   78,   33,
           28,   26,   12,   50,   11,   46,   48,   11,   14,   13,
           16,    9,   31,    9,    9,    9,   15,   40,   41,   31,
           47,   12,   11,   49,   31,   48,   49,   31,   37,    9,
           39,   13,   53,   77,   40,   41,   78,   29,   15,   49,
           29,   49,   50,   12,   16,   50,   74,   85,   75,   28,
           13,   12,   12,   12,   12,   12,   11,   15,   51,   52,
           50,   39,   12,    9,   30,   40,   41,   30,   28,   12,
           41,   28,   42,    8,    8,    8,    8,    8,   81,   83,
           33,   33,   33,   33,   55,   44,   42,   84,   43,   29,
           13,    8,   13,   13,   13,   22,   22,   22,   22,   22,
           87,   88,   33,   12,   31,   33,   44,   42,   13,   43,
           90,   97,   98,   22,   99,  100,   30,   44,   42,   48,
           43,   93,   48,   45,  106,    8,   91,   92,   44,   42,
           94,   43,   95,  112,   56,  118,  119,  107,  108,   44,
          122,  109,   13,  110,  116,  120,   44,   22,   44,   24,
           24,   24,   24,   24,  123,   19,   19,   19,   19,   19,
           76,   18,   18,   18,   18,   18,  125,   24,   21,   21,
           21,   21,   21,   19,  111,   43,    0,   44,  103,   18,
            0,   20,   20,   20,   20,   20,   21,    0,   17,   17,
           17,   17,   17,   34,   35,   36,   37,   38,   39,   20,
            0,   24,   40,   41,    0,    0,   17,   19,    0,    0,
            0,    0,   16,   18,    2,    0,    3,    4,    0,    0,
           21,    0,    0,    0,    5,    6,    7,    0,    0,    8,
            9,   10,   11,   20,    0,    0,   31,    0,   31,   31,
           17,    0,    0,    0,    6,    7,   31,   31,   31,    9,
           10,   31,   31,   31,   31,    2,    0,    3,    4,    0,
           14,    0,   14,   14,   14,    5,    6,    7,    0,    0,
            8,    9,   10,   11,    2,    0,    3,    4,   14,    0,
            0,    0,    0,    0,    5,    6,    7,    0,    0,    8,
            9,   10,   11,    8,    8,    8,    8,    8,    8,    0,
            0,    0,    8,    8,   34,   35,   36,   37,   38,   39,
            0,    0,   14,   40,   41,   22,   22,   22,   22,   22,
           22,    0,    0,    0,   22,   34,   35,   36,   37,   38,
           39,    0,    0,    0,   40,   41,   34,   35,   36,   37,
           38,   39,    0,    0,    0,   40,   41,   34,   35,   36,
           37,   38,   39,    0,    0,    0,   40,   41,   34,   35,
           36,   37,   38,   39,    0,    0,    0,   40,   41,   24,
           24,   24,   24,   24,   24,   19,   19,   19,   19,   19,
           19,   18,   18,   18,   18,   18,    0,    0,   21,   21,
           21,    0,   21,   27,    0,    0,   28,   29,    0,   30,
           32,   20,   20,   20,   36,   37,   38,   39,   17,   17,
           54,   40,   41,   80,    0,   82,    0,    0,   57,   58,
           59,   60,   61,   62,   63,   64,   65,   66,   67,    0,
           68,   70,   71,   72,    0,    0,    0,   68,  101,    0,
            0,  104,  105,   37,   38,   39,    0,    0,    0,   40,
           41,    0,  114,  115,    0,  117,    0,    0,    0,    0,
            0,  121,   86,    0,  124,   89,    0,  126,   35,   36,
           37,   38,   39,    0,   96,    0,   40,   41,    0,    0,
            0,   32,   32,   32,   32,    0,    0,    0,    0,    0,
           72,    0,    0,    0,    0,    0,    0,    0,    0,    0,
            0,    0,    0,   32,    0,    0,   32,
        };
    }
    static short yycheck[];
    static { yycheck(); }
    static void yycheck() {
        yycheck = new short[] {                                  10,
           33,   44,   41,   42,   43,   44,   45,   40,   44,   16,
           44,   61,   45,  269,   41,   40,   59,   44,   10,   33,
           59,   41,   33,   43,   44,   45,   40,  270,  271,   40,
           40,   45,   59,   41,   45,   40,   44,   10,  264,   59,
          266,   33,   91,   41,  270,  271,   44,   41,   40,   40,
           44,   59,   41,   45,   93,   44,   40,   93,   40,   93,
           33,   41,   42,   43,   44,   45,   93,   40,   23,   24,
           59,  266,   45,   93,   41,  270,  271,   44,   41,   59,
          271,   44,   40,   41,   42,   43,   44,   45,   44,   41,
           97,   98,   99,  100,   41,   42,   43,   41,   45,   93,
           41,   59,   43,   44,   45,   41,   42,   43,   44,   45,
           41,   41,  119,   93,  125,  122,   42,   43,   59,   45,
           59,   91,   92,   59,   94,   95,   93,   42,   43,   41,
           45,   59,   44,   59,   59,   93,  123,  123,   42,   43,
          123,   45,  123,  258,   59,   41,  116,  125,  125,   42,
          120,  125,   93,  125,  123,  123,   41,   93,   59,   41,
           42,   43,   44,   45,  125,   41,   42,   43,   44,   45,
           53,   41,   42,   43,   44,   45,  125,   59,   41,   42,
           43,   44,   45,   59,  106,  125,   -1,  125,   98,   59,
           -1,   41,   42,   43,   44,   45,   59,   -1,   41,   42,
           43,   44,   45,  261,  262,  263,  264,  265,  266,   59,
           -1,   93,  270,  271,   -1,   -1,   59,   93,   -1,   -1,
           -1,   -1,  261,   93,  257,   -1,  259,  260,   -1,   -1,
           93,   -1,   -1,   -1,  267,  268,  269,   -1,   -1,  272,
          273,  274,  275,   93,   -1,   -1,  257,   -1,  259,  260,
           93,   -1,   -1,   -1,  268,  269,  267,  268,  269,  273,
          274,  272,  273,  274,  275,  257,   -1,  259,  260,   -1,
           41,   -1,   43,   44,   45,  267,  268,  269,   -1,   -1,
          272,  273,  274,  275,  257,   -1,  259,  260,   59,   -1,
           -1,   -1,   -1,   -1,  267,  268,  269,   -1,   -1,  272,
          273,  274,  275,  261,  262,  263,  264,  265,  266,   -1,
           -1,   -1,  270,  271,  261,  262,  263,  264,  265,  266,
           -1,   -1,   93,  270,  271,  261,  262,  263,  264,  265,
          266,   -1,   -1,   -1,  270,  261,  262,  263,  264,  265,
          266,   -1,   -1,   -1,  270,  271,  261,  262,  263,  264,
          265,  266,   -1,   -1,   -1,  270,  271,  261,  262,  263,
          264,  265,  266,   -1,   -1,   -1,  270,  271,  261,  262,
          263,  264,  265,  266,   -1,   -1,   -1,  270,  271,  261,
          262,  263,  264,  265,  266,  261,  262,  263,  264,  265,
          266,  261,  262,  263,  264,  265,   -1,   -1,  261,  262,
          263,   -1,  265,    9,   -1,   -1,   12,   13,   -1,   15,
           16,  261,  262,  263,  263,  264,  265,  266,  261,  262,
           26,  270,  271,   71,   -1,   73,   -1,   -1,   34,   35,
           36,   37,   38,   39,   40,   41,   42,   43,   44,   -1,
           46,   47,   48,   49,   -1,   -1,   -1,   53,   96,   -1,
           -1,   99,  100,  264,  265,  266,   -1,   -1,   -1,  270,
          271,   -1,  110,  111,   -1,  113,   -1,   -1,   -1,   -1,
           -1,  119,   78,   -1,  122,   81,   -1,  125,  262,  263,
          264,  265,  266,   -1,   90,   -1,  270,  271,   -1,   -1,
           -1,   97,   98,   99,  100,   -1,   -1,   -1,   -1,   -1,
          106,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
           -1,   -1,   -1,  119,   -1,   -1,  122,
        };
    }
    final static short YYFINAL = 1;
    final static short YYMAXTOKEN = 275;
    
    final static String yyname[] = {
        "end-of-file",null,null,null,null,null,null,null,null,null,"'\\n'",null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,"'!'",null,null,null,null,null,null,"'('","')'","'*'","'+'",
        "','","'-'",null,null,null,null,null,null,null,null,null,null,null,null,null,
        "';'",null,"'='",null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,"IF","ELSE","WHILE","FOR","EQ",
        "NEQ","GT","LT","GET","LET","FNCT","NUMBER","VAR","AND","OR","FUNC",
        "RETURN","PARAMETRO","PROC",
    };
    /* AQUI VAN LAS REGLAS */
    final static String yyrule[] = {
        "$accept : list",
        "list :",
        "list : list '\\n'",
        "list : list linea '\\n'",
        
        "linea : exp ';'",
        "linea : stmt",
        "linea : linea exp ';'",
        "linea : linea stmt",
        
        "exp : VAR",
        "exp : '-' exp",
        "exp : NUMBER",
        "exp : VAR '=' exp",
        "exp : exp '*' exp",
        "exp : exp '+' exp",
        "exp : exp '-' exp",
        "exp : '(' exp ')'",
        "exp : exp EQ exp",
        "exp : exp NEQ exp",
        "exp : exp LET exp",
        "exp : exp LT exp",
        "exp : exp GET exp",
        "exp : exp GT exp",
        "exp : exp OR exp",
        "exp : exp AND exp",
        "exp : '!' exp",
        "exp : RETURN exp",
        "exp : PARAMETRO",
        "exp : nombreProc '(' arglist ')'",
        
        
    };
}
