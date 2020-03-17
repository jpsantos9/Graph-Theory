/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphtheory;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;
import java.awt.Graphics;

/**
 *
 * @author mk
 */
public class Vertex implements Comparable {

    public String name;
    public Point location;
    public boolean wasFocused;
    public boolean wasClicked;
    private int size1 = 40;
    private int size2 = 50;
    public Vector<Vertex> connectedVertices;
    public int degree;
    public float normalizedDegree;
    public float closeness;
    public int betweenness;
    public float normalizedBetweenness;
    public int color = 0;

    public Vertex(String name, int x, int y) {
        this.name = name;
        location = new Point(x, y);
        connectedVertices = new Vector<Vertex>();
    }

    public void addVertex(Vertex v) {
        connectedVertices.add(v);
    }
    
    public void setDegree(int degree) {
    	this.degree = degree;
    }
    
    public void setNormalizedDegree(float normalizedDegree) {
    	this.normalizedDegree = normalizedDegree;
    }
    
    public void setCloseness(float closeness) {
    	this.closeness = closeness;
    }
    
    public void setBetweenness(int betweenness) {
    	this.betweenness = betweenness;
    }
    
    public void setNormalizedBetweenness(float normalizedBetweenness) {
    	this.normalizedBetweenness = normalizedBetweenness;
    }
    
    public void setColor(int color) {
    	this.color = color;
    }

    public boolean hasIntersection(int x, int y) {
        double distance = Math.sqrt(Math.pow((x - location.x), 2) + Math.pow((y - location.y), 2));

        if (distance > size2 / 2) {
            return false;
        } else {
            return true;
        }
    }

    public boolean connectedToVertex(Vertex v) {
        if (connectedVertices.contains(v)) {
            return true;
        }
        return false;
    }

    public int getDegree() {
        return connectedVertices.size();
    }
    
    public int getCloseness() {
        return connectedVertices.size();
    }

    public int compareTo(Object v) {
        if (((Vertex) v).getDegree() > getDegree()) {
            return 1;
        } else if (((Vertex) v).getDegree() < getDegree()) {
            return -1;
        } else {
            return 0;
        }
    }

    public void draw(Graphics g) {
        if (wasClicked) {
            g.setColor(Color.red);
        } else if (wasFocused) {
            g.setColor(Color.blue);
        } else {
            g.setColor(Color.black);
        }

        g.fillOval(location.x - size2 / 2, location.y - size2 / 2, size2, size2);
        g.setColor(Color.WHITE);
        g.fillOval(location.x - size1 / 2, location.y - size1 / 2, size1, size1);
        g.setColor(Color.BLACK);
        g.drawString(name, location.x, location.y);
    }
    
    public void drawDegree(Graphics g) {
        if (wasClicked) {
            g.setColor(Color.red);
        } else if (wasFocused) {
            g.setColor(Color.blue);
        } else {
            g.setColor(Color.black);
        }

        g.fillOval(location.x - size2 / 2, location.y - size2 / 2, size2+degree, size2+degree);
        g.setColor(Color.CYAN);
        g.fillOval(location.x - size1 / 2, location.y - size1 / 2, size1+degree, size1+degree);
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(degree), location.x, location.y);
    }
    
    public void drawNormalizedDegree(Graphics g) {
        if (wasClicked) {
            g.setColor(Color.red);
        } else if (wasFocused) {
            g.setColor(Color.blue);
        } else {
            g.setColor(Color.black);
        }

        g.fillOval(location.x - size2 / 2, location.y - size2 / 2, size2+degree, size2+degree);
        g.setColor(Color.CYAN);
        g.fillOval(location.x - size1 / 2, location.y - size1 / 2, size1+degree, size1+degree);
        g.setColor(Color.BLACK);
        g.drawString(Float.toString(normalizedDegree), location.x, location.y);
    }
    
    public void drawCloseness(Graphics g) {
        if (wasClicked) {
            g.setColor(Color.red);
        } else if (wasFocused) {
            g.setColor(Color.blue);
        } else {
            g.setColor(Color.black);
        }

        g.fillOval(location.x - size2 / 4, location.y - size2 / 2, size2+degree, size2+degree);
        g.setColor(Color.PINK);
        g.fillOval(location.x - size1 / 4, location.y - size1 / 2, size1+degree, size1+degree);
        g.setColor(Color.BLACK);
        g.drawString(Float.toString(closeness), location.x, location.y);
    }
    
    public void drawBetweenness(Graphics g) {
        if (wasClicked) {
            g.setColor(Color.red);
        } else if (wasFocused) {
            g.setColor(Color.blue);
        } else {
            g.setColor(Color.black);
        }

        g.fillOval(location.x - size2 / 2, location.y - size2 / 2, size2+betweenness, size2+betweenness);
        g.setColor(Color.YELLOW);
        g.fillOval(location.x - size1 / 2, location.y - size1 / 2, size1+betweenness, size1+betweenness);
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(betweenness), location.x, location.y);
    }
    
    public void drawNormalizedBetweenness(Graphics g) {
        if (wasClicked) {
            g.setColor(Color.red);
        } else if (wasFocused) {
            g.setColor(Color.blue);
        } else {
            g.setColor(Color.black);
        }

        g.fillOval(location.x - size2 / 2, location.y - size2 / 2, size2+betweenness, size2+betweenness);
        g.setColor(Color.YELLOW);
        g.fillOval(location.x - size1 / 2, location.y - size1 / 2, size1+betweenness, size1+betweenness);
        g.setColor(Color.BLACK);
        g.drawString(Float.toString(normalizedBetweenness), location.x, location.y);
    }
    
    public void drawColor(Graphics g) {
    	if (wasClicked) {
            g.setColor(Color.red);
        } else if (wasFocused) {
            g.setColor(Color.blue);
        } else {
            g.setColor(Color.black);
        }

        g.fillOval(location.x - size2 / 2, location.y - size2 / 2, size2+betweenness, size2+betweenness);
//        g.setColor(new Color(color+color*100));
        g.setColor(Color.getHSBColor(color/10f, 1.00f, 1.00f));
        g.fillOval(location.x - size1 / 2, location.y - size1 / 2, size1+betweenness, size1+betweenness);
        g.setColor(Color.BLACK);
        g.drawString(Integer.toString(color), location.x, location.y);
    }
}
