package model.compiler;

import java.util.ArrayList;

public interface Function {
    /* Constants */
    public final int RADIAN = 360;
    public final int RGB = 256;
    public abstract void execute(Object A, ArrayList parameters);
}
