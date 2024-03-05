package org.graphTheory;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import javax.swing.*;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.ext.JGraphXAdapter;
import com.mxgraph.util.mxPoint;

public class VehicleLifecycleGraph {

    private Graph<String, DefaultEdge> vehicleGraph;
    private JGraphXAdapter<String, DefaultEdge> graphAdapter;

    public VehicleLifecycleGraph() {
        vehicleGraph = new SimpleGraph<>(DefaultEdge.class);

        // Add vertices for blockchain blocks
        addVertex("Block0: Genesis");
        addVertex("Block1: HashABC123");
        addVertex("Block2: HashXYZ789");
        addVertex("Block3: HashDEF456");

        // Add other vertices related to vehicle management
        addVertex("Vehicle1");
        addVertex("Owner1");
        addVertex("ServiceStation1");
        addVertex("Part1");

        // Add edges to demonstrate blockchain linking
        vehicleGraph.addEdge("Block0: Genesis", "Block1: HashABC123");
        vehicleGraph.addEdge("Block1: HashABC123", "Block2: HashXYZ789");
        vehicleGraph.addEdge("Block2: HashXYZ789", "Block3: HashDEF456");

        // Add edges between blockchain blocks and vehicle management entities
        vehicleGraph.addEdge("Block1: HashABC123", "Vehicle1"); // Link a transaction to a vehicle
        vehicleGraph.addEdge("Block2: HashXYZ789", "ServiceStation1"); // Link a service record
        vehicleGraph.addEdge("Block3: HashDEF456", "Part1"); // Link a part replacement

        // Add edges representing vehicle management relationships
        vehicleGraph.addEdge("Vehicle1", "Owner1");
        vehicleGraph.addEdge("Vehicle1", "ServiceStation1");
        vehicleGraph.addEdge("ServiceStation1", "Part1");

        graphAdapter = new JGraphXAdapter<>(vehicleGraph);
    }

    private void addVertex(String id) {
        vehicleGraph.addVertex(id);
    }

    public void visualize() {
        mxGraphComponent component = new mxGraphComponent(graphAdapter);
        component.setConnectable(false);
        component.getGraph().setAllowDanglingEdges(false);
        component.getGraph().setCellsEditable(false);
        component.setToolTips(true);

        JFrame frame = new JFrame("Vehicle Lifecycle Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(component);

        applyGraphLayout(graphAdapter);
        customizeGraphAppearance();

        frame.setVisible(true);
    }

    private void applyGraphLayout(JGraphXAdapter<String, DefaultEdge> graphAdapter) {
        mxGraph graph = graphAdapter;
        mxCircleLayout layout = new mxCircleLayout(graph);

        graph.getModel().beginUpdate();
        try {
            layout.execute(graph.getDefaultParent());
        } finally {
            graph.getModel().endUpdate();
        }

        centerGraph(graph, 800, 600);
    }

    private void centerGraph(mxGraph graph, int frameWidth, int frameHeight) {
        double graphCenterX = (graph.getGraphBounds().getX() + graph.getGraphBounds().getWidth() / 2.0);
        double graphCenterY = (graph.getGraphBounds().getY() + graph.getGraphBounds().getHeight() / 2.0);
        double offsetX = (frameWidth / 2.0) - graphCenterX;
        double offsetY = (frameHeight / 2.0) - graphCenterY;
        graph.getView().setTranslate(new mxPoint(offsetX, offsetY));
    }

    private void customizeGraphAppearance() {
        for (String vertex : vehicleGraph.vertexSet()) {
            var cell = graphAdapter.getVertexToCellMap().get(vertex);
            graphAdapter.setCellStyle("fillColor=lightblue;shape=ellipse;fontSize=16;", new Object[]{cell});
        }

        for (DefaultEdge edge : vehicleGraph.edgeSet()) {
            var cell = graphAdapter.getEdgeToCellMap().get(edge);
            graphAdapter.setCellStyle("strokeWidth=2;strokeColor=black;fontSize=12;", new Object[]{cell});
        }
    }

    public static void main(String[] args) {
        VehicleLifecycleGraph vlg = new VehicleLifecycleGraph();
        vlg.visualize();
    }
}
