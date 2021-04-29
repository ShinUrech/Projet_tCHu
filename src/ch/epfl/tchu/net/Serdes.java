package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * A class for defining different types of Serdes that are used in the project.
 *
 * @author Shin Urech (327245)
 */
public final class Serdes {

    private Serdes(){};

    /**
     * this is a Serde for int type data
     */
    public static final Serde<Integer> INTEGER_SERDE = Serde.of(integer -> integer.toString(), Integer::parseInt);

    /**
     * this is a Serde for String type data
     */
    public static final Serde<String> STRING_SERDE = Serde.of(

            toEncode -> {
                return (!toEncode.isEmpty())
                        ? Base64.getEncoder().encodeToString(toEncode.getBytes(StandardCharsets.UTF_8))
                        : "";
            },

            toDecode -> (!toDecode.isEmpty())
                    ? new String(Base64.getDecoder().decode(toDecode.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)
                    : "");

    /**
     * this is a Serde for PlayerId type data
     */
    public static final Serde<PlayerId> PLAYER_ID_SERDE = Serde.oneOf(PlayerId.ALL);

    /**
     * this is a Serde for a TurnKind type data
     */
    public static final Serde<Player.TurnKind> TURN_KIND_SERDE = Serde.oneOf(Player.TurnKind.ALL);

    /**
     * this is a Serde for a Card type data
     */
    public static final Serde<Card> CARD_SERDE = Serde.oneOf(Card.ALL);

    /**
     * this is a Serde for a Route type data
     */
    public static final Serde<Route> ROUTE_SERDE = Serde.oneOf(ChMap.routes());

    /**
     * this is a Serde for a Ticket type data
     */
    public static final Serde<Ticket> TICKET_SERDE = Serde.oneOf(ChMap.tickets());

    /**
     * this is a Serde for a List of String type data
     */
    public static final Serde<List<String>> STRING_LIST_SERDE = Serde.listOf(STRING_SERDE, ',');

    /**
     * this is a Serde for a List of Card type data
     */
    public static final Serde<List<Card>> CARD_LIST_SERDE = Serde.listOf(CARD_SERDE, ',');

    /**
     * this is a Serde for a List of Route type data
     */
    public static final Serde<List<Route>> ROUTE_LIST_SERDE = Serde.listOf(ROUTE_SERDE, ',');

    /**
     * this is a Serde for a SortedBag of Card type data
     */
    public static final Serde<SortedBag<Card>> CARD_SORTEDBAG_SERDE = Serde.bagOf(CARD_SERDE, ',');

    /**
     * this is a Serde for a SortedBag of Ticket type data
     */
    public static final Serde<SortedBag<Ticket>> TICKET_SORTEDBAG_SERDE = Serde.bagOf(TICKET_SERDE, ',');

    /**
     * this is a Serde for a List of SortedBag of Card type data
     */
    public static final Serde<List<SortedBag<Card>>> CARD_SORTEDBAG_LIST_SERDE = Serde.listOf(CARD_SORTEDBAG_SERDE, ';');

    /**
     * this is a Serde for a PublicCardState type data
     */
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

    /**
     * this is a Serde for a PublicPlayerState type data
     */
    public static final Serde<PublicPlayerState> PUBLIC_PLAYER_STATE_SERDE = Serde.of(

            toSerialize -> {
                ArrayList<String> listOfData = new ArrayList<>();

                listOfData.add(INTEGER_SERDE.serialize(toSerialize.ticketCount()));
                listOfData.add(INTEGER_SERDE.serialize(toSerialize.cardCount()));
                listOfData.add(ROUTE_LIST_SERDE.serialize(toSerialize.routes()));


                return String.join(";", listOfData);

            },

            toDeserialize -> {
                String[] attributes = toDeserialize.split(Pattern.quote(";"), -1);

                int ticketCount = INTEGER_SERDE.deserialize(attributes[0]);
                int cardCount = INTEGER_SERDE.deserialize(attributes[1]);
                List<Route> routes = ROUTE_LIST_SERDE.deserialize(attributes[2]);

                return new PublicPlayerState(ticketCount, cardCount, routes);
            }
    );

    /**
     * this is a Serde for a PlayerState type data
     */
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

    /**
     * this is a Serde for a PublicGameState type data
     */
    public static final Serde<PublicGameState> PUBLIC_GAME_STATE_SERDE = Serde.of(

            toSerialise -> {

                List<String> data = List.of(

                        INTEGER_SERDE.serialize(toSerialise.ticketsCount()),
                        PUBLIC_CARD_STATE_SERDE.serialize(toSerialise.cardState()),
                        PLAYER_ID_SERDE.serialize(toSerialise.currentPlayerId()),
                        PUBLIC_PLAYER_STATE_SERDE.serialize(toSerialise.playerState(PlayerId.PLAYER_1)),
                        PUBLIC_PLAYER_STATE_SERDE.serialize(toSerialise.playerState(PlayerId.PLAYER_2)),
                        PLAYER_ID_SERDE.serialize(toSerialise.lastPlayer())
                );

                return String.join(":", data);

            },

            toDeserialize -> {
                String[] attributes = toDeserialize.split(Pattern.quote(":"), -1);

                int ticketsCount = INTEGER_SERDE.deserialize(attributes[0]);

                PublicCardState cardstate = PUBLIC_CARD_STATE_SERDE.deserialize(attributes[1]);

                PlayerId currentPlayerId = PLAYER_ID_SERDE.deserialize(attributes[2]);

                Map<PlayerId, PublicPlayerState> playerStateMap = Map.of(

                        PlayerId.PLAYER_1, PUBLIC_PLAYER_STATE_SERDE.deserialize(attributes[3]),
                        PlayerId.PLAYER_2, PUBLIC_PLAYER_STATE_SERDE.deserialize(attributes[4])
                );

                PlayerId lastPlayer = PLAYER_ID_SERDE.deserialize(attributes[5]);

                return new PublicGameState(ticketsCount, cardstate, currentPlayerId, playerStateMap, lastPlayer);

            });
}