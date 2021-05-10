package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Color;
import ch.epfl.tchu.game.PublicCardState;
import javafx.beans.property.DoubleProperty;
import javafx.css.Style;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.Properties;

public class DecksViewCreator {

    private final int OUTSIDE_WIDTH = 60;
    private final int OUTSIDE_HEIGHT = 90;
    private final int INSIDE_FILLING_WIDTH = 40;
    private final int INSIDE_FILLING_HEIGHT = 70;


    private DecksViewCreator(){};

    public Node creatHandView(ObservableGameState currentGameState){

        Node base = new HBox();
        base.getStyleClass().add("deck.css");
        base.getStyleClass().add("colors.css");

        Node ticketList = new ListView<>(currentGameState.tickets());


        return null;
    }

    private Node cardStackPaneGenerator(Card inputCard){
        StackPane stackPane = new StackPane();

        Style color;

        Rectangle cardOutside = new Rectangle(OUTSIDE_WIDTH, OUTSIDE_HEIGHT);
        cardOutside.setArcWidth(10);
        cardOutside.setArcHeight(10);

        Rectangle cardInside = new Rectangle(INSIDE_FILLING_WIDTH, INSIDE_FILLING_HEIGHT);
        Rectangle locomotiveLogo = new Rectangle(INSIDE_FILLING_WIDTH, INSIDE_FILLING_HEIGHT);
        locomotiveLogo.getStyle()

    }

}
