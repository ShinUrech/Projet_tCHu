package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * A class that represents a remote Client.
 *
 * @author Aidas Venckunas (325464)
 */
public class RemotePlayerClient {

    private final Player player;
    private final String hostName;
    private final int port;

    /**
     * A public constructor for a remote Client.
     *
     * @param player a remote player (player that is running on a different program)
     * @param hostName a name of the hosting server
     * @param port a port that a server is attached to
     * @throws IllegalArgumentException if a given player is empty or if a hostName is empty
     */
    public RemotePlayerClient(Player player, String hostName, int port){

        Preconditions.checkArgument(player != null && hostName != null);

        this.player = player;
        this.hostName = hostName;
        this.port = port;
    }

    /**
     * A method that runs all the processes between a proxy and a client:
     * reads a message from a proxy,
     * deserializes it,
     * calls a requested method for the client player,
     * serializes and returns the needed information back to the proxy.
     */
    public void run(){
        try (Socket s = new Socket(hostName, port);

            BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream(), US_ASCII))){

            String message;
            do{
                message = r.readLine();
                if(message == null){
                    break;
                }

                String[] data = message.split(Pattern.quote(" "), -1);
                MessageId messageType = MessageId.valueOf(data[0]);

                switch (messageType) {

                    case INIT_PLAYERS:

                         PlayerId ownId = Serdes.PLAYER_ID_SERDE.deserialize(data[1]);
                         List<String> namesList = Serdes.STRING_LIST_SERDE.deserialize(data[2]);
                         Map<PlayerId, String> names = new EnumMap<PlayerId, String>(PlayerId.class);

                         names.put(PlayerId.PLAYER_1, namesList.get(0));
                         names.put(PlayerId.PLAYER_2, namesList.get(1));

                         player.initPlayers(ownId, names);
                         break;

                     case RECEIVE_INFO:

                         String info = Serdes.STRING_SERDE.deserialize(data[1]);

                         player.receiveInfo(info);
                         break;

                     case UPDATE_STATE:

                         PublicGameState newState = Serdes.PUBLIC_GAME_STATE_SERDE.deserialize(data[1]);
                         PlayerState ownState = Serdes.PLAYER_STATE_SERDE.deserialize(data[2]);

                         player.updateState(newState, ownState);
                         break;

                     case SET_INITIAL_TICKETS:

                         SortedBag<Ticket> tickets = Serdes.TICKET_SORTEDBAG_SERDE.deserialize(data[1]);

                         player.setInitialTicketChoice(tickets);
                         break;

                     case CHOOSE_INITIAL_TICKETS:

                         SortedBag<Ticket> chosenInitialTickets = player.chooseInitialTickets();

                         send(Serdes.TICKET_SORTEDBAG_SERDE.serialize(chosenInitialTickets), s);
                         break;

                     case NEXT_TURN:

                         Player.TurnKind nextTurn = player.nextTurn();

                         send(Serdes.TURN_KIND_SERDE.serialize(nextTurn), s);
                         break;

                     case CHOOSE_TICKETS:

                         SortedBag<Ticket> options = Serdes.TICKET_SORTEDBAG_SERDE.deserialize(data[1]);
                         SortedBag<Ticket> chosenTickets = player.chooseTickets(options);

                         send(Serdes.TICKET_SORTEDBAG_SERDE.serialize(chosenTickets), s);
                         break;

                     case DRAW_SLOT:

                         int drawSlot = player.drawSlot();

                         send(Serdes.INTEGER_SERDE.serialize(drawSlot), s);
                         break;

                     case ROUTE:

                         Route claimedRoute = player.claimedRoute();

                         send(Serdes.ROUTE_SERDE.serialize(claimedRoute), s);
                         break;

                     case CARDS:

                         SortedBag<Card> initialCards = player.initialClaimCards();

                         send(Serdes.CARD_SORTEDBAG_SERDE.serialize(initialCards), s);
                         break;

                     case CHOOSE_ADDITIONAL_CARDS:

                         List<SortedBag<Card>> optionsCards = Serdes.CARD_SORTEDBAG_LIST_SERDE.deserialize(data[1]);

                         SortedBag<Card> chosenCards = player.chooseAdditionalCards(optionsCards);

                         send(Serdes.CARD_SORTEDBAG_SERDE.serialize(chosenCards), s);
                         break;
                }
            }while(message != null);
        }catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void send(String message, Socket socket){
        try{
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII));
            w.write(message);
            w.write('\n');
            w.flush();
        }catch(IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
