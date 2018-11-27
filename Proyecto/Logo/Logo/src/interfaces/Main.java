package interfaces;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFrame;
import model.compiler.Parser;

public class Main extends JFrame{
    
    DrawingPanel drawing_panel;
    JTextArea commands_area;
    JScrollPane scroll_command;
    JButton execute, debug, next;
    Parser parser;
    boolean mode_debug;
    
    final Color BACKGROUND = Color.BLACK;
    final String path_execute  = System.getProperty("user.dir") + "/execute.png";
    final String path_debug    = System.getProperty("user.dir") + "/debug.png";
    final String path_next     = System.getProperty("user.dir") + "/next.png";
    
    public Main(){
        super("Compiler - Logo");
        mode_debug = false;
        parser = new Parser();
        parser.insertarInstrucciones();
        
        commands_area  = new JTextArea(20,20);
        scroll_command = new JScrollPane(commands_area);
        scroll_command.setBounds(10, 10, 250, 550);
        add(scroll_command);
        
        drawing_panel = new DrawingPanel();
        drawing_panel.setBounds(270, 10,
                                        Properties.DRAWING_PANEL_WIDTH,
                                        Properties.DRAWING_PANEL_HEIGHT
                                    );
        drawing_panel.setBackground(BACKGROUND);
        add(drawing_panel);
        
        /* ==== BUTTONS ==== */
        execute = new JButton(new ImageIcon(path_execute));
        execute.setBounds(10,570,50,40);
        execute.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                parser.limpiar();
                if (parser.compilar(commands_area.getText()))
                    drawing_panel.setConfiguration(parser.ejecutar());
                else{
                    parser = new Parser();
                    parser.insertarInstrucciones();
                    drawing_panel.setConfiguration(parser.getConfiguracion());
                }
                drawing_panel.repaint();
            }
        });
        add(execute);
        
        debug = new JButton(new ImageIcon(path_debug));
        debug.setBounds(150,570,50,40); 
        debug.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                parser.limpiar();
                if(!mode_debug){
                    if(parser.compilar(commands_area.getText())){
                        drawing_panel.setConfiguration(parser.getConfiguracion());
                        cambiarDebug();
                    }
                    else{
                        parser = new Parser();
                        parser.insertarInstrucciones();
                        drawing_panel.setConfiguration(parser.getConfiguracion());
                    }
                }
                else{
                    cambiarDebug();
                }
                drawing_panel.repaint();
            }
        });
        add(debug);
        
        next = new JButton(new ImageIcon(path_next));
        next.setBounds(210,570,50,40);
        next.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                parser.ejecutarSiguiente();
                drawing_panel.setConfiguration(parser.getConfiguracion());
                drawing_panel.repaint();
            }
        });
        next.setEnabled(false);
        add(next);
        
        setLayout(null);
        setBounds(50,50,890,649);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    void cambiarDebug(){
        if(!mode_debug){
            drawing_panel.setEnabled(false);
            execute.setEnabled(false);
            next.setEnabled(true);
        }
        else{
            drawing_panel.setEnabled(true);
            execute.setEnabled(true);
            next.setEnabled(false); 
        }
        mode_debug = !mode_debug;
    }
}
