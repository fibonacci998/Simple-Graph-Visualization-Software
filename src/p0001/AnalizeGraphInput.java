/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p0001;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import javafx.util.Pair;

/**
 *
 * @author tuans
 */
public class AnalizeGraphInput {
    String inforGraph;
    HashMap<String, Pair<String,String>> inforVertices;
    Vector<Pair<String,Pair<String,String>>> inforEdges;
    Graph graph;
    public AnalizeGraphInput(String text){
        inforGraph=text;
        inforVertices=new HashMap<>();
        inforEdges=new Vector();
    }
    
    public String getNameGraph(){
        String name="";
        for (int i=0;i<inforGraph.length();i++){
            if (inforGraph.charAt(i)=='{') break;
            name+=inforGraph.charAt(i);
        }
        return name;
    }
    public void analize(){
        
        StringTokenizer st=new StringTokenizer(inforGraph,"\n");
        while (st.hasMoreTokens()){
            String temp=st.nextToken();
            if (temp.substring(0, 1).compareTo("\\")==0) continue;
            if (temp.contains("label")&&temp.contains("color")){
                analizeVertice(temp);
            }
            else if (temp.contains("label")){
                analizeEdge(temp);
            }
        }
    }
    public Graph createGraph(){ 
        Graph graph=new Graph(getNumberVertices());
        Iterator it=inforVertices.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair=(Map.Entry)it.next();
            String name=(String) pair.getKey();
            Pair propertise=(Pair) pair.getValue();
            String label=(String) propertise.getKey();
            String color=(String) propertise.getValue();
            graph.addVertice(name, label, color);
            it.remove();
        }
        
        for (Pair element:inforEdges){
            String u=(String) element.getKey();
            Pair temp=(Pair) element.getValue();
            String v=(String) temp.getKey();
            String label=(String) temp.getValue();
            graph.addEdge(u, v, label);
        }
        return graph;
    }
    public int getNumberVertices(){
        return inforVertices.size();
    }
    private void analizeVertice(String inforVertice) {
        boolean startLabel=false;
        boolean startColor=false;
        boolean startName=false;
        String labelVertice="",nameVertice="",colorVertice="";       
        StringTokenizer st=new StringTokenizer(inforVertice,"\t\" ");
        
        while (st.hasMoreTokens()){
            String temp=st.nextToken();
            if (!startName){
                nameVertice=temp;
                startName=true;
                continue;
            }
            if (temp.contains("label")){
                labelVertice=st.nextToken();
                continue;
            }
            if (temp.contains("color")){
                colorVertice=st.nextToken();
                continue;
            }
        }
        inforVertices.put(nameVertice, new Pair<>(labelVertice,colorVertice));
    }
    private void analizeEdge(String edge) {
        StringTokenizer st=new StringTokenizer(edge,"\t\"-> ");
        String u="",v="",label="";
        while (st.hasMoreTokens()){
            String temp=st.nextToken();
            if (u.compareTo("")==0){
                u=temp;
                continue;
            }
            if (v.compareTo("")==0){
                v=temp;
                continue;
            }
            if (temp.contains("label")){
                label=st.nextToken();
            }
        }
        Pair pair1=new Pair(v, label);
        inforEdges.add(new Pair(u,pair1));
    }
}
