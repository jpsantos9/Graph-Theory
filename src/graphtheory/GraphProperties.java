/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphtheory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

/**
 *
 * @author mk
 */
public class GraphProperties {// implements ActionListener{

    public int[][] adjacencyMatrix;
    public int[][] distanceMatrix;
    public Vector<VertexPair> vpList;
    
    private JTextField tf1;
    private JButton b1;
    private JFrame f;

//    public int w, start, end;
    
    public int[][] generateAdjacencyMatrix(Vector<Vertex> vList, Vector<Edge> eList) {
        adjacencyMatrix = new int[vList.size()][vList.size()];

        for (int a = 0; a < vList.size(); a++)//initialize
        {
            for (int b = 0; b < vList.size(); b++) {
                adjacencyMatrix[a][b] = 0;
            }
        }

    	boolean directed = false;
    	
        for (int i = 0; i < eList.size(); i++) {
        	directed = Canvas.directedEdge[vList.indexOf(eList.get(i).vertex2)][vList.indexOf(eList.get(i).vertex1)];    // calling the array arr from that object
        	
        	if(directed == true){
        	 	adjacencyMatrix[vList.indexOf(eList.get(i).vertex2)][vList.indexOf(eList.get(i).vertex1)] = eList.get(i).weight;
        	}
        	else{
        	 	adjacencyMatrix[vList.indexOf(eList.get(i).vertex1)][vList.indexOf(eList.get(i).vertex2)] = eList.get(i).weight;
                adjacencyMatrix[vList.indexOf(eList.get(i).vertex2)][vList.indexOf(eList.get(i).vertex1)] = eList.get(i).weight;
        	}
        }
        return adjacencyMatrix;
    }

    public int[][] generateDistanceMatrix(Vector<Vertex> vList) {				//di na gumagana ng maayos
        distanceMatrix = new int[vList.size()][vList.size()];

        for (int a = 0; a < vList.size(); a++)//initialize
        {
            for (int b = 0; b < vList.size(); b++) {
                distanceMatrix[a][b] = 0;
            }
        }

        VertexPair vp;
        int shortestDistance;
        for (int i = 0; i < vList.size(); i++) {
            for (int j = i + 1; j < vList.size(); j++) {
                vp = new VertexPair(vList.get(i), vList.get(j));
                shortestDistance = vp.getShortestDistance();
                distanceMatrix[vList.indexOf(vp.vertex1)][vList.indexOf(vp.vertex2)] = shortestDistance;
                distanceMatrix[vList.indexOf(vp.vertex2)][vList.indexOf(vp.vertex1)] = shortestDistance;
            }
        }
        return distanceMatrix;
    }

    public void displayContainers(Vector<Vertex> vList) {
        vpList = new Vector<VertexPair>();
        int[] kWideGraph = new int[10];
        for (int i = 0; i < kWideGraph.length; i++) {
            kWideGraph[i] = -1;
        }



        VertexPair vp;

        for (int a = 0; a < vList.size(); a++) {    // assign vertex pairs
            for (int b = a + 1; b < vList.size(); b++) {
                vp = new VertexPair(vList.get(a), vList.get(b));
                vpList.add(vp);
                int longestWidth = 0;
                System.out.println(">Vertex Pair " + vList.get(a).name + "-" + vList.get(b).name + "\n All Paths:");
                vp.generateVertexDisjointPaths();
                for (int i = 0; i < vp.VertexDisjointContainer.size(); i++) {//for every container of the vertex pair
                    int width = vp.VertexDisjointContainer.get(i).size();
                    Collections.sort(vp.VertexDisjointContainer.get(i), new descendingWidthComparator());
                    int longestLength = vp.VertexDisjointContainer.get(i).firstElement().size();
                    longestWidth = Math.max(longestWidth, width);
                    System.out.println("\tContainer " + i + " - " + "Width=" + width + " - Length=" + longestLength);

                    for (int j = 0; j < vp.VertexDisjointContainer.get(i).size(); j++) //for every path in the container
                    {
                        System.out.print("\t\tPath " + j + "\n\t\t\t");
                        for (int k = 0; k < vp.VertexDisjointContainer.get(i).get(j).size(); k++) {
                            System.out.print("-" + vp.VertexDisjointContainer.get(i).get(j).get(k).name);
                        }
                        System.out.println();
                    }

                }
                //d-wide for vertexPair
                for (int k = 1; k <= longestWidth; k++) { // 1-wide, 2-wide, 3-wide...
                    int minLength = 999;
                    for (int m = 0; m < vp.VertexDisjointContainer.size(); m++) // for each container with k-wide select shortest length
                    {
                        minLength = Math.min(minLength, vp.VertexDisjointContainer.get(m).size());
                    }
                    if (minLength != 999) {
                        System.out.println(k + "-wide for vertexpair(" + vp.vertex1.name + "-" + vp.vertex2.name + ")=" + minLength);
                        kWideGraph[k] = Math.max(kWideGraph[k], minLength);
                    }
                }
            }
        }

        for (int i = 0; i < kWideGraph.length; i++) {
            if (kWideGraph[i] != -1) {
                System.out.println("D" + i + "(G)=" + kWideGraph[i]);
            }
        }


    }

