/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphtheory;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author mk
 */
public class GraphProperties {

    public int[][] adjacencyMatrix;
    public int[][] distanceMatrix;
    public Vector<VertexPair> vpList;

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
    
    public Vector<Vertex> getCloseness (Vector<Vertex> vList, Vector<Edge> eList){
    	int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	float sum = 0;
    	float closeness = 0;
    	for (Vertex v : vList) {
    		sum = 0;
    		for (int i = 0; i<vList.size(); i++) {
    			sum += matrix[Integer.parseInt(v.name)][i];
    		}
    		closeness = vList.size() / sum ;
    		//System.out.println("vList" + vList.size());
    		//System.out.println("sum" + sum);
    		//System.out.println("close " + closeness);
    		
    		v.setCloseness(closeness);
    	}
    	return vList;
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
    	 int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	 int[] includedVertex = new int[vList.size()-w];
    	 int maxDistance = 0;
    	 includedVertex[0]=2;
    	 includedVertex[1]=1;
    	 includedVertex[2]=3;
    	 
    	 // generate the combination of vertices 
    	 List<int[]> includedList = combination(vList.size(), vList.size()-w);
    	 
    	 for (int[] elem : includedList) {
    		 int newEnd=0;
    		 int newStart=0;
    		 if (ifInclude(elem, start) && ifInclude(elem, end)) {
    			 int[][] tempMatrix = new int[vList.size()-w][vList.size()-w];
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
    	 
    	 
//    	 int[][] tempMatrix = new int[vList.size()-w][vList.size()-w];
//    	 System.out.println("length: " + includedVertex.length);
//    	 for (int i=0; i<includedVertex.length; i++) {
//    		 if (end == includedVertex[i]) {
//    			 end = i;
//    		 }
//    		 for (int j=0; j<includedVertex.length; j++) {
//    			 tempMatrix[i][j] = matrix[includedVertex[i]][includedVertex[j]];
//    		 }
//    	 }
//    	 printMatrix(tempMatrix);
//    	 
//    	 getDistance(tempMatrix, start, end);
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
    
    public int getDiameter (int[][] matrix) {
    	int diameter = 0;
    	int rowLen = matrix.length;
    	int colLen = matrix[0].length;
    	//(i,j) will be your vertex pair
    	for (int i = 0; i < rowLen; i++) {
    		for (int j = 0; j < colLen; j++) {
    			int currDistance = getDistance(matrix, i, j);
    			if ( currDistance > diameter) {
    				diameter = currDistance;
    			}
    		}
    	}
    	
    	return diameter;
    }

    public int getFaultDiameter (Vector<Vertex> vList, Vector<Edge> eList, int w) {
    	int faultDiameter = 0;
    	int[][] matrix = generateAdjacencyMatrix(vList, eList);
    	Vector<Vertex> tempVlist = new Vector<Vertex>();
    	Vector<Edge> tempElist = new Vector<Edge>();
    	
    	List<int[]> includedList = combination(vList.size(), vList.size()-w);
      	 
   	 	for (int[] elem : includedList) {
   	 		//check if connected ang graph
   	 		
 			int[][] tempMatrix = new int[vList.size()-w][vList.size()-w];	//G-S
 			System.out.println("length: " + elem.length);
 			for (int i=0; i<elem.length; i++) {
 				for (int j=0; j<elem.length; j++) {
 					tempMatrix[i][j] = matrix[elem[i]][elem[j]];
 				}
	 		}
 			if (faultDiameter < getDiameter(tempMatrix)) {
 				faultDiameter = getDiameter(tempMatrix);
 			}
   	 	}
   	 	
    	
    	return faultDiameter;
    }
    
   //////// MM 
    
    
    
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
    
}
