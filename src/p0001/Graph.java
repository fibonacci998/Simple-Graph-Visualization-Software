/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p0001;
//taieubfibuefiusbe
import java.util.HashMap;
import java.util.Vector;
import javafx.util.Pair;

/**
 *
 * @author tuans
 */
public class Graph {
    int sizeGraph;
    String[][] matrixGraph;
    int labelName;   
    HashMap<String, Integer> nameVertice;
    HashMap<String, Vector> propertiseVertice;
    Vector<Pair<String,String>> adjEdges;
    public Graph(){}
    public Graph(int numberVertices) {
        sizeGraph=numberVertices;
        labelName=0;
        matrixGraph=new String[sizeGraph][sizeGraph];
        propertiseVertice=new HashMap<>();
        nameVertice=new HashMap<>();
        adjEdges=new Vector<>();
    }
    //add vertice to graph and name it by number
    void addVertice(String name,String label,String color){
        nameVertice.put(name, labelName++);
        Vector temp=new Vector();temp.add(label);temp.add(color);
        propertiseVertice.put(name,temp);
    }
    //add edge to graph
    void addEdge(String u, String v, String label){
        adjEdges.add(new Pair<>(u,v));
        matrixGraph[nameVertice.get(u)][nameVertice.get(v)]=label;
    }
    //return label of edge
    String getLabelEdge(String u,String v){
        return matrixGraph[nameVertice.get(u)][nameVertice.get(v)];
    }
}
