/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphtheory;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.JFileChooser;

/**
 *
 * @author mk
 */
public class FileManager {

    public JFileChooser jF;
    GraphProperties gP = new GraphProperties();

    public FileManager() {
        jF = new JFileChooser();


    }

    public void saveFile(Vector<Vertex> vList, Vector<Edge> eList, File fName) {
        try {
        	int[][] matrix = gP.generateAdjacencyMatrix(vList, eList);
            BufferedWriter out = new BufferedWriter(new FileWriter(fName));

            out.write(""+vList.size());
            out.newLine();
            for (Vertex v : vList) {
                out.write(v.name);
                out.newLine();
            }
            
            for (int i = 0; i < vList.size(); i++) {
            	for (int j = 0; j< vList.size(); j++) {
            		out.write(Integer.toString(matrix[i][j]));
            	}
            	out.newLine();;
            }
//            for (int i = 0; i < vList.size(); i++) {
//                for (int j = 0; j < vList.size(); j++) {
//                    if (vList.get(i).connectedToVertex(vList.get(j))) {
//                        out.write("1");
//                    } else {
//                        out.write("0");
//                    }
//                }
//                out.newLine();
//            }
            for (int k = 0; k < vList.size(); k++) {
                out.write(vList.get(k).location.x + "," + vList.get(k).location.y);
                out.newLine();
            }
            out.close();

        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public Vector<Vector> loadFile(File fName) {
        Vector<Vertex> vertexList = new Vector<Vertex>();
        Vector<Edge> edgeList = new Vector<Edge>();
        Vector<Vector> file = new Vector<Vector>();
        int[][] matrix = new int[50][50];
        try {
            FileReader f = new FileReader(fName.toString());
            Scanner data = new Scanner(f);
            if (data.hasNext()) {
                int size = Integer.parseInt(data.nextLine());
                for (int i = 0; i < size; i++) {//vertex only
                    Vertex v = new Vertex(data.nextLine(), 0, 0);
                    vertexList.add(v);
                }

                for (int j = 0; j < vertexList.size(); j++) { // adjacency list
                    String adjacencyLine = data.nextLine();
                    System.out.println(adjacencyLine);
                    for (int k = 0; k < vertexList.size(); k++) {
                        if (adjacencyLine.charAt(k) != '0') {
                            vertexList.get(j).addVertex(vertexList.get(k));
                        }
                    }
                    
                    

                    for (int l = 0; l < vertexList.size(); l++) {
                    	matrix[j][l] = Character.getNumericValue(adjacencyLine.charAt(l));
                    }
//                    for (int l = j + 1; l < vertexList.size(); l++) { //edges
//                        if (adjacencyLine.charAt(l) != '0') {
//                            Edge e = new Edge(vertexList.get(j), vertexList.get(l));
//                            e.directed = 1;
//                            e.weight = Character.getNumericValue(adjacencyLine.charAt(l));
//                            if (findInverse(edgeList, j, l)) {
//                            	e.directed = 0;
//                            }
//                            edgeList.add(e);
//                        }
//                    }
                }
                
                for (int j = 0; j < vertexList.size(); j++) {
                	for (int l = j + 1; l < vertexList.size(); l++) {
                		if (matrix[j][l] != 0) {
                			Edge e = new Edge(vertexList.get(j), vertexList.get(l));
                			e.weight = matrix[j][l];
                			if (matrix[j][l]==matrix[l][j]) {
                				e.directed = 0;
                			} else {
                				e.directed = 1;
                			}
                			edgeList.add(e);
                		}
                	}
                }
                
                

                if (data.hasNextLine()) {
                    for (Vertex v : vertexList) {
                        String pos = data.nextLine();
                        v.location = new Point(Integer.parseInt(pos.split(",")[0]), Integer.parseInt(pos.split(",")[1]));
                    }
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        file.add(vertexList);
        file.add(edgeList);
        return file;
    }
    
    public boolean findInverse(Vector<Edge> eList, int j, int l) {
    	for (Edge edge : eList) {
    		if(edge.vertex1.name==Integer.toString(l) && edge.vertex2.name == Integer.toString(j)) {
    			edge.directed = 0;
    			return true;
    		}
    	}
    	return false;
    }
}
