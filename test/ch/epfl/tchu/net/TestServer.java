package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class TestServer {

    public static void main (String [] args) throws IOException {

        System.out.println("Starting server!");

        try (ServerSocket serverSocket = new ServerSocket(5108);

        Socket socket = serverSocket.accept()){
            Player playerProxy = new RemotePlayerProxy(socket);

            var playerNames = Map.of(PlayerId.PLAYER_1, "Ada", PlayerId.PLAYER_2, "Charles");
            playerProxy.initPlayers(PlayerId.PLAYER_1, playerNames);

            playerProxy.receiveInfo("some type of information");

            var faceUpCards = SortedBag.of(5, Card.LOCOMOTIVE).toList();
            var cardState = new PublicCardState(faceUpCards, 0, 0);
            var initialPlayerState = (PublicPlayerState) PlayerState.initial(SortedBag.of(4, Card.RED));
            var playerState = Map.of(
                    PLAYER_1, initialPlayerState,
                    PLAYER_2, initialPlayerState);
            var pgs = new PublicGameState(5, cardState, PLAYER_1, playerState, PLAYER_1);
            playerProxy.updateState(pgs, PlayerState.initial(SortedBag.of(4, Card.RED)));

            playerProxy.setInitialTicketChoice(SortedBag.of(ChMap.tickets().subList(0, 10)));

            playerProxy.chooseInitialTickets();

            playerProxy.nextTurn();

            playerProxy.chooseTickets(SortedBag.of(ChMap.tickets().subList(0, 10)));

            playerProxy.drawSlot();

            playerProxy.claimedRoute();

            playerProxy.initialClaimCards();

            playerProxy.chooseAdditionalCards(List.of(SortedBag.of(4, Card.RED), SortedBag.of(3, Card.BLUE),
                    SortedBag.of(4, Card.GREEN)));
        }

        System.out.println("Server done!");
    }
}