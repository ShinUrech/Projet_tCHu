package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.Route;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;


class MapViewCreator {

    private MapViewCreator(){};

    public static Node createMapView(ObservableGameState observableGameState,
                 ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHP){

        Pane pane = new Pane();
        pane.getStylesheets().add("map.css");
        pane.getStylesheets().add("colors.css");

        ImageView image = new ImageView();
        pane.getChildren().add(image);

        List<Route> allRoutes = ChMap.routes();

        for(Route route : allRoutes){

            Group routeGroup = new Group();

            ReadOnlyObjectProperty<PlayerId> routeProp = observableGameState.route(route);
            if(routeProp != null) {
                routeProp.addListener((property, o, n) ->
                        routeGroup.getStyleClass().add(routeProp.getName()));
            }
            routeGroup.disableProperty().bind(
                    claimRouteHP.isNull().or(observableGameState.canSeize(route).not()));

            routeGroup.getStyleClass().add("route");
            routeGroup.getStyleClass().add(route.level().name());
            if(route.color() == null){
                routeGroup.getStyleClass().add("NEUTRAL");
            }
            else routeGroup.getStyleClass().add(route.color().name());
            routeGroup.setId(route.id());

            System.out.println(routeGroup.getStyleClass());

            for(int i = 1; i <= route.length(); ++i){
                Group caseGroup = new Group();
                caseGroup.setId(route.id() + "_" + Integer.toString(i));

                Rectangle track = new Rectangle();
                track.getStyleClass().add("track");
                track.getStyleClass().add("filled");
                track.setWidth(36);
                track.setHeight(12);

                Group wagonGroup = new Group();
                wagonGroup.getStyleClass().add("car");

                Rectangle wagonRect = new Rectangle();
                wagonRect.getStyleClass().add("filled");
                wagonRect.setWidth(36);
                wagonRect.setHeight(12);

                Circle c1 = new Circle();
                c1.getStyleClass().add("filled");
                c1.setRadius(3);
                c1.setCenterX(12);
                c1.setCenterY(6);

                Circle c2 = new Circle();
                c2.getStyleClass().add("filled");
                c2.setRadius(3);
                c2.setCenterX(24);
                c2.setCenterY(6);

                wagonGroup.getChildren().add(wagonRect);
                wagonGroup.getChildren().add(c1);
                wagonGroup.getChildren().add(c2);

                caseGroup.getChildren().add(track);
                caseGroup.getChildren().add(wagonGroup);

                routeGroup.getChildren().add(caseGroup);
            }

            pane.getChildren().add(routeGroup);
        }
        return pane;
    }
}