    public void drawAdjacencyMatrix(Graphics g, Vector<Vertex> vList, int x, int y) {
        int cSize = 20;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y-30, vList.size() * cSize+cSize, vList.size() * cSize+cSize);
        g.setColor(Color.black);
        g.drawString("AdjacencyMatrix", x, y - cSize);
        for (int i = 0; i < vList.size(); i++) {
            g.setColor(Color.RED);
            g.drawString(vList.get(i).name, x + cSize + i * cSize, y);
            g.drawString(vList.get(i).name, x, cSize + i * cSize + y);
            g.setColor(Color.black);
            for (int j = 0; j < vList.size(); j++) {
                g.drawString("" + adjacencyMatrix[i][j], x + cSize * (j + 1), y + cSize * (i + 1));
            }
        }
    }

    public void drawDistanceMatrix(Graphics g, Vector<Vertex> vList, int x, int y) {
        int cSize = 20;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y-30, vList.size() * cSize+cSize, vList.size() * cSize+cSize);
        g.setColor(Color.black);
        g.drawString("ShortestPathMatrix", x, y - cSize);
        for (int i = 0; i < vList.size(); i++) {
            g.setColor(Color.RED);
            g.drawString(vList.get(i).name, x + cSize + i * cSize, y);
            g.drawString(vList.get(i).name, x, cSize + i * cSize + y);
            g.setColor(Color.black);
            for (int j = 0; j < vList.size(); j++) {
                g.drawString("" + distanceMatrix[i][j], x + cSize * (j + 1), y + cSize * (i + 1));
            }
        }
    }

    public Vector<Vertex> vertexConnectivity(Vector<Vertex> vList) {
        Vector<Vertex> origList = new Vector<Vertex>();
        Vector<Vertex> tempList = new Vector<Vertex>();
        Vector<Vertex> toBeRemoved = new Vector<Vertex>();
        Vertex victim;


        origList.setSize(vList.size());
        Collections.copy(origList, vList);

        int maxPossibleRemove = 0;
        while (graphConnectivity(origList)) {
            Collections.sort(origList, new ascendingDegreeComparator());
            maxPossibleRemove = origList.firstElement().getDegree();

            for (Vertex v : origList) {
                if (v.getDegree() == maxPossibleRemove) {
                    for (Vertex z : v.connectedVertices) {
                        if (!tempList.contains(z)) {
                            tempList.add(z);
                        }
                    }
                }
            }

            while (graphConnectivity(origList) && tempList.size() > 0) {
                Collections.sort(tempList, new descendingDegreeComparator());
                victim = tempList.firstElement();
                tempList.removeElementAt(0);
                origList.remove(victim);
                for (Vertex x : origList) {
                    x.connectedVertices.remove(victim);
                }
                toBeRemoved.add(victim);
            }
            tempList.removeAllElements();
        }

        return toBeRemoved;
    }

    private boolean graphConnectivity(Vector<Vertex> vList) {

        Vector<Vertex> visitedList = new Vector<Vertex>();

        recurseGraphConnectivity(vList.firstElement().connectedVertices, visitedList); //recursive function
        if (visitedList.size() != vList.size()) {
            return false;
        } else {
            return true;
        }
    }

    private void recurseGraphConnectivity(Vector<Vertex> vList, Vector<Vertex> visitedList) {
        for (Vertex v : vList) {
            {
                if (!visitedList.contains(v)) {
                    visitedList.add(v);
                    recurseGraphConnectivity(v.connectedVertices, visitedList);
                }
            }
        }
    }
    
    //--------------------------- NEW FUNCTIONS ---------------------------//
    public Vector<Vertex> getDegree (Vector<Vertex> vList, Vector<Edge> eList){
    	int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	int sum = 0;
    	for (Vertex v : vList) {
    		sum = 0;
    		for (int i = 0; i<vList.size(); i++) {
    			sum += matrix[Integer.parseInt(v.name)][i];
    		}
    		v.setDegree(sum);
    	}
    	return vList;
    }
    
    public Vector<Vertex> getInDegree (Vector<Vertex> vList, Vector<Edge> eList){
    	int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	int sum = 0;
    	for (Vertex v : vList) {
    		sum = 0;
    		for (int i = 0; i<vList.size(); i++) {
    			sum += matrix[i][Integer.parseInt(v.name)];
    		}
    		v.setInDegree(sum);
    	}
    	return vList;
    }
    
    public Vector<Vertex> getNormalizedDegree (Vector<Vertex> vList, Vector<Edge> eList) {
    	int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	float normalizedDegree = 0;
    	float degree = 0;
    	float denominator = vList.size() - 1;
    	for (Vertex v : vList) {
    		degree = 0;
    		for (int i = 0; i<vList.size(); i++) {
    			degree += matrix[Integer.parseInt(v.name)][i];
    		}
    		normalizedDegree = degree / denominator;
    		System.out.println("normalized " + normalizedDegree);
    		v.setNormalizedDegree(normalizedDegree);
    	}    	
    	return vList;
    }
    
    public Vector<Vertex> getCloseness (Vector<Vertex> vList, Vector<Edge> eList){
    	int[][] matrix = generateDistanceMatrix(vList);
    	float sum = 0;
    	float closeness = 0;
    	for (Vertex v : vList) {
    		sum = 0;
    		for (int i = 0; i<vList.size(); i++) {
    			sum += matrix[Integer.parseInt(v.name)][i];
    		}
    		closeness = 1 / sum ;					//closeness centrality
    		//closeness = (vList.size()-1) / sum ;  //normalized closeness centrality
    		//System.out.println("vList " + vList.size());
    		//System.out.println("sum " + sum);
    		//System.out.println("close " + closeness);
    		
    		v.setCloseness(closeness);
    	}
    	return vList;
    }
    
    public Vector<Vertex> getNormalizedCloseness (Vector<Vertex> vList, Vector<Edge> eList){
    	int[][] matrix = generateDistanceMatrix(vList);
    	float sum = 0;
    	float normalizedCloseness = 0;
    	for (Vertex v : vList) {
    		sum = 0;
    		for (int i = 0; i<vList.size(); i++) {
    			sum += matrix[Integer.parseInt(v.name)][i];    			
    		}
    		normalizedCloseness = (vList.size() - 1)  / sum;
    		v.setNormalizedCloseness(normalizedCloseness); 	
		}
    	return vList;
    }
    
    public float getCentralization (Vector<Vertex> vList, Vector<Edge> eList){
    	int[][] matrix = generateDistanceMatrix(vList);
    	float sum = 0;
    	float normalizedCloseness = 0;
    	float largest = 0;
    	float numerator = 0;
    	float denominator = 0;
    	float centralization = 0;
		int largestIndex = 0;
    	for (Vertex v : vList) {
    		sum = 0;
    		for (int i = 0; i<vList.size(); i++) {
    			sum += matrix[Integer.parseInt(v.name)][i];    			
    		}
    		normalizedCloseness = (vList.size() - 1)  / sum;
    		//v.setNormalizedCloseness(normalizedCloseness);
    		
    		if(normalizedCloseness > largest){
		        largest = normalizedCloseness;
		        largestIndex = Integer.parseInt(v.name);
		    }  
    	}	
		
		for (Vertex v : vList) {
			sum = 0;
			for (int i = 0; i<vList.size(); i++) {
    			sum += matrix[Integer.parseInt(v.name)][i];    			
    		}
			normalizedCloseness = (vList.size() - 1)  / sum;
			
			if(Integer.parseInt(v.name) != largestIndex) {
				numerator += largest - normalizedCloseness; 
			}
			denominator = ((vList.size() - 1)*(vList.size() - 2));
			centralization = numerator / denominator;
			v.setCentralization(centralization);
		}
		//System.out.println("Centralization= " + centralization);
    	return centralization;
    }
    
    public int getBetweenness(int[][] adjacencyMatrix, int except) {
    	
    	int[] path;
    	Vector<Integer> passed = new Vector<Integer>();
    	passed.add(except);
    	int value = 0;
    	for (int i = 0; i<adjacencyMatrix[0].length; i++) {
    		if (i!=except) {
    			path = dijkstra(adjacencyMatrix, i);
    			for (int j=i; j<path.length; j++) {
    				if (passed.contains(path[j])) {
    					value++;
    					passed.add(j);
    				}
    			}
    			passed = new Vector<Integer>();
    	    	passed.add(except);
    		}
    	}
    	return value;
    }
    
    public float getNormalizedBetweenness(int[][] adjacencyMatrix, int except) {
    	
    	int[] path;
    	Vector<Integer> passed = new Vector<Integer>();
    	passed.add(except);
    	int value = 0;
    	float denominator = (float) (adjacencyMatrix.length);
    	denominator = (denominator - 1) * ( denominator - 2) / 2;
    	for (int i = 0; i<adjacencyMatrix[0].length; i++) {
    		if (i!=except) {
    			path = dijkstra(adjacencyMatrix, i);
    			for (int j=i; j<path.length; j++) {
    				if (passed.contains(path[j])) {
    					value++;
    					passed.add(j);
    				}
    			}
    			passed = new Vector<Integer>();
    	    	passed.add(except);
    		}
    	}
    	float normalizedBetweenness = 0;
    		normalizedBetweenness = value / denominator;
    	return normalizedBetweenness;
    }
    
    public int[] dijkstra(int[][] adjacencyMatrix, int startVertex) { 
    	int nVertices = adjacencyMatrix[0].length;
    	int[] shortestDistances = new int[nVertices];
    	boolean[] added = new boolean[nVertices];
    	
    	for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) { 
    		shortestDistances[vertexIndex] = Integer.MAX_VALUE; 
    		added[vertexIndex] = false; 
    	} 
    	
    	shortestDistances[startVertex] = 0; 
    	int[] parents = new int[nVertices];
    	parents[startVertex] = -1;
    	
    	for (int i = 1; i < nVertices; i++) {
    		int nearestVertex = -1; 
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
            	if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance) {
            		nearestVertex = vertexIndex; 
                    shortestDistance = shortestDistances[vertexIndex];
            	}
            }
            added[nearestVertex] = true; 
            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
            	int edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];
            	if (edgeDistance > 0 && ((shortestDistance + edgeDistance) <  shortestDistances[vertexIndex])) {
            		parents[vertexIndex] = nearestVertex; 
                    shortestDistances[vertexIndex] = shortestDistance + edgeDistance; 
            	}
            }  
    	}
    	return parents;
    }
    
    public Vector<Vertex> getColorization(Vector<Vertex> vList, Vector<Edge> eList) {
    	int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	Vector<Integer> colored = new Vector<Integer>();
    	int numColor = 1;
    	vList.get(0).setColor(numColor);		//set color of first vertex
    	colored.add(0);
    	int found = 0;
    	
    	while (colored.size()<vList.size()) {
    		for (int i=1; i<vList.size(); i++) {
    			for (int n : colored) {
    				if (matrix[i][n]>0 && vList.get(n).color==numColor) {	//check if connected to colored
    					found++;
    				}
    			}
    			if (found==0) {
    				vList.get(i).setColor(numColor);
    				colored.add(i);
    			} else {
    				found = 0;
    			}
    		}
    		numColor++;
    		for (int j=1; j<vList.size(); j++) {
    			if (!colored.contains(j)) {
    				vList.get(j).setColor(numColor);
    				colored.add(j);
    				break;
    			}
    		}
    	}
    	
    	return vList;
    }
    
    public int getDistance(Vector<Vertex> vList, Vector<Edge> eList, int start, int end) {
    	 int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	 int[] path;
    	 int distance = 0;
    	 
    	 try {
    		 path = dijkstra(matrix, start);
//    		 System.out.println("Path");
//        	 for (int i=0; i<path.length; i++) {
//        		 System.out.print(path[i] + "|" );
//        	 }
        	 
        	 //get the weights
        	 
        	 int prevIndex = end;
        	 int nextIndex = path[prevIndex];
        	 
        	 System.out.println("Shortest Distance from " + start + "-" + end);
        	 while (nextIndex != -1) {
        		 if(prevIndex == nextIndex) {
 	       			return -1;
 	       		}
//        		 for (Edge e : eList) {
//        			 //System.out.println(e.vertex1.name + " : " + Integer.parseInt(e.vertex2.name));
//        			 if ((Integer.parseInt(e.vertex1.name) == prevIndex && Integer.parseInt(e.vertex2.name) == nextIndex) || (Integer.parseInt(e.vertex1.name) == nextIndex && Integer.parseInt(e.vertex2.name) == prevIndex)) {
//        				 distance += e.weight;
//        			 }
//        		 }
        		 System.out.println(prevIndex + " : " + nextIndex);
        		 distance += matrix[prevIndex][nextIndex];
        		 prevIndex = nextIndex;
        		 nextIndex = path[prevIndex];
        	 }
        	 System.out.println("distance : " + distance);
        	 return distance;
    	 } catch (Exception e) {
    		 System.out.println("Disconnected");
    		 return -1;
    	 }
     }
    
    public int getDistance(int[][] matrix, int start, int end) {
	   	 int[] path;
	   	 int distance = 0;
	   	 
	   	 try {
	   		 path = dijkstra(matrix, start);
	       	 
	       	 int prevIndex = end;
	       	 int nextIndex = path[prevIndex];
	       	 
	       	 System.out.println("Shortest Distance from " + start + "-" + end);
	       	 while (nextIndex != -1) {
	       		if(prevIndex == nextIndex) {
	       			return -1;
	       		}
	       		 System.out.println(prevIndex + " : " + nextIndex);
	       		 distance += matrix[prevIndex][nextIndex];
	       		 prevIndex = nextIndex;
	       		 nextIndex = path[prevIndex];
	       	 }
	       	 System.out.println("distance : " + distance);
	       	 return distance;
	   	 } catch (Exception e) {
	   		 System.out.println("Disconnected");
	   		 return -1;
	   	 }
    }
     
     public List<int[]> combination(int n, int r) {
    	 List<int[]> combinations = new ArrayList<>();
    	 int[] combination = new int[r];

    	 // initialize with lowest lexicographic combination
    	 for (int i = 0; i < r; i++) {
    		 combination[i] = i;
    	 }

    	 while (combination[r - 1] < n) {
    		 combinations.add(combination.clone());
    	
	    	 // generate next combination in lexicographic order
	    	 int t = r - 1;
	    	 while (t != 0 && combination[t] == n - r + t) {
	    		 t--;
	    	 }
	    	 combination[t]++;
	    	 for (int i = t + 1; i < r; i++) {
	    		 combination[i] = combination[i - 1] + 1;
	    	 }
    	 }
    	 return combinations;
     }
     
     public void printComb(List<int[]> mylist) {
    	 for (int[] elem : mylist) {
    		 System.out.print("[");
    		 for (int e : elem) {
    			 System.out.print(e + ", ");
    		 }
    		 System.out.println("]");
    	 }
     }
     
     public int getFaultDistance(Vector<Vertex> vList, Vector<Edge> eList, int start, int end, int w) {
    	 w = w-1;
    	 int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	 int maxDistance = 0;
    	 
    	 for(int p = w; p >= 0; p--) {
    		 List<int[]> includedList = combination(vList.size(), vList.size()-p);
        	 
        	 for (int[] elem : includedList) {
        		 int newEnd=0;
        		 int newStart=0;
        		 if (ifInclude(elem, start) && ifInclude(elem, end)) {
        			 int[][] tempMatrix = new int[vList.size()-p][vList.size()-p];
                	 System.out.println("length: " + elem.length);
                	 for (int i=0; i<elem.length; i++) {
                		 if (end == elem[i]) {
                			 newEnd = i;
                		 }
                		 if (start == elem[i]) {
                			 newStart = i;
                		 }
                		 for (int j=0; j<elem.length; j++) {
                			 tempMatrix[i][j] = matrix[elem[i]][elem[j]];
                		 }
                	 }
                	 if (maxDistance<getDistance(tempMatrix, newStart, newEnd)) {
                		 maxDistance = getDistance(tempMatrix, newStart, newEnd);
                	 }
        		 }
        	 }
    	 }
    	 
    	 
    	 return maxDistance;
     }
     
     public boolean ifInclude(int[] list, int x) {
    	 for (int e : list) {
    		 if (e == x) {
    			 return true;
    		 }
    	 }
    	 return false;
     }
     
     public void printMatrix (int[][] matrix) {
    	 for (int i=0; i<matrix[0].length; i++) {
    		 for (int j=0; j<matrix[0].length; j++) {
    			 System.out.print("|" + matrix[i][j]);
    		 }
    		 System.out.println();
    	 }
     }
    
    //////// MM 
    public float getDensity (Vector<Vertex> vList, Vector<Edge> eList) {  // formula for undirected graphs
    	int vNum = vList.size();
    	int eNum = eList.size();
    	int numerator = ( 2 * eNum );
    	int denominator = (vNum) * ( vNum - 1 );
    	float density = (float) numerator / denominator;
    	return density;
    }
    
    public int getFaultToleranceDiameter (Vector<Vertex> vList, Vector<Edge> eList, int w) {
//    	max fault distance of a graph given w
    	int faultToleranceDiameter = 0;
    	for(int i = 0; i < vList.size(); i++) {
    		for (int j = 0; j < vList.size(); j++) {
    			if (faultToleranceDiameter < getFaultDistance(vList, eList, i, j, w)) {
    				faultToleranceDiameter = getFaultDistance(vList, eList, i, j, w);
    			}
    		}
    	}
//    	System.out.println("faultDistance: " + faultToleranceDiameter);
    	return faultToleranceDiameter;
    }
    
    public int getDiameter (int[][] matrix) {
    	int diameter = 0;
    	int rowLen = matrix.length;
    	int colLen = matrix[0].length;
    	int currDistance;
    	
    	for (int i = 0; i < rowLen; i++) {
    		for (int j = 0; j < colLen; j++) {
				currDistance = getDistance(matrix, i, j);
    			if (currDistance == -1 ) {						//disconnected ang graph
    				return -1;
    			}
    			else if ( currDistance > diameter) {
    				diameter = currDistance;
    			}
    		}
    	}
    	
    	return diameter;
    }
    

    public int getFaultDiameter (Vector<Vertex> vList, Vector<Edge> eList, int w) {
    	int currDiameter = 0;
    	int faultDiameter = 0;
    	int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	
    	for (int p = w; p >= 0; p--) {
    		List<int[]> includedList = combination(vList.size(), vList.size()-p); 		
       	 	for (int[] elem : includedList) {
       	
       	 		System.out.println("Elem: ");
       	 		for(int content: elem) {
       	 			System.out.print(content);
       	 		}System.out.println();
       	 		
     			int[][] tempMatrix = new int[vList.size()-p][vList.size()-p];	//G-S
     			for (int i=0; i<elem.length; i++) {
     				for (int j=0; j<elem.length; j++) {
     					tempMatrix[i][j] = matrix[elem[i]][elem[j]];
     				}
     			}
//     			System.out.println("Matrix " + p);
//     			printMatrix(tempMatrix);
//     			System.out.println("Diameter: " + currDiameter);
//     			System.out.println();

     			currDiameter = getDiameter(tempMatrix);
     			if (faultDiameter < currDiameter) {
     				faultDiameter = currDiameter;
     			}
       	 	}
    	}
    	
    	return faultDiameter;
    }
    
    
   //////// MM 
    public boolean hasEulerianCircuit (Vector<Vertex> vList, Vector<Edge> eList) {
    	Vector<Vertex> degrees = getDegree(vList, eList);
    	int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	int edges = 0;
    	int odd = 0;
    	
    	for (int i = 0; i < degrees.size(); i++) {				//just in case magbigay si sir ng input na walang edges
    		edges = edges + degrees.get(i).getVertexDegree();
    	}
    	//System.out.println("edges = " + edges);
    	if (edges == 0) {
    		return true;
    	}
    	
    			
    	try {													//check if the graph is disconnected
    		int[] path = dijkstra(matrix, matrix[0][0]);
       	} catch (Exception e) {
       		//System.out.println("Disconnected");
       		return false;
       	}
    	
    	
    	for (int i = 0; i < degrees.size(); i++) {				//check if the degree of all vertices are even
    		if (degrees.get(i).getVertexDegree() % 2 != 0) {
    			odd++;
    		}
    	}
    	
    	return (odd == 0) ? true : false;   
    	
    }
    
    public boolean hasEulerianTrail (Vector<Vertex> vList, Vector<Edge> eList) {
    	Vector<Vertex> degrees = getDegree(vList, eList);
    	int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	int edges = 0;
    	int odd = 0;
    	
    	for (int i = 0; i < degrees.size(); i++) {
    		edges = edges + degrees.get(i).getVertexDegree();
    	}
    	//System.out.println("edges = " + edges);
    	if (edges == 0) {
    		return true;
    	}
    	
    	try {
    		int[] path = dijkstra(matrix, matrix[0][0]);
       	} catch (Exception e) {
       		//System.out.println("Disconnected");
       		return false;
       	}
    	
    	for (int i = 0; i < degrees.size(); i++) {
    		if (degrees.get(i).getVertexDegree() % 2 != 0) {
    			odd++;
    		}
    	}
    	
    	if (odd == 0) {
    		return true;
    	} else if (odd > 2) {
            return false; 
    	} else if (odd == 2) {
    		return true;
    	} else {
    		return false;
    	} 
    }
    
    public boolean hasHamiltonianPath (Vector<Vertex> vList) {
    	System.out.println("------CHECK FOR HAMILTONIAN PATHS------");
    	boolean hasHamiltonianPath = false;
    	VertexPair vp;
    	for (int a = 0; a < vList.size(); a++) {    // assign vertex pairs
            for (int b = 0; b < vList.size(); b++) {
            	if(b != a) {
	                vp = new VertexPair(vList.get(a), vList.get(b));
	                vpList.add(vp);
	                System.out.println(">Vertex Pair " + vList.get(a).name + "-" + vList.get(b).name + "\n All Paths:");
	                vp.generateVertexDisjointPaths();
	                for (int i = 0; i < vp.VertexDisjointContainer.size(); i++) {
	                    int longestLength = vp.VertexDisjointContainer.get(i).firstElement().size();
	                    if(longestLength == vList.size()) {
	                    	System.out.println("Length = " + longestLength);
	                    	//check if all vertices are unique
	                    	ArrayList<String> list=new ArrayList<String>();
	                    	list.add(vp.VertexDisjointContainer.get(i).get(0).get(0).name);
	                    	System.out.print("-" + vp.VertexDisjointContainer.get(i).get(0).get(0).name);
	                        hasHamiltonianPath = true;
	                    	for (int k = 1; k < vp.VertexDisjointContainer.get(i).get(0).size(); k++) {
	                        	if(!(list.contains(vp.VertexDisjointContainer.get(i).get(0).get(k).name))) {
	                        		list.add(vp.VertexDisjointContainer.get(i).get(0).get(k).name);
	                        	}
	                        	else {
	                        		hasHamiltonianPath = false;
	                        	}
	                        }
	                    	if(hasHamiltonianPath == true) {
	                    		System.out.println("\nHAMILTONIAN PATH");
	                    		for (int k = 0; k < vp.VertexDisjointContainer.get(i).get(0).size(); k++) {
		                        	System.out.print("-" + vp.VertexDisjointContainer.get(i).get(0).get(k).name);
		                        }
	                    	}
	                        System.out.println();
	                    }
	                }
            	}
            }
    	}
        return hasHamiltonianPath;
    	
    }

    //kruskal
    public Vector<Edge> generateMST(Vector<Vertex> vList, Vector<Edge> eList) {
    	//create temporary list
    	Vector<Edge> tempEList = new Vector<Edge>();
    	for (Edge e : eList) {
    		tempEList.add(e);
    	}
    	
    	// sort eList smallest to highest
    	sort("weight", tempEList);
    	
    	// generate the tree
    	Vector<Edge> tree = new Vector<Edge>();
    	int i = 0;
    	Vector<String> vertexAdded = new Vector<>();
    	while (vList.size()-1>tree.size()) {
    		tree.add(tempEList.get(i));
    		addVertex(vertexAdded, tempEList.get(i).vertex1.name);
    		addVertex(vertexAdded, tempEList.get(i).vertex2.name);
    		
    		//check if formed a cycle
    		if (vertexAdded.size()<=tree.size()) {
    			tree.remove(tree.size()-1);
    		}
    		i++;
    	}
    	return tree;
    }
    
    public void sort(final String field, Vector<Edge> itemLocationList) {
        Collections.sort(itemLocationList, new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
            	return Integer.toString(o1.weight).compareTo(Integer.toString(o2.weight));
            }           
        });
    }
    
    public void addVertex(Vector<String> list, String x) {
    	int found = 0;
    	for (String i : list) {
    		if (x.equals(i)) {
    			found++;
    		}
    	}
    	if (found == 0) {
    		list.add(x);
    	}
    }
    
    public void removeVertex(Vector<String> list, String x) {
    	int index = 0;
    	for (String i : list) {
    		if (x.equals(i)) {
    			list.remove(index);
    		}
    		index++;
    	}
    }
    
    public void printEdge(Vector<Edge> eList) {
    	for (Edge e : eList) {
    		System.out.println(e.vertex1.name + " : " + e.vertex2.name + " = " + e.weight);
    	}
    }
    
    //minimum vertex cover (brute force)
    //this function returns if a subset of vertex is a vertexcover
    public boolean isVertexCover (Vector<String> subset, Vector<Edge> eList, Vector<Vertex> vList) {
    	Vector<String> coveredList = new Vector<String>();
    	for (String v : subset) {
    		for (Edge e : eList) {
    			//check if the vertex in subset connected to an edge
    			if (e.vertex1.name.equals(v) || e.vertex2.name.equals(v)) {
    				//save all adjacent vertex
    				addVertex(coveredList, e.vertex1.name);
    				addVertex(coveredList, e.vertex2.name);
    			}
    		}
    	}
    	// if all vertex are there return true
    	if (coveredList.size() == vList.size()) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    //get the minimum vertex cover
    public Vector<String> getMinimumVertexCover (Vector<Edge> eList, Vector<Vertex> vList) {
    	//from 1 to N subsets
    	for (int i = 1; i<=vList.size(); i++) {
    		List<int[]> subsetList = new ArrayList<>();
    		//get all subset of size i
    		subsetList = combination(vList.size(), i);
    		//check for all subset if is vertex cover 
    		for (int[] subset : subsetList) {
    			Vector<String> temp = new Vector<String>();
    			temp = arrayToVector(subset);
    			//return the first vertex cover found
    			if (isVertexCover(temp, eList, vList)) {
    				return temp;
    			}
    		}
    	}
    	return null;
    }
    
    //save in the Vertex class if the vertex is part of the vertex cover
    public Vector<Vertex> generateMinimumVertexCover (Vector<Edge> eList, Vector<Vertex> vList) {
    	
    	Vector<String> mvc = new Vector<String>();
    	mvc = getMinimumVertexCover(eList, vList);
    	int counter = 0;
    	for (Vertex v : vList) {
    		counter = 0;
    		//if vertex is part of the vertex cover mark is vertex cover
    		for (String w : mvc) {
    			if (v.name.equals(w)) {
    				counter = 1;
//    				v.setVertexCover(true);
    			}
    		}
    		if (counter == 1) {
    			v.setVertexCover(true);
    		} else {
    			v.setVertexCover(false);
    		}
    	}
    	return vList;
    }
    
    public Vector<String> arrayToVector (int[] array) {
    	Vector<String> temp = new Vector<String>();
    	for (int i = 0; i<array.length; i++) {
    		temp.add(Integer.toString(array[i]));
    	}
    	return temp;
    }
    
    public int askW() {
    	int w;
    	String str1 = JOptionPane.showInputDialog("Enter value for w");
    	w = Integer.parseInt(str1);
    	return w;
    }
    
    public int askInput(String str) {	//ask user for vertex
    	int output;
    	String str1 = JOptionPane.showInputDialog("Enter value for " + str + ": ");
    	output = Integer.parseInt(str1);
    	return output;
    }
    
    public int displayFaultDistance(Vector<Vertex> vList, Vector<Edge> eList) {
    	int output, start, end, w;
    	w = askW();
    	System.out.println("Value of W: " + w);
    	start = askInput("starting vertex");
    	System.out.println("Value of X: " + start);
    	end = askInput("ending vertex");
    	System.out.println("Value of Y: " + end);
    	output = getFaultDistance(vList, eList, start, end, w);
    	
    	return output;
    }
    
    public int displayFaultToleranceDiameter(Vector<Vertex> vList, Vector<Edge> eList) {
    	int output, w;
    	w = askW();
    	output = getFaultToleranceDiameter(vList, eList, w);
    	return output;
    }
    
    public int displayFaultDiameter(Vector<Vertex> vList, Vector<Edge> eList) {
    	int output, w;
    	w = askW();
    	output = getFaultDiameter(vList, eList, w);
    	return output;
    }


    
    
    //---------------------------------------------------------------------//

    private class ascendingDegreeComparator implements Comparator {

        public int compare(Object v1, Object v2) {

            if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
                return 1;
            } else if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private class descendingDegreeComparator implements Comparator {

        public int compare(Object v1, Object v2) {

            if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
                return -1;
            } else if (((Vertex) v1).getDegree() > ((Vertex) v2).getDegree()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private class descendingWidthComparator implements Comparator {

        public int compare(Object v1, Object v2) {

            if (((Vector<Vertex>) v1).size() > (((Vector<Vertex>) v2).size())) {
                return -1;
            } else if (((Vector<Vertex>) v1).size() < (((Vector<Vertex>) v2).size())) {
                return 1;
            } else {
                return 0;
            }
        }
    }
    
    public Vector<Vertex> getEigenvector (Vector<Vertex> vList, Vector<Edge> eList){
    	int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	int sum = 0;
    	int[] degreeCentrality = new int[vList.size()]; 
    	for (Vertex v : vList) {
    		sum = 0;
    		for (int i = 0; i<vList.size(); i++) {
    			sum += matrix[Integer.parseInt(v.name)][i];
    		}
    		degreeCentrality[Integer.parseInt(v.name)] = sum;
    	}
    	
    	int eigenvectorCentrality = 0;
    	
    	for (Vertex v : vList) {
    		eigenvectorCentrality = 0;
    		for(int i = 0; i<vList.size(); i++) {
    			eigenvectorCentrality += (matrix[Integer.parseInt(v.name)][i]) * (degreeCentrality[i]);
    		}
    		v.setEigenvectorCentrality(eigenvectorCentrality);
    	}
    	
    	return vList;
    }
    
}
