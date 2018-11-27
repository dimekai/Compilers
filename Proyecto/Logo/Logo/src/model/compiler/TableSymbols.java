package model.compiler;

import java.util.ArrayList;

public class TableSymbols {
    ArrayList<Symbol> Symbols;

    public TableSymbols() {
        this.Symbols = new ArrayList<Symbol>();
    }

    public Object found(String name) {
        for (int i = 0; i < this.Symbols.size(); i++)
            if (name.equals(this.Symbols.get(i).getName()))
                return this.Symbols.get(i).getObject();

        return null;
    }

    public boolean insert(String name, Object object) {
        Symbol symb = new Symbol(name, name);
        for (int i = 0; i < this.Symbols.size(); i++)
            if (name.equals(this.Symbols.get(i).getName())) {
                this.Symbols.get(i).setObject(object);
                return true;
            }
        this.Symbols.add(symb);
        return false;
    }
    
    public void print(){
        for(int i = 0; i < this.Symbols.size(); i++)
            System.out.println(this.Symbols.get(i).getName() 
                             + this.Symbols.get(i).getObject().toString());
    }
}