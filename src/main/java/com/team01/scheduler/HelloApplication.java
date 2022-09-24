package com.team01.scheduler;

import com.team01.scheduler.algorithm.DepthFirstSearch;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.EdgesLinkedList;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.Node;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HelloApplication extends Application {

    private TaskRunner taskRunner;

    /**
     * Creates a sample graph using the node and edge
     * weights provided in the brief (refer to Figure 1).
     *
     * @return Graph instance
     */
    public Graph createSampleGraph() {
        Node nodeA = new Node("a", 2);
        Node nodeB = new Node("b", 3);
        Node nodeC = new Node("c", 3);
        Node nodeD = new Node("d", 2);

        Edge edgeAB = new Edge(nodeA, nodeB, 1);
        Edge edgeAC = new Edge(nodeA, nodeC, 2);
        Edge edgeBD = new Edge(nodeB, nodeD, 2);
        Edge edgeCD = new Edge(nodeC, nodeD, 1);

        List<Node> nodes = Arrays.asList(nodeA, nodeB, nodeC, nodeD);
        List<Edge> edges = Arrays.asList(edgeAB, edgeAC, edgeBD, edgeCD);

        return new Graph(edges, nodes);
    }

    /**
     * Creates a new task to print out all the edges in the graph.
     *
     * @return IRunnable instance
     */
    public IRunnable createPrintGraphTask() {
        return new IRunnable() {
            @Override
            public String getTaskName() {
                return "Print Graph";
            }

            @Override
            public void run(Graph graph) {
                for (EdgesLinkedList currentList: graph.getGraph().values()) {
                    int listSize = currentList.size();
                    Edge currentEdge = currentList.get(0);

                    for (int i = 0; i < listSize; i++) {
                        currentEdge = currentList.get(i);

                        System.out.println(currentEdge.toString());
                    }
                }
            }
        };
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        Graph graphData = createSampleGraph();

        // Do runner
        taskRunner = new TaskRunner();

        taskRunner.safeRun(createPrintGraphTask(), graphData);
        taskRunner.safeRun(new DepthFirstSearch(), graphData);
    }

    public static void main(String[] args) {

        launch();
    }
}