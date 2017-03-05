/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithms;

import graph.search.Action;
import graph.search.Graph;
import graph.search.IProblemDefinition;
import graph.search.Node;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Diego Jacobs
 */
public class AStar implements IProblemDefinition{
    private Graph Graph;
    private ArrayList<Node> FinalPath;
    private final ArrayList<Node> Checked;
    
    public AStar(int sizeX, int sizeY, BufferedImage image){
        this.Graph = new Graph(sizeX, sizeY);
        this.Graph.Create();
        this.Graph.fillGraph(image);
        this.Checked = new ArrayList<>();
    }

    public Graph getGraph() {
        return Graph;
    }

    public void setGraph(Graph Graph) {
        this.Graph = Graph;
    }

    public ArrayList<Node> getFinalPath() {
        return FinalPath;
    }

    public void setFinalPath(ArrayList<Node> FinalPath) {
        this.FinalPath = FinalPath;
    }
    
    
    public void calcular(boolean diagonales,boolean dijkstra,boolean greedy, boolean h1) 
    {
        nodosEvaluados= new HashSet<>(); //set de nodos evaluados
        PriorityQueue<Nodo> nodosPorEvaluar = new PriorityQueue<>();//set de nodos por evaluar, contiene inicialmente al nodo inicial

        this.inicio.setFuncionG(0); //costo desde el inicio hasta el mejor camino conocido
        
        nodosPorEvaluar.add(inicio); //se evalúa el primer nodo
        
        int contadorIteraciones=0; //contador de iteraciones de algoritmo
        while (!nodosPorEvaluar.isEmpty()) {
            
            Nodo actual = nodosPorEvaluar.poll();//obtener el nodo con menor funcion f

            if (goalTest(actual)) {//en caso de encontrar el destino, termina el algoritmo  
                System.out.println("Iteraciones totales A* -> " + contadorIteraciones);
                reconstruirCamino(actual);//se muestra el nuevo camino
                System.out.println("Costo Total A* -> " + (pathCost(this.path)));
                System.out.println(path.toString() + " ->Camino más corto");
                break;
            }
            //cambio de estructura para evaluar el nodo actual
           // nodosPorEvaluar.remove(actual);
            nodosEvaluados.add(actual);

            //función obtiene los nodos adyacentes del nodo actual
            for (Accion accion : actions(actual)) {
                Nodo adyacente = result(actual, accion);
               //en caso de que un nodo ya haya sido evaluado
                //se omite del ciclo
                if (nodosEvaluados.contains(adyacente))
                    continue; //se salta una iteracion
                
                //mientras no sea un obstaculo el nodo
                if (!adyacente.isObstaculo()) {
                    //costo real del nodo adyacente
                    double nuevoCosto = stepCost(actual,accion,  adyacente);
                    //se evalua si el nodoa adyacente tiene un menor costo
                    
                    if (!nodosPorEvaluar.contains(adyacente)|| nuevoCosto < adyacente.getFuncionG()){
                        adyacente.setRaiz(actual); //añadir el camino nuevo
                        adyacente.setFuncionG(nuevoCosto); 
                        adyacente.setFuncionHeuristica(calcularHeuristica(adyacente, destinos, diagonales, h1));
                        if (dijkstra)//si es dijsktra la heuristica es cero
                            adyacente.setFuncionHeuristica(0);
                        if (greedy)
                            adyacente.setFuncionG(0);
                        //la cola prioritaria ordena los nodos cada vez que se inserta un nodo
                        //el nodo con menor funcion f es el primero
                        nodosPorEvaluar.add(adyacente);//se añade hasta de último el nodos adyacente
                    }
                   
                }
            }//cierra for adyacente
            contadorIteraciones++;
        }//cierra while
    }//cierra calcular

    //método para mostrar el camino más corto encontrado
    public void reconstruirCamino(Nodo nodo)
    {
        //grafo.getGrafoGrafico(); //mostrar grafo en terminal
        while (!(nodo.getRaiz() == null)) {
            path.add(nodo);
            nodo = nodo.getRaiz();
        }
        path.add(nodo);//agregar el último nodo
        Collections.reverse(path); //cambiar el orden
        
    }
   
    //distancia REAL entre nodos, costo dijkstra
    public double getDistanciaEntre(Nodo n1, Nodo n2) {
        if ((n1.getX() == n2.getX() ) || (n1.getY() == n2.getY()))
            return 1; //si estan a a la par el costo es constante
        else
            return Math.sqrt(2); //en otro caso estan en diagonal, costo = raiz de 2
    }
    
    
    //referencia: http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#diagonal-distance
    public double calcularHeuristica(Nodo current, ArrayList<Nodo> goals, boolean diagonales, boolean h1) {
       
        double D = 1.0; //peso de aristas adyacentes
        double D2 = Math.sqrt(2); //peso de arista diagonales
        double tempLength = this.grafo.getAlto()*this.grafo.getAncho();
        Nodo goal = null;
        for (int i = 0; i < goals.size(); i++) {
            double dxTemp = Math.abs(current.getX() - goals.get(i).getX());
            double dyTemp = Math.abs(current.getY() - goals.get(i).getY());
            double calcLength = Math.sqrt(Math.pow(dxTemp, 2) + Math.pow(dyTemp, 2));
            if (calcLength < tempLength) {
                tempLength = calcLength;
                goal = goals.get(i);
            }
            
        }
        //manhattan de 4
        double dx = Math.abs(current.getX() - goal.getX());
        double dy = Math.abs(current.getY() - goal.getY());
        //cross breaking ties
        double dx1 = current.getX()-goal.getX();
        double dy1 = current.getY()-goal.getY();
        double p = 1/1000;//minimum cost of taking one step/expected maximum path length
        double dw = inicio.getX()-goal.getX(); //cross heuristics
        double dz = inicio.getY()-goal.getY(); //cross heuristics
    
        //se realiza un promedio de distancia manhattan de 8 movimientos y 4 movimientos
        double promedio = ((D*(dx+dy)+(D2-2*D)*Math.min(dx,dy))+(D*(dx+dy)))/2;
        if (diagonales)
            return promedio;//normal heuristics for diagonals
        if (h1) //sum of absolute differences. Manhattan distance
            return D*(dx + dy);
        return (D*(dx+dy))+Math.abs(dx1*dz-dw*dy1); //normal heuristic + cross breaking ties
       
    }


    @Override
    public double stepCost(Nodo s1, Accion a, Nodo s2) {
       if (s1.equals(a.getOrigen()) && s2.equals(a.getDestino())) {
           return s1.getFuncionG() + this.getDistanciaEntre(s1, s2);
       }
       throw new UnsupportedOperationException("Step cost not valid"); //To change body of generated methods, choose Tools | Templates.
    }

    //Get the total cost of the path
    @Override
    public double pathCost(ArrayList<Nodo> path) {
        Nodo nodo = path.get(path.size() - 1);
        return nodo.getFuncionG() + nodo.getFuncionHeuristica();
    }
    
    @Override
    public ArrayList<Action> actions(Node node) {
        return this.Graph.getNeighbors(node);
    }

    @Override
    public float stepCost(Node fromNode, Node toNode, Action action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public float pathCost(ArrayList<Node> path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean goalTest(Node goal) {
        return this.Graph.getEndList().contains(goal);
    }

    @Override
    public Node result(Node node, Action action) {
        if(node.equals(action.getFrom())){
            return action.getTo();
        }
        
        return null;
    }
    
    
}
