package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;

import static java.nio.charset.StandardCharsets.US_ASCII;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * A class that represents a proxy client, who communicates with a remote client through a socket.
 *
 * @author Aidas Venckunas (325464)
 */
public class RemotePlayerProxy implements Player {

    private final Socket socket;

    /**
     * A public constructor for a proxy client.
     *
     * @param socket a given socket
     * @throws IllegalArgumentException if the given socket is null
     */
    public RemotePlayerProxy(Socket socket){

        Preconditions.checkArgument(socket != null);

        this.socket = socket;
    }

    /**
     * A method that sends a request to a remote client to call a method initPlayers with
     * the following information:
     *
     * @param ownId the player's Id
     * @param playerNames the players' names
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

        String message = MessageId.INIT_PLAYERS.name() + " " + Serdes.PLAYER_ID_SERDE.serialize(ownId) + " "
                + Serdes.STRING_LIST_SERDE.serialize(List.of(playerNames.get(PlayerId.PLAYER_1),
                    playerNames.get(PlayerId.PLAYER_2)));

        send(message);
    }

    /**
     * A method that sends a request to a remote client to call a method receiveInfo with
     * the following information:
     *
     * @param info the information that needs to be communicated
     */
    @Override
    public void receiveInfo(String info) {

        String message = MessageId.RECEIVE_INFO.name() + " " + Serdes.STRING_SERDE.serialize(info);

        send(message);
    }

    /**
     * A method that sends a request to a remote client to call a method updateState with
     * the following information:
     *
     * @param newState the new PublicGameState
     * @param ownState the new PlayerState of a given player
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

        String message = MessageId.UPDATE_STATE.name() + " " + Serdes.PUBLIC_GAME_STATE_SERDE.serialize(newState) + " "
                + Serdes.PLAYER_STATE_SERDE.serialize(ownState);

        send(message);
    }

    /**
     * A method that sends a request to a remote client to call a method setInitialTicketChoice with
     * the following information:
     *
     * @param tickets the drawn tickets
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

        String message = MessageId.SET_INITIAL_TICKETS.name() + " " + Serdes.TICKET_SORTEDBAG_SERDE.serialize(tickets);

        send(message);
    }

    /**
     * A method that sends a request to a remote client to call a method chooseInitialTickets and
     * reads and returns the following information:
     *
     * @return tickets initially chosen
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {

        String message = MessageId.CHOOSE_INITIAL_TICKETS.name();

        send(message);
        String receivedMessage = read();

        return Serdes.TICKET_SORTEDBAG_SERDE.deserialize(receivedMessage);
    }

    /**
     * A method that sends a request to a remote client to call a method NextTurn and
     * reads and returns the following information:
     *
     * @return the chosen kind of the turn
     */
    @Override
    public TurnKind nextTurn() {

        String message = MessageId.NEXT_TURN.name();

        send(message);
        String receivedMessage = read();

        return Serdes.TURN_KIND_SERDE.deserialize(receivedMessage);
    }

    /**
     *  A method that sends a request to a remote client to call a method chooseTickets with
     *  the following information:
     *
     * @param options the tickets that can be picked by the player
     *
     * Reads and returns the following information:
     *
     * @return the choice of the tickets
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {

        String message = MessageId.CHOOSE_TICKETS.name() + " " + Serdes.TICKET_SORTEDBAG_SERDE.serialize(options);

        send(message);
        String receivedMessage = read();

        return Serdes.TICKET_SORTEDBAG_SERDE.deserialize(receivedMessage);
    }

    /**
     * A method that sends a request to a remote client to call a method drawSlot and
     * reads and returns the following information:
     *
     * @return the draw slot chosen
     */
    @Override
    public int drawSlot() {

        String message = MessageId.DRAW_SLOT.name();

        send(message);
        String receivedMessage = read();

        return Serdes.INTEGER_SERDE.deserialize(receivedMessage);
    }

    /**
     * A method that sends a request to a remote client to call a method claimedRoute and
     * reads and returns the following information:
     *
     * @return the route that has been claimed
     */
    @Override
    public Route claimedRoute() {

        String message = MessageId.ROUTE.name();

        send(message);
        String receivedMessage = read();

        return Serdes.ROUTE_SERDE.deserialize(receivedMessage);
    }

    /**
     * A method that sends a request to a remote client to call a method initialClaimCards and
     * reads and returns the following information:
     *
     * @return the cards initially used to claim a route
     */
    @Override
    public SortedBag<Card> initialClaimCards() {

        String message = MessageId.CARDS.name();

        send(message);
        String receivedMessage = read();

        return Serdes.CARD_SORTEDBAG_SERDE.deserialize(receivedMessage);
    }

    /**
     *  A method that sends a request to a remote client to call a method chooseAdditionalCards with
     *  the following information:
     *
     * @param options the card combinations that can be picked
     *
     * Reads and returns the following information:
     *
     * @return the chosen combination of cards
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {

        String message = MessageId.CHOOSE_ADDITIONAL_CARDS.name() + " "
                + Serdes.CARD_SORTEDBAG_LIST_SERDE.serialize(options);

        send(message);
        String receivedMessage = read();

        return Serdes.CARD_SORTEDBAG_SERDE.deserialize(receivedMessage);
    }

    private void send(String message){
        try{
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII));
                w.write(message);
                w.write('\n');
                w.flush();
        }catch(IOException e){
            throw new UncheckedIOException(e);
        }
    }


    private String read(){
        try{
            BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));
            return r.readLine();
        }catch(IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
