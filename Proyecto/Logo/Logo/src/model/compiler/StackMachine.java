package model.compiler;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import model.Initialize;
import model.Line;

public class StackMachine {
    
    private int pc;     /* Contador de programa */
    private ArrayList memory;
    private Stack stack;
    private Stack<Marco> stackMarcos;
    private TableSymbols table;
    private Initialize currentConfig;
    private boolean _return = false;
    private boolean STOP = false;
    
    public StackMachine(TableSymbols table){
        this.memory = new ArrayList<>();
        this.stack = new Stack<>();
        this.stackMarcos = new Stack<>();
        this.currentConfig = new Initialize();
        this.pc = 0;
        this.table = table;
    }
    
    public int numberOfElements(){
        return this.memory.size() + 1;
    }
    
    /*
    ||=======================================||
    ||  FUNCTIONS ARE WRITTEN ON THE MEMORY  ||
    ||=======================================||
    */
    public int aggregateOperation(String name){
        int position = this.memory.size();
        try {
            this.memory.add(this.getClass().getDeclaredMethod(name, null));
            return position;
        } catch (Exception e) {
            System.out.println("Error to add operation " + name + "\n");
            System.out.println(e.getCause());
        }
        return -1;
    }
    
    public int aggregate(Object object){
        int position = this.memory.size();
        this.memory.add(object);
        return position;
    }
    
    public void aggregate(Object object, int position){
        this.memory.remove(position);
        this.memory.add(position,object);
    }
    
    public int aggregateOperationIn(String name, int position){
        try {
            this.memory.add(position, this.getClass().getDeclaredMethod(name, null));
        } catch (Exception e) {
            System.out.println("Error to add operation " + name 
                             + "in position "+ position +".\n");
            System.out.println(e.getCause());
        }
        
        return position;
    }
    
    /*
    ||=====================================================||
    ||  MACHINE EXECUTES FOLLOWING FUNCTIONS ON THE STACK  ||
    ||=====================================================||
    */
    private void add(){
        Object matrixA = this.stack.pop();
        Object matrixB = this.stack.pop();
        
        this.stack.push( (double)matrixA + (double)matrixB );
    }
    
    private void sub(){
        Object matrixA = this.stack.pop();
        Object matrixB = this.stack.pop();
        
        this.stack.push( (double)matrixA - (double)matrixB );
    }
    
    private void mult(){
        Object matrixA = this.stack.pop();
        Object matrixB = this.stack.pop();
        
        this.stack.push( (double)matrixA * (double)matrixB );
    }
    
    private void negative(){
        Object matrixA = this.stack.pop();
        System.out.println(matrixA);
        this.stack.push(-(double)matrixA);
    }
    
    private void constPush(){
        this.stack.push(this.memory.get(++this.pc));
    }
    
    private void varPush(){
        this.stack.push(this.memory.get(++this.pc));
    }
    
    private void varPush_eval(){
        this.stack.push(this.table.found((String)this.memory.get(++this.pc)));
    }
    
    public void asign(){
        String variable = (String)this.stack.pop();
        Object object = this.stack.pop();
        this.table.insert(variable, object);
    }
    
    /*
    ||=========================||
    ||  CONDITIONAL OPERATORS  ||
    ||=========================||
    */
    private void EQ(){
        Object B = this.stack.pop();
        Object A = this.stack.pop();
       this.stack.push( (double)A == (double)B );
    }
    
    private void NEQ(){
        Object B = this.stack.pop();
        Object A = this.stack.pop();
        this.stack.push( (double)A != (double)B );
    }
    
    private void LT(){
        Object B = this.stack.pop();
        Object A = this.stack.pop();
        this.stack.push( (double)A < (double)B );
    }
    
    private void GT(){
        Object B = this.stack.pop();
        Object A = this.stack.pop();
        this.stack.push( (double)A > (double)B );
    }
    
    private void LET(){
        Object B = this.stack.pop();
        Object A = this.stack.pop();
        this.stack.push( (double)A <= (double)B );
    }
    
