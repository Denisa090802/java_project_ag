module com.graph_coloring.graph_coloring {
    requires javafx.controls;
    requires javafx.fxml;
    requires graph4j;
    requires javatuples;


    opens com.graph_coloring.graph_coloring to javafx.fxml;
    exports com.graph_coloring.graph_coloring;
}