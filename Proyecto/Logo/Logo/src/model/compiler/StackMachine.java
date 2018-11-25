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
    private boolean returning = false;
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
        Object matrixA = stack.pop();
        Object matrixB = stack.pop();
        
        stack.push( (double)matrixA + (double)matrixB );
    }
    
    private void sub(){
        Object matrixA = stack.pop();
        Object matrixB = stack.pop();
        
        stack.push( (double)matrixA - (double)matrixB );
    }
    
    private void mult(){
        Object matrixA = stack.pop();
        Object matrixB = stack.pop();
        
        stack.push( (double)matrixA * (double)matrixB );
    }
    
    private void negative(){
        Object matrixA = stack.pop();
        System.out.println(matrixA);
        stack.push(-(double)matrixA);
    }
    
    private void constPush(){
        stack.push(memory.get(++pc));
    }
    
    private void varPush(){
        stack.push(memory.get(++pc));
    }
    
    private void varPush_eval(){
        stack.push(table.found((String)memory.get(++pc)));
    }
    
    public void asign(){
        String variable = (String)stack.pop();
        Object object = stack.pop();
        table.insert(variable, object);
    }
}
