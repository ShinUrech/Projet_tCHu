package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.Route;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;


class MapViewCreator {

    private MapViewCreator(){};

    public static Node createMapView(ObservableGameState observableGameState,
                 ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHP, CardChooser cardChooser){

        Pane pane = new Pane();
        pane.getStylesheets().add("map.css");
        pane.getStylesheets().add("colors.css");

        ImageView image = new ImageView();
        pane.getChildren().add(image);

        List<Route> allRoutes = ChMap.routes();

        for(Route route : allRoutes){

            Group routeGroup = new Group();

            ReadOnlyObjectProperty<PlayerId> routeProp = observableGameState.route(route);
            System.out.println(routeProp);

            routeGroup.disableProperty().bind(
                    claimRouteHP.isNull().or(observableGameState.canSeize(route).not()));


            ActionHandlers.ClaimRouteHandler claimRouteH = claimRouteHP.get();

            routeGroup.setOnMouseClicked(e -> observableGameState.possibleClaimCards(route));
            List<SortedBag<Card>> possibleClaimCards = observableGameState.possibleClaimCards(route);
            if(possibleClaimCards.size() == 1) {
               claimRouteH.onClaimRoute(route, possibleClaimCards.get(0));
            }
            else if(possibleClaimCards.size() > 1){
                ActionHandlers.ChooseCardsHandler chooseCardsH =
                        chosenCards -> claimRouteH.onClaimRoute(route, chosenCards);
                cardChooser.chooseCards(possibleClaimCards, chooseCardsH);
            }


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
                c1.setRadius(3);
                c1.setCenterX(12);
                c1.setCenterY(6);

                Circle c2 = new Circle();
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

            dumpTree(routeGroup);

            observableGameState.route(route).addListener((property, o, n) ->
                    routeGroup.getStyleClass().add(observableGameState.route(route).getName()));

            pane.getChildren().add(routeGroup);
        }
        return pane;
    }

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options, ActionHandlers.ChooseCardsHandler handler);
    }

    public static void dumpTree(Node root) {
        dumpTree(0, root);
    }

    public static void dumpTree(int indent, Node root) {
        System.out.printf("%s%s (id: %s, classes: [%s])%n",
                " ".repeat(indent),
                root.getTypeSelector(),
                root.getId(),
                String.join(", ", root.getStyleClass()));
        if (root instanceof Parent) {
            Parent parent = ((Parent) root);
            for (Node child : parent.getChildrenUnmodifiable())
                dumpTree(indent + 2, child);
        }
    }
}