    private void GET(){
        Object B = this.stack.pop();
        Object A = this.stack.pop();
        this.stack.push( (double)A >= (double)B );
    }
    
    /*
    ||=========================||
    ||    LOGICAL OPERATORS    ||
    ||=========================||
    */
    private void negate(){
        Object A = this.stack.pop();
        this.stack.push(! (boolean)A);
    }
    
    private void AND(){
        Object B = this.stack.pop();
        Object A = this.stack.pop();
        this.stack.push( (boolean)A && (boolean)B );
    }
    
    private void OR(){
        Object B = this.stack.pop();
        Object A = this.stack.pop();
        this.stack.push( (boolean)A || (boolean)B );
    }
    
    /*
    ||==================================||
    ||   METHODS WHICH EXECUTE CODING   ||
    ||==================================||
    */
    public void printMemory(){
        for(int i = 0; i < this.memory.size(); i++)
            System.out.println(i + ": " + this.memory.get(i));
    }
    
    public void executeInstruction(int index){
        try {
            Object objectRead = this.memory.get(index);
            if (objectRead instanceof Method) {
                Method method = (Method)objectRead;
                method.invoke(this, (Object) null);
            }
            if (objectRead instanceof Function) {
                ArrayList parameters = new ArrayList<>();
                Function function = (Function)objectRead;
                this.pc++;
                /*Se usa null como un delimitador
                Se agregan los parametros al marco*/
                while(this.memory.get(this.pc) != null){
                    if (this.memory.get(this.pc) instanceof String){
                        if (((String)(this.memory.get(this.pc))).equals("Limite")) {
                            Object parameter = this.stack.pop();
                            parameters.add(parameter);
                            this.pc++;
                        }
                    }else{
                        executeInstruction(this.pc);
                    }
                }
                function.execute(this.currentConfig, parameters);
            }
            this.pc++;
        } catch (Exception e) {
            System.out.println("Error to execute the instruction in "+ index);
            System.out.println(e.getCause());
        }
    }
    
    public void execute(){
        /*printMemory();*/
        this.STOP = false;
        while(this.pc < this.memory.size())
            executeInstruction(this.pc);
    
    }
    
    public boolean executeNext(){
        /*printMemory();*/
        if (this.pc < this.memory.size()) {
            executeInstruction(pc);
            return true;
        }
        return false;
    }
    
    public void execute(int index){
        /*Ejecuta hasta encontrar un STOP*/
        this.pc = index;
        while(!this.STOP && !this._return)
            executeInstruction(pc);
        this.STOP = false;
    }
    
    public void executeFunction(int index){
        this.pc = index;
        while(!this._return && this.memory.get(this.pc) != null)
            executeInstruction(this.pc);
        
        this._return = false;
        this.pc = this.stackMarcos.lastElement().getRetorno();
        this.stackMarcos.removeElement(this.stackMarcos.lastElement());
    }
    
    public Initialize getConfiguration(){
        return this.currentConfig;
    }    
    
    /*
    ||==========================||
    ||        STATEMENTS        ||
    ||==========================||
    */
    private void stop(){
        this.STOP = true;
    }
    
    private void _return(){
        this._return = true;
    }
    
    private void nop(){}
    
    private void _while(){
        int condition = this.pc;
        boolean _continue = true;
        while(_continue && !this._return){
            execute(condition + 3);
            if ((boolean)this.stack.pop()) { 
                //Se lee el resultado de la condicion de la pila
                // y se ejecuta el cuerpo
                execute( (int)this.memory.get(condition + 1));
            }else{
                this.pc = (int)this.memory.get(condition + 2);
                _continue = false;
            }
        }
    }
    
    private void if_then_else(){
        int condition = this.pc;
        execute(condition + 4);
        boolean res = true;
        try {
            res = (boolean)this.stack.pop();
        } catch (Exception e) {
            System.out.println("Error to pop on stack.");
            System.out.println(e.getCause());
        }
        if (res) //Se lee el resultado de la condicion de la pila
            execute( (int)this.memory.get(condition + 1) ); //Ejecuta el cuerpo
        else
            execute( (int)this.memory.get(condition + 2) );
        
        this.pc = (int)this.memory.get(condition + 3) - 1;
        //El -1 es para corregir el aumento del pc al final de cada instrucción
    }
    
