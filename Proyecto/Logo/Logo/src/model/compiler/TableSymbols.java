package model.compiler;

import java.util.ArrayList;
import java.util.Vector;

public class TableSymbols {
    ArrayList<Symbol> Symbols;

    public TableSymbols() {
        this.Symbols = new ArrayList<>();
    }

    public Object found(String name) {
        for (int i = 0; i < this.Symbols.size(); i++)
            if (name.equals(this.Symbols.get(i).getName()))
                return this.Symbols.get(i).getObject();

        return null;
    }

    public boolean insert(String name, Object object) {
        for (int i = 0; i < this.Symbols.size(); i++)
            if (name.equals(this.Symbols.get(i).getName())) {
                this.Symbols.get(i).setObject(object);
                return true;
            }
        this.Symbols.add(new Symbol(name, object));
        return false;
    }
    
    public void print(){
        for(int i = 0; i < this.Symbols.size(); i++)
            System.out.println(this.Symbols.get(i).getName() 
                             + this.Symbols.get(i).getObject().toString());
    }
}