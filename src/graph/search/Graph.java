/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph.search;

import Types.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Diego Jacobs
 */
public class Graph {
    private Node Initial;
    private ArrayList<Node> Goals;
    private ArrayList<ArrayList<Node>> Graph;
    private int SizeY;
    private int SizeX;
    
    public Graph(int sizeX, int sizeY){
        this.SizeX = sizeX;
        this.SizeY = sizeY;
        this.Goals = new ArrayList<>();
    }

    public ArrayList<ArrayList<Node>> getGraph() {
        return Graph;
    }

    public void setGraph(ArrayList<ArrayList<Node>> Graph) {
        this.Graph = Graph;
    }

    public int getSizeY() {
        return SizeY;
    }

    public void setSizeY(int sizeY) {
        this.SizeY = sizeY;
    }

    public int getSizeX() {
        return SizeX;
    }

    public void setSizeX(int sizeX) {
        this.SizeX = sizeX;
    }
    
    public Node getNodo(int x, int y) {
        return Graph.get(y).get(x);
    }

    public Node getInitial() {
        return Initial;
    }

    public void setInitial(Node Initial) {
        this.Initial = Initial;
    }

    public ArrayList<Node> getGoals() {
        return Goals;
    }

    public void setGoals(ArrayList<Node> Goals) {
        this.Goals = Goals;
    }
    
    public void Create() 
    {
        Node node;
        Graph = new ArrayList<>();
        for (int y = 0; y < SizeY; y++) 
        {
            ArrayList temp= new ArrayList();
            Graph.add(temp);
            for (int x = 0; x < SizeX; x++)
            {
                node = new Node(x, y, this);
                Graph.get(y).add(node);
            }
        }
    }
    
    public void fillGraph(BufferedImage image) {
        for (int y = 0; y < SizeY; y++) {
            for (int x = 0; x < SizeX; x++) {
                Color color =  this.getColor(image.getRGB(x, y));
                
                if (color == Color.BLACK) 
                    this.Graph.get(y).get(x).setBlocked();
                
                if (color == Color.RED) 
                    this.Initial = this.Graph.get(y).get(x);
                
                if (color == Color.GREEN)
                    this.Goals.add(this.Graph.get(y).get(x)); 
            }
        }
    }
    
    public ArrayList<Action> getNeighbors(Node node) {
        int x = node.getX(); 
        int y = node.getY();
        
        ArrayList<Action> neighbors = new ArrayList();
        
        //Up
        if ((y != 0)) {
            Node temp = this.getNodo(x, y - 1);
            Action action = new Action(node, temp);
            neighbors.add(action);
        }
        
        //Right
        if ((x != (this.getSizeX()) - 1)) {
            Node temp = this.getNodo(x + 1,y);
            Action action = new Action(node, temp);
            neighbors.add(action);
        }
        
        //Left
        if ((x != 0)) {
            Node temp = this.getNodo(x - 1, y);
            Action action = new Action(node, temp);
            neighbors.add(action);

        }
        
        //Down
        if ((y != (this.getSizeY() - 1))) {
            Node temp = this.getNodo(x, y + 1);
            Action action = new Action(node, temp);
            neighbors.add(action);
        }
        
        
        return neighbors;
    }
    
    public Color getColor(int pixel) {
        
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        int sum = red + green + blue;
        
        if ((red > 200 && green < 200 && blue < 200))
            return Color.RED;

        if ((sum) > 750)
            return Color.WHITE;

        if (green > 100 && red > 20 && blue < 80)
            return Color.GREEN;

        if (red < 150 && green < 150 && blue < 150)
            return Color.BLACK;

        if (red == green && green == blue)
            return Color.BLACK;
        
        return Color.GREEN;
    }
}
