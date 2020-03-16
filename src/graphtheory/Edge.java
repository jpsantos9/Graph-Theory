/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphtheory;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 
import java.awt.geom.AffineTransform;

/**
 *
 * @author mk
 */
public class Edge implements ActionListener{

    public Vertex vertex1;
    public Vertex vertex2;
    public boolean wasFocused;
    public boolean wasClicked;
    public int weight = 1;
    
    private JTextField tf1;
    private JButton b1;
    private JFrame f;
    
    public int directed = 0;

    public Edge(Vertex v1, Vertex v2) {
        vertex1 = v1;
        vertex2 = v2;
    }
    
    private final int ARR_SIZE = 10;

    void drawArrow(Graphics g1, int x2, int y2, int x1, int y1) {
    	Graphics2D g = (Graphics2D) g1.create();
     	
        double dx = (x2 - x1)*0.91, dy = (y2 - y1)*0.91;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);
       // g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }

    public void draw(Graphics g) {
        if (wasClicked) {
            g.setColor(Color.red);
        } else if (wasFocused) {
            g.setColor(Color.blue);
        } else {
            g.setColor(Color.black);
        }
        g.drawLine(vertex1.location.x, vertex1.location.y, vertex2.location.x, vertex2.location.y);
        
        if(directed == 1) { 
        	drawArrow(g, vertex1.location.x, vertex1.location.y, vertex2.location.x, vertex2.location.y);
        }
        
        int x = (vertex1.location.x+vertex2.location.x)/2;
    	int y = (vertex1.location.y+vertex2.location.y)/2;
        g.drawString(Integer.toString(weight), x, y);
    }
    
    public void askWeight() {
    	f= new JFrame("Weight Value"); 
    	tf1 = new JTextField();  
        tf1.setBounds(75,5,150,20);
        b1=new JButton("enter");  
        b1.setBounds(100,30,100,30); 
        b1.addActionListener(this);  
        f.add(tf1);
        f.add(b1);
        f.setSize(300,150);
        f.setLayout(null);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {  
        String s1=tf1.getText();   
        int a=Integer.parseInt(s1);    
        if(e.getSource()==b1){  
            weight = a;
        } else {
        	weight = 1;
        }
        f.dispose();
    }  

    public boolean hasIntersection(int x, int y) {
        int x1, x2, y1, y2;
        x1 = vertex1.location.x;
        x2 = vertex2.location.x;
        y1 = vertex1.location.y;
        y2 = vertex2.location.y;
        float slope = 0;
        if (x2 != x1) {
            slope = (y2 - y1) / (x2 - x1);
        }

        float b = Math.abs(x1 * slope - y1);

        if (y + b <= Math.round(slope * x) + 10 && y + b >= Math.round(slope * x) - 10) {
            if (x1 > x2 && y1 > y2) {
                if (x <= x1 && x >= x2 && y <= y1 && y >= y2) {
                    return true;
                }
            } else if (x1 < x2 && y1 > y2) {
                if (x <= x2 && x >= x1 && y <= y1 && y >= y2) {
                    return true;
                }
            } else if (x1 < x2 && y1 < y2) {
                if (x <= x2 && x >= x1 && y <= y2 && y >= y1) {
                    return true;
                }
            } else if (x <= x1 && x >= x2 && y <= y2 && y >= y1) {
                return true;
            }
        }
        return false;

    }
}
