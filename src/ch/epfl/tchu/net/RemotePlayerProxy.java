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

    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return null;
    }

    @Override
    public TurnKind nextTurn() {
        return null;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        return null;
    }

    @Override
    public int drawSlot() {
        return 0;
    }

    @Override
    public Route claimedRoute() {
        return null;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return null;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        return null;
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
}
