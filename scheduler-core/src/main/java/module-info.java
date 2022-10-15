module com.team01.scheduler {
    requires org.slf4j;

    exports com.team01.scheduler;
    exports com.team01.scheduler.algorithm;
    exports com.team01.scheduler.prototype;
    exports com.team01.scheduler.graph.models;
    exports com.team01.scheduler.graph.exceptions;
    exports com.team01.scheduler.graph.util;
    exports com.team01.scheduler.algorithm.matrixModels;
    exports com.team01.scheduler.visualizer;
    exports com.team01.scheduler.algorithm.astar;
    exports com.team01.scheduler.algorithm.branchandbound;
}