package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.PublicCardState;
import ch.epfl.tchu.game.Ticket;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

class DecksViewCreator {

    private DecksViewCreator(){};

    public static Node createHandView(){

        HBox handView = new HBox();
        handView.getStylesheets().add("decks.css");
        handView.getStylesheets().add("colors.css");

        ListView<Ticket> tickets = new ListView<>();
        tickets.setId("tickets");

        HBox hand = new HBox();
        hand.setId("hand-pane");

        for(Card card : Card.ALL){

            StackPane cardPane = new StackPane();
            if(card == Card.LOCOMOTIVE){
                cardPane.getStyleClass().add("NEUTRAL");
            }
            else cardPane.getStyleClass().add(card.color().name());
            cardPane.getStyleClass().add("card");

            Rectangle outside = new Rectangle();
            outside.getStyleClass().add("outside");
            outside.setHeight(90);
            outside.setWidth(60);

            Rectangle inside = new Rectangle();
            inside.getStyleClass().add("inside");
            inside.getStyleClass().add("filled");
            inside.setHeight(70);
            inside.setWidth(40);

            Rectangle train = new Rectangle();
            train.getStyleClass().add("train-image");
            train.setHeight(70);
            train.setWidth(40);

            Text counter = new Text();
            counter.getStyleClass().add("count");

            cardPane.getChildren().add(outside);
            cardPane.getChildren().add(inside);
            cardPane.getChildren().add(train);
            cardPane.getChildren().add(counter);

            hand.getChildren().add(cardPane);
        }

        handView.getChildren().add(tickets);
        handView.getChildren().add(hand);

        return handView;
    }

    public static Node createCardsView(){
        /**
         * to do for Shin
         *
         * You have to create the second graph from 3.5.1
         * You can inspire from the method createHandsView, its almost the same ;)
         *
         * After that you can try to establish links and listeners in this method, after reading
         * javafx pdf section 4.
         *
         * to do this you have to look at 3.5.2
         *
         * The properties will be defined in ObservableGameState, but this class is not written yet.
         */
        return null;
    }



}
