/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p0001;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author tuans
 */
public class DrawGraph extends Canvas{
    //canvas create after paint graph
    final Canvas canvas;
    final int INT_SIZE=5;
    Graph graph;
    
    //list of axis point to draw
    HashMap<String, Pair> saveVerticesAxis;
    HashMap<String, Pair> sizeVertices;
    public DrawGraph() {
        this.setSize(300, 300);
        canvas = this;
    }
    public DrawGraph(Graph graph){
        this.setSize(300,300);
        this.graph=graph;
        canvas=this;
    }
    //return canvas of drawGraph
    public Canvas getCanvas(){
        return canvas;
    }
    //if has not axis of element, random axis to draw it
    public void paint(Graphics g){
        saveVerticesAxis=new HashMap<>();
        sizeVertices=new HashMap<>();
        g.create();
        System.out.println("new");
        Graphics2D g2=(Graphics2D) g;
        g2.setBackground(Color.WHITE);
        //g2.drawRect(0, 0, this.getWidth(), this.getHeight());
        g2.clearRect(0, 0, this.getWidth()*10, this.getHeight()*10);
        Iterator it=graph.nameVertice.entrySet().iterator();
        //loop to paint all element
        while (it.hasNext()){
            Map.Entry pair=(Map.Entry) it.next();
            String name=(String) pair.getKey();
            Vector propertise=graph.propertiseVertice.get(name);
            String label=(String) propertise.get(0);
            String color=(String) propertise.get(1);
            int x=getRanNum(0,250,'X');
            int y=getRanNum(0,250,'Y');
            drawVertice(g,name,label,color,x,y);
            saveVerticesAxis.put(name,new Pair<>(x,y));
        }
        Boolean error=false;
        //g2.drawLine(300, 3, 60, 600);
        for (Pair edge:graph.adjEdges){
            String u=(String) edge.getKey();
            String v=(String) edge.getValue();
            String label=graph.getLabelEdge(u, v);
            int x1=(int) saveVerticesAxis.get(u).getKey();
            int y1=(int) saveVerticesAxis.get(u).getValue();
            int height1=(int) sizeVertices.get(u).getKey();
            int width1=(int) sizeVertices.get(u).getValue();
            int x2=(int) saveVerticesAxis.get(v).getKey();
            int y2=(int) saveVerticesAxis.get(v).getValue();
            int height2=(int) sizeVertices.get(v).getKey();
            int width2=(int) sizeVertices.get(v).getValue();
            if (y1+height1+INT_SIZE<y2){
                int xStart=x1+width1/2;
                int yStart=y1+height1;
                int xEnd=x2+width2/2;
                int yEnd=y2;
                drawArrow(g, xStart, yStart, xEnd, yEnd,label);
                continue;
            }
            
            if (y2+height2+INT_SIZE<y1){
                int xStart=x1+width1/2;
                int yStart=y1;
                int xEnd=x2+width2/2;
                int yEnd=y2+height2;
                drawArrow(g, xStart, yStart, xEnd, yEnd,label);
                continue;
            }
            
            if (x1+width1+INT_SIZE<x2){
                int xStart=x1+width1;
                int yStart=y1+height1/2;
                int xEnd=x2;
                int yEnd=y2+height2/2;
                drawArrow(g, xStart, yStart, xEnd, yEnd,label);
                continue;
            }
            //if (x2+width2+INT_SIZE<x1)
            {
                int xStart=x1;
                int yStart=y1+height1/2;
                int xEnd=x2+width2;
                int yEnd=y2+height2/2;
                drawArrow(g, xStart, yStart, xEnd, yEnd,label);
                continue;            
            }
        }
    }
    //if has axis of element, use it to draw it
    public void paintWithSavedData(Graphics g){
        Graphics2D g2=(Graphics2D) g;
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, this.getWidth(), this.getHeight());
        Iterator it=graph.nameVertice.entrySet().iterator();
        int position=0;
        //loop to paint all element
        while (it.hasNext()){
            Map.Entry pair=(Map.Entry) it.next();
            String name=(String) pair.getKey();
            Vector propertise=graph.propertiseVertice.get(name);
            String label=(String) propertise.get(0);
            String color=(String) propertise.get(1);
            Pair saved=(Pair) saveVerticesAxis.get(name);
            int x=(int) saved.getKey();
            int y=(int) saved.getValue();
            drawVertice(g,name,label,color,x,y);
            position++;
        }
    }
    
    //process draw vertice into graphic with its information
    private void drawVertice(Graphics g, String name, String label, String color,int x,int y) {
        Graphics2D g2 = (Graphics2D) g;
        Color colorBorder;
        //get color type from string type 
        try {
            Field field = Class.forName("java.awt.Color").getField(color);
            colorBorder = (Color)field.get(null);
        } catch (Exception e) {
            colorBorder = null; // Not defined
        }
        g2.setColor(colorBorder);
        //set border for vertice
        float thickness=3;
        Stroke oldStroke=g2.getStroke();
        g2.setStroke(new BasicStroke(thickness));
        //set width and height for text inside vertice
        String text=label;
        FontMetrics fm=g2.getFontMetrics();
        int w=fm.stringWidth(text);
        int h=fm.getAscent();
        //draw vertice 
        g2.drawOval(x, y, w*2, h*4);
        sizeVertices.put(name, new Pair(h*4,w*2));
        g2.setStroke(oldStroke);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("TimesNewRoman", Font.BOLD, 13));
        //draw string inside vertice
        g2.drawString(text, x+(w/2), y+(h*2));
    }
    //process draw arrow for (x1,y1) to (x2,y2) into graphic
    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2,String label) {
        Graphics2D g2 = (Graphics2D) g;
        
        int ARR_SIZE = 10;
        //set border for line
        float thickness=2;
        Stroke oldStroke=g2.getStroke();
        g2.setStroke(new BasicStroke(thickness));
        
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        //transform the draw
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g2.transform(at);
        
        // Draw horizontal arrow starting in (0, 0)
        g2.drawLine(0, 0, len, 0);
        g2.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE},
                      new int[] {0, -ARR_SIZE, ARR_SIZE}, 3);
        
        //set old status of graphic
        g2.setStroke(oldStroke);
        try {
            g2.transform(at.createInverse());
        } catch (NoninvertibleTransformException ex) {
            Logger.getLogger(DrawGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
        g2.drawString(label, (x1+x2)/2+8, (y1+y2)/2);
    }
    //return random number from min to max
    private int getRanNum(int min, int max, char label) {
        while (true){
            int number = new Random().nextInt(max - min) +min;
            if (check(number,label)){
                return number;
            }
        }
    }

    private boolean check(int number,char label) {
        Iterator it=saveVerticesAxis.entrySet().iterator();
        Boolean ok=false;
        while (it.hasNext()){
            Map.Entry pair=(Map.Entry) it.next();
            Pair value = (Pair) pair.getValue();
            int x=(int) value.getKey();
            int y=(int) value.getValue();
            int numGet=(label=='X')?x:y;
            if (Math.abs(numGet-number)<50){
                return false;
            }
        }
        return true;
    }
}
