package interfaces;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

import model.Initialize;
import model.Line;

public class DrawingPanel extends JPanel{
    Initialize configuration;
    
    public DrawingPanel(){
        this.configuration = new Initialize();
    }
    
    public void setConfiguration(Initialize configuration){
        this.configuration = configuration;
    }
    
    //Este será el puntero con el que se dibujará
    public Polygon triangular_pointer(double X, double Y, int angle){
        int Xs[] = new int[3];
        int Ys[] = new int[3];
        
        Xs[0] = (Properties.DRAWING_PANEL_WIDTH  / 2) + (int) X;
        Ys[0] = (Properties.DRAWING_PANEL_HEIGHT / 2) - (int) Y;
        Xs[1] = (Properties.DRAWING_PANEL_WIDTH  / 2) + (int)(X - 10*Math.cos(Math.toRadians(angle + 20)));
        Ys[1] = (Properties.DRAWING_PANEL_HEIGHT / 2) - (int)(Y - + 10*Math.sin(Math.toRadians(angle + 20)));
        Xs[2] = (Properties.DRAWING_PANEL_WIDTH  / 2) + (int)(X - 10*Math.cos(Math.toRadians(angle - 20)));
        Ys[2] = (Properties.DRAWING_PANEL_HEIGHT / 2) - (int)(Y - + 10*Math.sin(Math.toRadians(angle - 20)));
        
        return (new Polygon(Xs, Ys, 3));
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        ArrayList<Line> lines = configuration.getLines();
        for (int i = 0; i < lines.size(); i++) {
            int Xi = (Properties.DRAWING_PANEL_WIDTH  / 2) + lines.get(i).getXi();
            int Yi = (Properties.DRAWING_PANEL_HEIGHT / 2) - lines.get(i).getYi();;
            int Xf = (Properties.DRAWING_PANEL_WIDTH  / 2) + lines.get(i).getXf();;
            int Yf = (Properties.DRAWING_PANEL_HEIGHT / 2) - lines.get(i).getYf();;
            g.setColor(lines.get(i).getColor());
            g.drawLine(Xi, Yi, Xf, Yf);
        }
        g.setColor(Color.BLUE);
        Polygon Triangle = triangular_pointer(configuration.getX(), 
                                    configuration.getY(), 
                                    configuration.getAngle()
                                   );
        g.drawPolygon(Triangle);
        g.fillPolygon(Triangle);
    }
}
