package com.graph_coloring.graph_coloring;

import com.graph_coloring.ColoringBkt;
import com.graph_coloring.ColoringGreedy;
import com.graph_coloring.genetic_algo.ColoringGA;
import com.graph_coloring.wrapper.VisualEdge;
import com.graph_coloring.wrapper.VisualGraph;
import com.graph_coloring.wrapper.VisualVertex;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelloController {
    VisualGraph graph;

    @FXML
    Pane graphPane;
    @FXML
    RadioButton addVertexButton;
    @FXML
    RadioButton addEdgeButton;
    @FXML
    RadioButton removeVertexButton;
    @FXML
    RadioButton removeEdgeButton;

    @FXML
    Button startAlgoGa;

    @FXML
    Button startAlgoGreedy;

    @FXML
    Button startAlgoBkt;

    @FXML
    Button removeGraph;

    @FXML
    ChoiceBox existingGraphSelector;
    @FXML
    Button loadExistingGraph;

    @FXML
    public void initialize() {
        graph = new VisualGraph(graphPane);

        RadioButtonEvent(addVertexButton, addEdgeButton, removeVertexButton, addEdgeButton, removeEdgeButton);
        RadioButtonEvent(removeVertexButton, addVertexButton, addEdgeButton, removeEdgeButton, removeVertexButton);

        Set<String> existingGraphs = Stream.of(new File("../graph_coloring/test_instances").listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
        existingGraphs.forEach(name -> {
            existingGraphSelector.getItems().add(name);
        });

        startAlgoGa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    startGA();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        startAlgoGreedy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    startGreedy();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        startAlgoBkt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    startBkt();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        removeGraph.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                graph.removeAll();
                graphPane.getChildren().clear();
            }
        });

        loadExistingGraph.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                graph.removeAll();
                graphPane.getChildren().clear();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader("../graph_coloring/test_instances/" + (String)existingGraphSelector.getValue()));
                    String line = reader.readLine();

                    while (line != null) {
                        if(line.charAt(0) == 'p') {
                            String[] parts = line.split(" ");
                            int numOfNodes = Integer.parseInt(parts[2]);
                            for(int i = 0; i < numOfNodes; i++) {
                                graph.addVertex(new VisualVertex(0, 0));
                            }
                        }
                        if(line.charAt(0) == 'e') {
                            try {
                                String[] parts = line.split(" ");
                                int fNode = Integer.parseInt(parts[1]);
                                int sNode = Integer.parseInt(parts[2]);
                                graph.addEdge(new VisualEdge(
                                        graph.getVertexes().get(fNode-1),
                                        graph.getVertexes().get(sNode-1)
                                ), fNode-1, sNode-1);
                            }
                            catch (Exception ex) { }
                        }
                        // read next line
                        line = reader.readLine();
                    }

                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                graphPane.getChildren().add(new TextArea());
                ((TextArea)graphPane.getChildren().get(0))
                        .appendText("Graph loaded... " + graph.getVertexes().size() + " nodes and " + graph.getEdges().size() + " edges.");
            }
        });

        initializeEvents();
    }

    private void RadioButtonEvent(RadioButton addVertexButton, RadioButton addEdgeButton, RadioButton removeVertexButton, RadioButton addEdgeButton2, RadioButton removeEdgeButton) {
        addVertexButton.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(t1) {
                addEdgeButton.setSelected(false);
                removeVertexButton.setSelected(false);
                removeEdgeButton.setSelected(false);
            }
        });
        addEdgeButton2.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if(t1) {
                addVertexButton.setSelected(false);
                removeVertexButton.setSelected(false);
                removeEdgeButton.setSelected(false);
            }
        });
    }

    private void initializeEvents() {
        graphPane.setOnMouseClicked(event -> {
            if(addVertexButton.isSelected()) {
                double X = event.getX();
                double Y = event.getY();

                // TODO Make sure we don't have a vertex there already (in the case of double clicking on the same point)
                VisualVertex vertex = new VisualVertex(X, Y);
                graph.addVertex(vertex);
                graphPane.getChildren().add(vertex.getCircle());
            }
            else if(removeVertexButton.isSelected()) {
                double X = event.getX();
                double Y = event.getY();

                VisualVertex vertex = graph.findClosestVertex(X, Y);
                if(vertex != null) {
                    graphPane.getChildren().remove(vertex.getCircle());
                    graph.removeVertex(vertex);
                }
            }
            else if(addEdgeButton.isSelected()) {
                double X = event.getX();
                double Y = event.getY();

                VisualVertex v1 = graph.findClosestVertex(X, Y);
                if(v1 != null) {
                    v1.getCircle().setFill(Color.BLUE);
                    graphPane.setOnMouseClicked(event1 -> {
                        double X1 = event1.getX();
                        double Y1 = event1.getY();

                        VisualVertex v2 = graph.findClosestVertex(X1, Y1);
                        if(v2 != null) {
                            if(!graph.hasEdge(v1, v2)) {
                                VisualEdge edge = new VisualEdge(v1, v2);
                                v1.getCircle().setFill(Color.BLACK);
                                graph.addEdge(edge);
                                graphPane.getChildren().add(edge.getLine());

                                graphPane.setOnMouseClicked(null);
                                initializeEvents();
                            }
                        }
                    });
                }
            }
            else if(removeEdgeButton.isSelected()) {
                double X = event.getX();
                double Y = event.getY();

                VisualVertex v1 = graph.findClosestVertex(X, Y);
                if(v1 != null) {
                    v1.getCircle().setFill(Color.BLUE);
                    graphPane.setOnMouseClicked(event1 -> {
                        double X1 = event1.getX();
                        double Y1 = event1.getY();

                        VisualVertex v2 = graph.findClosestVertex(X1, Y1);
                        if(v2 != null) {
                            VisualEdge edge = graph.getEdge(v1, v2);
                            if(edge != null) {
                                v1.getCircle().setFill(Color.BLACK);
                                graph.removeEdge(edge);
                                graphPane.getChildren().remove(edge.getLine());

                                graphPane.setOnMouseClicked(null);
                                initializeEvents();
                            }
                        } else {
                            System.out.println("V2 is null");
                        }
                    });
                }
            }
        });
    }

    private void startGA() throws InterruptedException {
        ColoringGA ga = new ColoringGA(
                graph,
                200,
                50
        );
        ga.Execute();
    }

    private void startGreedy() throws InterruptedException {
        ColoringGreedy greedy = new ColoringGreedy(graph);
        greedy.Execute();
    }

    private void startBkt() throws InterruptedException {
        ColoringBkt bkt = new ColoringBkt(graph);
        bkt.Execute();
    }
}