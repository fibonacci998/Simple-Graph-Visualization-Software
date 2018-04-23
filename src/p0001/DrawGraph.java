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
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;
import javafx.util.Pair;

/**
 *
 * @author tuans
 */
public class DrawGraph extends Canvas{
    //canvas create after paint graph
    final Canvas canvas;
    
    Graph graph;
    
    //list of axis point to draw
    Vector saveVerticesAxis;
    
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
        saveVerticesAxis=new Vector();
        Graphics2D g2=(Graphics2D) g;
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, this.getWidth(), this.getHeight());
        Iterator it=graph.nameVertice.entrySet().iterator();
        //loop to paint all element
        while (it.hasNext()){
            Map.Entry pair=(Map.Entry) it.next();
            String name=(String) pair.getKey();
            Vector propertise=graph.propertiseVertice.get(name);
            String label=(String) propertise.get(0);
            String color=(String) propertise.get(1);
            int x=getRanNum(0,200);
            int y=getRanNum(0,200);
            drawVertice(g,name,label,color,x,y);
            saveVerticesAxis.add(new Pair<>(x,y));
        }
        drawArrow(g, 30, 30, 200, 200);
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
            Pair saved=(Pair) saveVerticesAxis.get(position);
            int x=(int) saved.getKey();
            int y=(int) saved.getValue();
            drawVertice(g,name,label,color,x,y);
            position++;
        }
        drawArrow(g, 30, 30, 200, 200);
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
        g2.setStroke(oldStroke);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("TimesNewRoman", Font.BOLD, 13));
        //draw string inside vertice
        g2.drawString(text, x+(w/2), y+(h*2));
    }
    //process draw arrow for (x1,y1) to (x2,y2) into graphic
    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        Graphics2D g2 = (Graphics2D) g;
        int ARR_SIZE = 10;
        //set border for line
        float thickness=2;
        Stroke oldStroke=g2.getStroke();
        g2.setStroke(new BasicStroke(thickness));
        
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g2.transform(at);
        
        // Draw horizontal arrow starting in (0, 0)
        g2.drawLine(0, 0, len, 0);
        g2.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
        g2.setStroke(oldStroke);
    }
    //return random number from min to max
    private int getRanNum(int min, int max) {
        int number = new Random().nextInt(max - min) +min;
        return number;
    }
}
