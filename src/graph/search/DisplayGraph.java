/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.search;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Diego Jacobs
 */
public class DisplayGraph extends JPanel{
    private final int UnitX;
    private final int UnitY;
    private final int SizeX;
    private final int SizeY;
    private final ArrayList<Node> Path;
    private final Graph Graph;
    private final BufferedImage image;

    
    public DisplayGraph(int sizeX, int sizeY, BufferedImage image, Graph graph, ArrayList path) {
        this.UnitX = 450/sizeX;
        this.UnitY = 450/sizeY;
        this.SizeX = sizeX;
        this.SizeY = sizeY;
        this.image = image;
        this.Path = path;
        this.Graph = graph;
        setSize(UnitX * sizeX, UnitY * sizeY);
        setVisible(true);
    }
    
    @Override
    public void paint(Graphics graphics) {
        graphics.setColor(Color.GRAY);
        paintGraph(graphics);
        paintPath(graphics);
    }
    
    private void paintRectangle(Graphics graphics, int x, int y) {
        graphics.fill3DRect(UnitX * x, UnitY * y, UnitX, UnitY, true);
    }
        
    public void paintGraph(Graphics graphics){
        boolean hasStart = false;
        for (int y = 0; y < SizeY; y++) {
            for (int x = 0; x < SizeX; x++) {
                graphics.setColor(Color.white);
                Types.Color color =  this.Graph.getColor(this.image.getRGB(x, y));
             
                if (color == Types.Color.BLACK)
                    graphics.setColor(Color.black);
                
                if (color == Types.Color.WHITE)
                    graphics.setColor(Color.white);

                if (color == Types.Color.RED && !hasStart) 
                {
                    hasStart = true;
                    graphics.setColor(Color.red);
                }
                
                if (color == Types.Color.GREEN) 
                    graphics.setColor(Color.green);
                
                paintRectangle(graphics, x, y);
           }
       }
    }
    
    private void paintPath(Graphics graphics) {       
        graphics.setColor(Color.MAGENTA);
        if(Path == null){
            JOptionPane.showMessageDialog(null, "Invalid.");
        }
        else
            for (Node n : Path) {
                if (!this.Graph.getGoals().contains(n) && n != this.Graph.getInitial()) {
                    int x = n.getX(); int y = n.getY();
                    paintRectangle(graphics, x, y);   
                }
           }
    }
}
