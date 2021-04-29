package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;

import static java.nio.charset.StandardCharsets.US_ASCII;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class RemotePlayerProxy implements Player {

    private Socket socket;

    public RemotePlayerProxy(Socket socket){
        this.socket = socket;
    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

        String message = MessageId.INIT_PLAYERS.name() + " " + Serdes.PLAYER_ID_SERDE.serialize(ownId) + " "
                + Serdes.STRING_LIST_SERDE.serialize(List.of(playerNames.get(PlayerId.PLAYER_1),
                    playerNames.get(PlayerId.PLAYER_2)));

        send(message);
    }

    @Override
    public void receiveInfo(String info) {

        String message = MessageId.RECEIVE_INFO.name() + " " + Serdes.STRING_SERDE.serialize(info);

        send(message);
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

        String message = MessageId.UPDATE_STATE.name() + " " + Serdes.PUBLIC_GAME_STATE_SERDE.serialize(newState) + " "
                + Serdes.PLAYER_STATE_SERDE.serialize(ownState);

        send(message);
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

        String message = MessageId.SET_INITIAL_TICKETS.name() + " " + Serdes.TICKET_SORTEDBAG_SERDE.serialize(tickets);

        send(message);
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {

        String message = MessageId.CHOOSE_INITIAL_TICKETS.name();

        send(message);
        String receivedMessage = read();

        return Serdes.TICKET_SORTEDBAG_SERDE.deserialize(receivedMessage);
    }

    @Override
    public TurnKind nextTurn() {

        String message = MessageId.NEXT_TURN.name();

        send(message);
        String receivedMessage = read();

        return Serdes.TURN_KIND_SERDE.deserialize(receivedMessage);
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {

        String message = MessageId.CHOOSE_TICKETS.name() + " " + Serdes.TICKET_SORTEDBAG_SERDE.serialize(options);

        send(message);
        String receivedMessage = read();

        return Serdes.TICKET_SORTEDBAG_SERDE.deserialize(receivedMessage);
    }

    @Override
    public int drawSlot() {

        String message = MessageId.DRAW_SLOT.name();

        send(message);
        String receivedMessage = read();

        return Serdes.INTEGER_SERDE.deserialize(receivedMessage);
    }

    @Override
    public Route claimedRoute() {

        String message = MessageId.ROUTE.name();

        send(message);
        String receivedMessage = read();

        return Serdes.ROUTE_SERDE.deserialize(receivedMessage);
    }

    @Override
    public SortedBag<Card> initialClaimCards() {

        String message = MessageId.CARDS.name();

        send(message);
        String receivedMessage = read();

        return Serdes.CARD_SORTEDBAG_SERDE.deserialize(receivedMessage);
    }

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
