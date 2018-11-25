package model.compiler;

import java.util.ArrayList;

public class Marco {
   
    private ArrayList parameters;
    private String name;
    private int retorno;
    
    public Marco(){
        this.parameters = new ArrayList<>();
        this.name = null;
        this.retorno = 0;
    }
    
    public void addParameter(Object parameter){
        this.parameters.add(parameter);
    }
    
    public Object getParameter(int id){
        return this.parameters.get(id);
    }
    
    public void setParameters(ArrayList parameters){
        this.parameters = parameters;
    }
    
    public int getRetorno(){
        return this.retorno;
    }
    
    public void setRetorno(int retorno){
        this.retorno = retorno;
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
}
