package model;

import java.awt.Color;

public class Line {

    private int Xi;
    private int Yi;
    private int Xf;
    private int Yf;
    private Color color;

    /* Constructor */
    public Line(int Xi, int Yi, int Xf, int Yf, Color color) {
        this.Xi = Xi;
        this.Yi = Yi;
        this.Xf = Xf;
        this.Yf = Yf;
        this.color = color;
    }

    /* Getters and Setters */
    public int getXi() {
        return this.Xi;
    }

    public void setXi(int Xi) {
        this.Xi = Xi;
    }

    public int getYi() {
        return this.Yi;
    }

    public void setYi(int Yi) {
        this.Yi = Yi;
    }

    public int getXf() {
        return this.Xf;
    }

    public void setXf(int Xf) {
        this.Xf = Xf;
    }

    public int getYf() {
        return this.Yf;
    }

    public void setYf(int Yf) {
        this.Yf = Yf;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "(" + Xi + "," + Yi + "," + Xf + "," + Yf + ")";
    }
}