    private void _for(){
        int condition = this.pc;
        execute(condition + 5); // Se ejecuta la primera parte
        boolean _continue = true;
        
        while(_continue && !this._return){
            execute( (int)this.memory.get(condition + 1) ); //Evaluamos condicion
            if ( (boolean)this.stack.pop() ) {      //Lee el resultado de la condicion de la pila
                execute( (int)this.memory.get(condition + 3) ); //Ejecuta el cuerpo
                execute( (int)this.memory.get(condition + 2) );//Ejecuta la ultima parte del for
            }else{
                this.pc = (int)this.memory.get(condition + 4);
                _continue = false;
            }
        }
    }
    
    private void statement(){
        this.table.insert((String)this.memory.get(++this.pc), ++this.pc); //Apuntamos a la primer instrucción de la función
        int invoked = 0;
        /*Se lleva al pc hasta la siguiente 
        instruccion después de la declaracion */
        while(this.memory.get(this.pc) != null || invoked != 0){
            if (this.memory.get(this.pc) instanceof Method)
                if (((Method)this.memory.get(this.pc)).getName().endsWith("invocar")) 
                    invoked++;
            if (this.memory.get(this.pc) instanceof Function)
                invoked++;
            if (this.memory.get(this.pc) == null)
                invoked--;            
        }
    }
    
    public void invoke(){
        Marco marco = new Marco();
        String name = (String)this.memory.get(++this.pc);
        marco.setName(name);
        this.pc++;
        
        /*Se utiliza el delimitador null para posteriormente
        poder agregar los parametros al marco */
        while (this.memory.get(this.pc) != null) {            
            if (this.memory.get(this.pc) instanceof String) {
                if (((String)(this.memory.get(this.pc))).equals("Limite")) {
                    Object parameter = this.stack.pop();
                    marco.addParameter(parameter);
                    this.pc++;
                }
            }else{
                executeInstruction(this.pc);
            }
        }
        marco.setRetorno(pc);
        this.stackMarcos.add(marco);
        executeFunction((int)table.found(name)); /*Aqui es donde vamos */
    }
    
    private void push_Parameter(){
        int id = (int)this.memory.get(++this.pc);
        this.stack.push(this.stackMarcos.lastElement().getParameter(id - 1));
    }
    
    /*
    ||==================================||
    ||   PARTICULAR FUNCTIONS OF LOGO   ||
    ||==================================||
    */
    
    public static class turn implements Function{

        @Override
        public void execute(Object A, ArrayList parameters) {
            Initialize config = (Initialize)A;
            int angle = ( config.getAngle() + (int)(double)parameters.get(0))%RADIAN;
            config.setAngle(angle);
        }        
    }
    
    public static class goForward implements Function{

        @Override
        public void execute(Object A, ArrayList parameters) {
            Initialize config = (Initialize)A;
            double Xi = config.getX();
            double Yi = config.getY();
            int angle = config.getAngle();
            
            double Xf = Xi + Math.cos(Math.toRadians(angle))*(double)parameters.get(0);
            double Yf = Yi + Math.sin(Math.toRadians(angle))*(double)parameters.get(0);
            
            config.setPosition(Xf, Yf);
            Line line = new Line((int)Xi, (int)Yi, 
                                 (int)Xf, (int)Yf, config.getColor());
            config.addLine(line);
        }        
    }
    
    public static class changeColor implements Function{

        @Override
        public void execute(Object A, ArrayList parameters) {
            Initialize config = (Initialize)A;
            config.setColor(new Color((int)(double)parameters.get(0)%RGB, 
                                      (int)(double)parameters.get(1)%RGB, 
                                      (int)(double)parameters.get(2)%RGB)
                            );
        }    
    }
}
