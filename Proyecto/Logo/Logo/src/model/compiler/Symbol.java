package model.compiler;

public class Symbol {

    private String name;
    private Object object;

    public Symbol(String name, Object object) {
        this.name = name;
        this.object = object;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return this.object;
    }

}
