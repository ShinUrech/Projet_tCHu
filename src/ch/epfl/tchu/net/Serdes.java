package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import com.sun.javafx.beans.event.AbstractNotifyListener;

import java.nio.charset.StandardCharsets;
import java.time.chrono.IsoEra;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class Serdes {

    private Serdes(){};

    public static final Serde<Integer> INTEGER_SERDE = Serde.of(integer -> integer.toString(), Integer::parseInt);

    public static final Serde<String> STRING_SERDE = Serde.of(

            toEncode -> Base64.getEncoder().encodeToString(toEncode.getBytes(StandardCharsets.UTF_8)),

            toDecode ->  new String(Base64.getDecoder().decode(toDecode), StandardCharsets.UTF_8));

    public static final Serde<PlayerId> PLAYER_ID_SERDE = Serde.oneOf(PlayerId.ALL);

    public static final Serde<Player.TurnKind> TURN_KIND_SERDE = Serde.oneOf(Player.TurnKind.ALL);

    public static final Serde<Card> CARD_SERDE = Serde.oneOf(Card.ALL);

    public static final Serde<Route> ROUTE_SERDE = Serde.oneOf(ChMap.routes());

    public static final Serde<Ticket> TICKET_SERDE = Serde.oneOf(ChMap.tickets());

    public static final Serde<List<String>> STRING_LIST_SERDE = Serde.listOf(STRING_SERDE, ',');

    public static final Serde<List<Card>> CARD_LIST_SERDE = Serde.listOf(CARD_SERDE, ',');

    public static final Serde<List<Route>> ROUTE_LIST_SERDE = Serde.listOf(ROUTE_SERDE, ',');

    public static final Serde<SortedBag<Card>> CARD_SORTEDBAG_SERDE = Serde.bagOf(CARD_SERDE, ',');

    public static final Serde<SortedBag<Ticket>> TICKET_SORTEDBAG_SERDE = Serde.bagOf(TICKET_SERDE, ',');

    public static final Serde<List<SortedBag<Card>>> CARD_SORTEDBAG_LIST_SERDE = Serde.listOf(CARD_SORTEDBAG_SERDE, ';');

    public static final Serde<PublicCardState> PUBLIC_CARD_STATE_SERDE = Serde.of(

            toSerialize -> {

                List<String> attributes = List.of(
                        CARD_LIST_SERDE.serialize(toSerialize.faceUpCards()),
                        INTEGER_SERDE.serialize(toSerialize.deckSize()),
                        INTEGER_SERDE.serialize(toSerialize.discardsSize())
                );

                return String.join(";", attributes);
            },

            toDeserialize -> {

                String[] attributes = toDeserialize.split(Pattern.quote(";"), -1);

                List<Card> faceUpCards = CARD_LIST_SERDE.deserialize(attributes[0]);
                int deckSize = INTEGER_SERDE.deserialize(attributes[1]);
                int discardsSize = INTEGER_SERDE.deserialize(attributes[2]);

                return new PublicCardState(faceUpCards, deckSize, discardsSize);
            });

    public static final Serde<PublicPlayerState> PUBLIC_PLAYER_STATE_SERDE = Serde.of(

            toSerialize -> {
                ArrayList<String> listOfData = new ArrayList<>();

                        listOfData.add(INTEGER_SERDE.serialize(toSerialize.ticketCount()));
                        listOfData.add(INTEGER_SERDE.serialize(toSerialize.cardCount()));
                        listOfData.add(ROUTE_LIST_SERDE.serialize(toSerialize.routes()));


                return String.join(";", listOfData);

            },

            toDeserialize -> new PublicPlayerState(

                    INTEGER_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialize).get(0)),
                    INTEGER_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialize).get(1)),
                    ROUTE_LIST_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialize).get(2))
            )
    );

    public static final Serde<PlayerState> PLAYER_STATE_SERDE = Serde.of(

            toSerialise -> {
                List<String> data = List.of(
                        TICKET_SORTEDBAG_SERDE.serialize(toSerialise.tickets()),
                        CARD_SORTEDBAG_SERDE.serialize(toSerialise.cards()),
                        ROUTE_LIST_SERDE.serialize(toSerialise.routes())
                );
                return String.join(";", data);
            },

            toDeserialise -> {
                String[] listOfData = toDeserialise.split(Pattern.quote(";"), -1);

                SortedBag<Ticket> tickets = TICKET_SORTEDBAG_SERDE.deserialize(listOfData[0]);
                SortedBag<Card> cards = CARD_SORTEDBAG_SERDE.deserialize(listOfData[1]);
                List<Route> routes = ROUTE_LIST_SERDE.deserialize(listOfData[2]);


                return new PlayerState(tickets, cards,routes);

            }
    );

    public static final Serde<PublicGameState> PUBLIC_GAME_STATE_SERDE = Serde.of(

            toSerialise -> {

                List<String> data = List.of(

                        INTEGER_SERDE.serialize(toSerialise.ticketsCount()),
                        PUBLIC_CARD_STATE_SERDE.serialize(toSerialise.cardState()),
                        PLAYER_ID_SERDE.serialize(toSerialise.currentPlayerId()),
                        PUBLIC_PLAYER_STATE_SERDE.serialize(toSerialise.playerState(PlayerId.PLAYER_1)),
                        PUBLIC_PLAYER_STATE_SERDE.serialize(toSerialise.playerState(PlayerId.PLAYER_2)),
                        PLAYER_ID_SERDE.serialize(toSerialise.lastPlayer()));

                return String.join(":", data);

            },

            toDeserialize -> {
                String[] data = toDeserialize.split(Pattern.quote(":"), -1);

                int ticketsCount = INTEGER_SERDE.deserialize(data[0]);
                PublicCardState cardstate = PUBLIC_CARD_STATE_SERDE.deserialize(data[1]);
                PlayerId currentPlayerId = PLAYER_ID_SERDE.deserialize(data[2]);

                Map<PlayerId, PublicPlayerState> playerStateMap = Map.of(

                        PlayerId.PLAYER_1, PLAYER_STATE_SERDE.deserialize(data[3]),
                        PlayerId.PLAYER_2, PLAYER_STATE_SERDE.deserialize(data[4])
                );

                PlayerId lastPlayer = PLAYER_ID_SERDE.deserialize(data[5]);

                return new PublicGameState(ticketsCount, cardstate, currentPlayerId, playerStateMap, lastPlayer);

            });
}
