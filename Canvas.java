package graphtheory;

/**
 *
 * @author mk
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class Canvas {

    public JFrame frame;
    private JMenuBar menuBar;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage,  canvasImage2;
    private int selectedTool;
    private int selectedWindow;
    private Dimension screenSize;
    public int width,  height;
    private int clickedVertexIndex;
    private int clickedEdgeIndex;
    private FileManager fileManager = new FileManager();
    
    public static boolean directedEdge[][] = new boolean[100][100];
    
    /////////////
    private Vector<Vertex> vertexList;
    private Vector<Edge> edgeList;
    private GraphProperties gP = new GraphProperties();
    /////////////

    public Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        frame.setTitle(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        canvas = new CanvasPane();
        InputListener inputListener = new InputListener();
        canvas.addMouseListener(inputListener);
        canvas.addMouseMotionListener(inputListener);
        frame.setContentPane(canvas);

        this.width = width;
        this.height = height;
        canvas.setPreferredSize(new Dimension(width, height));

        //events
        menuBar = new JMenuBar();
        JMenu menuOptions = new JMenu("Tools");
        JMenu menuOptions1 = new JMenu("File");
        JMenu menuOptions2 = new JMenu("Extras");
        JMenu menuOptions3 = new JMenu("Window");
        JMenu menuOptions4 = new JMenu("Centrality");

        JMenuItem item = new JMenuItem("Add Vertex");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        item.addActionListener(new MenuListener());
        menuOptions.add(item);
        item = new JMenuItem("Open File");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        item.addActionListener(new MenuListener());
        menuOptions1.add(item);
        item = new JMenuItem("Save to File");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        item.addActionListener(new MenuListener());
        menuOptions1.add(item);
        item = new JMenuItem("Add Edges");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        item.addActionListener(new MenuListener());
        menuOptions.add(item);
        item = new JMenuItem("Add Directed Edges");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
        item.addActionListener(new MenuListener());
        menuOptions.add(item);
        item = new JMenuItem("Grab Tool");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK));
        item.addActionListener(new MenuListener());
        menuOptions.add(item);
        item = new JMenuItem("Remove Tool");
        item.addActionListener(new MenuListener());
        item.setEnabled(false);
        menuOptions.add(item);
        item = new JMenuItem("Auto Arrange Vertices");
        item.addActionListener(new MenuListener());

        menuOptions2.add(item);
        item = new JMenuItem("Remove All");
        item.addActionListener(new MenuListener());
        menuOptions2.add(item);

        item = new JMenuItem("Graph");
        item.addActionListener(new MenuListener());
        menuOptions3.add(item);
        item = new JMenuItem("Properties");
        item.addActionListener(new MenuListener());
        menuOptions3.add(item);
        
        item = new JMenuItem("(Out) Degree");
        item.addActionListener(new MenuListener());
        menuOptions4.add(item);
        item = new JMenuItem("Normalized Degree");
        item.addActionListener(new MenuListener());
        menuOptions4.add(item);
        
        item = new JMenuItem("Closeness");
        item.addActionListener(new MenuListener());
        menuOptions4.add(item);
        item = new JMenuItem("Normalized Closeness");
        item.addActionListener(new MenuListener());
        menuOptions4.add(item);
        item = new JMenuItem("Betweenness");
        item.addActionListener(new MenuListener());
        menuOptions4.add(item);
        item = new JMenuItem("Normalized Betweenness");
        item.addActionListener(new MenuListener());
        menuOptions4.add(item);
        
        item = new JMenuItem("Color");
        item.addActionListener(new MenuListener());
        menuOptions4.add(item);
        

        menuBar.add(menuOptions1);
        menuBar.add(menuOptions);
        menuBar.add(menuOptions2);
        menuBar.add(menuOptions3);
        menuBar.add(menuOptions4);

        frame.setJMenuBar(menuBar);

        backgroundColour = bgColour;

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(screenSize.width / 2 - width / 2, screenSize.height / 2 - height / 2, width, height);
        frame.pack();
        setVisible(true);

        vertexList = new Vector<Vertex>();
        edgeList = new Vector<Edge>();

    }

    class InputListener implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            if (selectedWindow == 0) {
                switch (selectedTool) {
                    case 1: {
                        Vertex v = new Vertex("" + vertexList.size(), e.getX(), e.getY());
                        vertexList.add(v);
                        v.draw(graphic);
                        //System.out.print(vertexList.size());
                        break;
                    }
                    case 4: {

                        /* for (Vertex v : vertexList) {
                        if (v.hasIntersection(e.getX(), e.getY())) {
                        {
                        for (Edge d : edgeList) {
                        if (d.vertex1 == v || d.vertex2 == v) {
                        edgeList.remove(d);
                        }
                        }
                        for (Vertex x : vertexList) {
                        if (x.connectedToVertex(v)) {
                        x.connectedVertices.remove(v);
                        }
                        }
                        vertexList.remove(v);
                        }
                        }
                        }*/ break;
                    }
                }
            //refresh();
            }


        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (selectedWindow == 0 && vertexList.size() > 0) {
                switch (selectedTool) {
                    case 2: {
                        for (Vertex v : vertexList) {
                            if (v.hasIntersection(e.getX(), e.getY())) {
                                v.wasClicked = true;
                                clickedVertexIndex = vertexList.indexOf(v);
                            } else {
                                v.wasClicked = false;
                            }
                        }
                        break;
                    }
                    case 3: {
 
                        for (Edge d : edgeList) {
                            if (d.hasIntersection(e.getX(), e.getY())) {
                                d.wasClicked = true;
                                clickedEdgeIndex = edgeList.indexOf(d);
                            } else {
                                d.wasClicked = false;
                            }
                        }
                        for (Vertex v : vertexList) {
                            if (v.hasIntersection(e.getX(), e.getY())) {
                                v.wasClicked = true;
                                clickedVertexIndex = vertexList.indexOf(v);
//                                gP.getDistance(vertexList, edgeList, clickedVertexIndex, 0);
//                                gP.printComb(gP.combination(3, 2));
//                                System.out.println("Fault Distance: " + gP.getFaultDistance(vertexList, edgeList, clickedVertexIndex, 1, 1));
                                //System.out.println(vertexList.get(clickedVertexIndex).getDegree());	//print the degree to console
                            } else {
                                v.wasClicked = false;
                            }
                        }
                        break;
                    }
                    case 4: {

                        for (Edge d : edgeList) {
                            if (d.hasIntersection(e.getX(), e.getY())) {
                                d.wasClicked = true;
                                clickedEdgeIndex = edgeList.indexOf(d);
                            } else {
                                d.wasClicked = false;
                            }
                        }
                        for (Vertex v : vertexList) {
                            if (v.hasIntersection(e.getX(), e.getY())) {
                                v.wasClicked = true;
                                clickedVertexIndex = vertexList.indexOf(v);
                                System.out.println(vertexList.get(clickedVertexIndex).getCloseness()); //print closeness to console
                            } else {
                                v.wasClicked = false;
                            }
                        }
                        break;
                    }
                    case 5: {
                    	for (Vertex v : vertexList) {
                            if (v.hasIntersection(e.getX(), e.getY())) {
                                v.wasClicked = true;
                                clickedVertexIndex = vertexList.indexOf(v);
                            } else {
                                v.wasClicked = false;
                            }
                        }
                        break;
                    }
                }
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (selectedWindow == 0 && vertexList.size() > 0) {
                switch (selectedTool) {
                    case 2: {
                        Vertex parentV = vertexList.get(clickedVertexIndex);
                        for (Vertex v : vertexList) {
                            if (v.hasIntersection(e.getX(), e.getY()) && v != parentV && !v.connectedToVertex(parentV)) {              //System.out.println(clickedVertexIndex+" "+vertexList.indexOf(v));
                                Edge edge = new Edge(v, parentV);
                                v.addVertex(parentV);
                                parentV.addVertex(v);
                                v.wasClicked = false;
                                parentV.wasClicked = false;
                                edgeList.add(edge);
                                edge.askWeight();
                                
                                int x = Integer.parseInt(parentV.name);
                                int y = Integer.parseInt(v.name);
                                directedEdge[x][y] = false;
                            } else {
                                v.wasClicked = false;
                            }
                        }
                        break;
                    }
                    case 3: {
                        vertexList.get(clickedVertexIndex).wasClicked = false;
                        break;
                    }
                    case 4: {
                        vertexList.get(clickedVertexIndex).wasClicked = false;
                        break;
                    }
                    case 5: {
                    	Vertex parentV = vertexList.get(clickedVertexIndex);
                        for (Vertex v : vertexList) {
                            if (v.hasIntersection(e.getX(), e.getY()) && v != parentV && !v.connectedToVertex(parentV)) {              //System.out.println(clickedVertexIndex+" "+vertexList.indexOf(v));
                                Edge edge = new Edge(v, parentV);
                                v.addVertex(parentV);
                                parentV.addVertex(v);
                                v.wasClicked = false;
                                parentV.wasClicked = false;
                                edge.directed = 1;
                                edgeList.add(edge);
                                edge.askWeight();
                                
                                int x = Integer.parseInt(parentV.name);
                                int y = Integer.parseInt(v.name);
                                directedEdge[x][y] = true;
                                
                            } else {
                                v.wasClicked = false;
                            }
                        }
                        break;
                    }
                }
            }
            erase();
            refresh();
        }
        
        private final int ARR_SIZE = 4;

        void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        	Graphics2D g = (Graphics2D) g1.create();

            double dx = x2 - x1, dy = y2 - y1;
            double angle = Math.atan2(dy, dx);
            int len = (int) Math.sqrt(dx*dx + dy*dy);
            AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
            at.concatenate(AffineTransform.getRotateInstance(angle));
            g.transform(at);

            // Draw horizontal arrow starting in (0, 0)
            g.drawLine(0, 0, len, 0);
            g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                          new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            if (selectedWindow == 0 && vertexList.size() > 0) {
                erase();
                switch (selectedTool) {
                    case 2: {
                        graphic.setColor(Color.RED);
                        drawLine(vertexList.get(clickedVertexIndex).location.x, vertexList.get(clickedVertexIndex).location.y, e.getX(), e.getY());
                        break;

                    }
                    case 3: {
                        if (vertexList.get(clickedVertexIndex).wasClicked) {
                            vertexList.get(clickedVertexIndex).location.x = e.getX();
                            vertexList.get(clickedVertexIndex).location.y = e.getY();
                        }
                        break;
                    }
                    case 4: {
                        if (vertexList.get(clickedVertexIndex).wasClicked) {
                            vertexList.get(clickedVertexIndex).location.x = e.getX();
                            vertexList.get(clickedVertexIndex).location.y = e.getY();
                        }
                        break;
                    }
                    case 5: {
                    	graphic.setColor(Color.RED);
                    	
                    	drawLine(vertexList.get(clickedVertexIndex).location.x, vertexList.get(clickedVertexIndex).location.y, e.getX(), e.getY());
                    	drawArrow(graphic, vertexList.get(clickedVertexIndex).location.x, vertexList.get(clickedVertexIndex).location.y, e.getX(), e.getY());
                        
                        break;
                    }
                }
                refresh();
            }

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (selectedWindow == 0) {
                for (Edge d : edgeList) {
                    if (d.hasIntersection(e.getX(), e.getY())) {
                        d.wasFocused = true;
                    } else {
                        d.wasFocused = false;
                    }
                }
                for (Vertex v : vertexList) {
                    if (v.hasIntersection(e.getX(), e.getY())) {
                        v.wasFocused = true;
                    } else {
                        v.wasFocused = false;
                    }
                }
                refresh();
            }

        }
    }

    class MenuListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Add Vertex")) {
                selectedTool = 1;
            } else if (command.equals("Add Edges")) {
                selectedTool = 2;
            } else if (command.equals("Grab Tool")) {
                selectedTool = 3;
            } else if (command.equals("Remove Tool")) {
                selectedTool = 4;  
            } else if (command.equals("Add Directed Edges")) {
                selectedTool = 5;
            } else if (command.equals("Auto Arrange Vertices")) {
                arrangeVertices();
                erase();
            } else if (command.equals("Remove All")) {
                edgeList.removeAllElements();
                vertexList.removeAllElements();
                clickedVertexIndex = 0;
                erase();
            } else if (command.equals("Open File")) {
                int returnValue = fileManager.jF.showOpenDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    loadFile(fileManager.loadFile(fileManager.jF.getSelectedFile()));
                    System.out.println(fileManager.jF.getSelectedFile());
                    selectedWindow=0;
                }
            } else if (command.equals("Save to File")) {
                int returnValue = fileManager.jF.showSaveDialog(frame);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    fileManager.saveFile(vertexList, edgeList, fileManager.jF.getSelectedFile());
                    System.out.println(fileManager.jF.getSelectedFile());
                }
            } else if (command.equals("Graph")) {
                selectedWindow = 0;
                erase();
            } else if (command.equals("Properties")) {
                selectedWindow = 1;
                if (vertexList.size() > 0) {
                    //adjacency list
                    int[][] matrix = gP.generateAdjacencyMatrix(vertexList, edgeList);

                    //connectivity
                    Vector<Vertex> tempList = gP.vertexConnectivity(vertexList);
                    for (Vertex v : tempList) {
                        vertexList.get(vertexList.indexOf(v)).wasClicked = true;
                    }
                    reloadVertexConnections(matrix, vertexList);

                    //distance
                    gP.generateDistanceMatrix(vertexList);

                    //VD paths
                    gP.displayContainers(vertexList);
                //gP.drawNWideDiameter();
                }
                erase();
            } else if (command.equals("(Out) Degree")) {
            	selectedWindow = 3;
            	erase();
            } else if (command.equals("Closeness")) {
            	selectedWindow = 4;
            	erase();
            } else if (command.equals("Betweenness")) {
            	selectedWindow = 5;
            	erase();
            } else if (command.equals("Color")) {
            	selectedWindow = 6;
            	erase();
            } else if (command.equals("Normalized Betweenness")) {
            	selectedWindow = 7;
            	erase();
            } else if (command.equals("Normalized Degree")) {
            	selectedWindow = 8;
            	erase();
            } else if (command.equals("Normalized Closeness")) {
            	selectedWindow = 9;
            	erase();
            }
            

            refresh();
        }
    }

    private void arrangeVertices() {
        double deg2rad = Math.PI / 180;
        double radius = height / 5;
        double centerX = width / 2;
        double centerY = height / 2;
        int interval = 360 / vertexList.size();


        for (int i = 0; i < vertexList.size(); i++) {
            double degInRad = i * deg2rad * interval;
            double x = centerX + (Math.cos(degInRad) * radius);
            double y = centerY + (Math.sin(degInRad) * radius);
            int X = (int) x;
            int Y = (int) y;
            vertexList.get(i).location.x = X;
            vertexList.get(i).location.y = Y;
        }

    }

    private void reloadVertexConnections(int[][] aMatrix, Vector<Vertex> vList) {
        for (Vertex v : vList) {
            v.connectedVertices.clear();
        }

        for (int i = 0; i < aMatrix.length; i++) {
            for (int j = 0; j < aMatrix.length; j++) {
                if (aMatrix[i][j] == 1) {
                    vList.get(i).addVertex(vList.get(j));
                }
            }
        }

    }

    private void loadFile(Vector<Vector> File) {
        vertexList = File.firstElement();
        edgeList = File.lastElement();
        erase();
    }

    public void refresh() {
        for (Edge e : edgeList) {
            e.draw(graphic);
        }
        for (Vertex v : vertexList) {
            v.draw(graphic);
        }

        canvas.repaint();
    }

    public void setVisible(boolean visible) {
        if (graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            canvasImage2 = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    public boolean isVisible() {
        return frame.isVisible();
    }

    public void erase() {
        graphic.clearRect(0, 0, width, height);
    }

    public void erase(int x, int y, int x1, int y2) {
        graphic.clearRect(x, y, x1, y2);
    }

    public void drawString(String text, int x, int y, float size) {
        Font orig = graphic.getFont();
        graphic.setFont(graphic.getFont().deriveFont(1, size));
        graphic.drawString(text, x, y);
        graphic.setFont(orig);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        graphic.drawLine(x1, y1, x2, y2);
    }

    private class CanvasPane extends JPanel {

        public void paint(Graphics g) {
            switch (selectedWindow) {
                case 0: {   //graph window
                    graphic.drawString("Vertex Count=" + vertexList.size() +
                            "  Edge Count=" + edgeList.size() +
                            "  Selected Tool=" + selectedTool + 
                    		" Density=" + gP.getDensity(vertexList, edgeList) +
                            " Centralization=" + gP.getCentralization(vertexList, edgeList), 50, height / 2 + (height * 2) / 5);
                    g.drawImage(canvasImage, 0, 0, null); //layer 1
                    g.setColor(Color.black);
                    break;
                }
                case 1: {   //properties window
                    canvasImage2.getGraphics().clearRect(0, 0, width, height); //clear
                    gP.drawAdjacencyMatrix(canvasImage2.getGraphics(), vertexList, width / 2 + 50, 50);//draw adjacency matrix
                    gP.drawDistanceMatrix(canvasImage2.getGraphics(), vertexList, width / 2 + 50, height / 2 + 50);//draw distance matrix
                    g.drawImage(canvasImage2, 0, 0, null); //layer 1
                    drawString("Graph disconnects when nodes in color red are removed.", 100, height - 30, 20);
                    g.drawString("See output console for Diameter of Graph", 100, height / 2 + 50);
                    g.drawImage(canvasImage.getScaledInstance(width / 2, height / 2, Image.SCALE_SMOOTH), 0, 0, null); //layer 1
                    g.draw3DRect(0, 0, width / 2, height / 2, true);
                    g.setColor(Color.black);
                    
                    vertexList = gP.getColorization(vertexList, edgeList);
                    System.out.println("-------COLORIZATION------");
                    for (Vertex v : vertexList) {
                    	System.out.println(v.name + " : " + v.color);
                    }
                    break;
                }
                case 3: {				//print the vertex as degree
                	vertexList = gP.getDegree(vertexList, edgeList);
                	for (Edge e : edgeList) {
                        e.draw(g);
                    }
                    for (Vertex v : vertexList) {
                        v.drawDegree(g);
                    }
                    
                    break;
                }
                case 4: {				//print the vertex as closeness degree
                	vertexList = gP.getCloseness(vertexList, edgeList);
                	for (Edge e : edgeList) {
                        e.draw(g);
                    }
                    for (Vertex v : vertexList) {
                        v.drawCloseness(g);
                    }
                    
                    break;
                }
                case 5: {
                	for (Vertex v : vertexList) {
                    	v.setBetweenness(gP.getBetweenness(gP.generateAdjacencyMatrix(vertexList, edgeList), Integer.parseInt(v.name)));
                    }
                	for (Edge e : edgeList) {
                        e.draw(g);
                    }
                    for (Vertex v : vertexList) {
                        v.drawBetweenness(g);
                    }
                    
                    break;
                }
                case 6: {
                	vertexList = gP.getColorization(vertexList, edgeList);
                	for (Edge e : edgeList) {
                        e.draw(g);
                    }
                    for (Vertex v : vertexList) {
                        v.drawColor(g);
                    }
                    
                    break;
                }
                case 7: {
                	for (Vertex v : vertexList) {
                    	v.setNormalizedBetweenness(gP.getNormalizedBetweenness(gP.generateAdjacencyMatrix(vertexList, edgeList), Integer.parseInt(v.name)));
                    }
                	for (Edge e : edgeList) {
                        e.draw(g);
                    }
                    for (Vertex v : vertexList) {
                        v.drawNormalizedBetweenness(g);
                    }
                    
                    break;
                }
                case 8: {
                	vertexList = gP.getNormalizedDegree(vertexList, edgeList);
                	for (Edge e : edgeList) {
                        e.draw(g);
                    }
                    for (Vertex v : vertexList) {
                        v.drawNormalizedDegree(g);
                    }
                    
                    break;
                }
                case 9: {				//print the vertex as normalized closeness degree
                	vertexList = gP.getNormalizedCloseness(vertexList, edgeList);
                	for (Edge e : edgeList) {
                        e.draw(g);
                    }
                    for (Vertex v : vertexList) {
                        v.drawNormalizedCloseness(g);
                    }
                    
                    break;
                }
            }

        }
    }
}
