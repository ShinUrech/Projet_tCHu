package ch.epfl.tchu.gui;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;



class MapViewCreator {

    private MapViewCreator(){};

    public static Node createMapView(){

        Pane pane = new Pane();
        pane.getStylesheets().add("map.css");
        pane.getStylesheets().add("colors.css");

        ImageView image = new ImageView();
        pane.getChildren().add(image);

        return pane;
    }
}
