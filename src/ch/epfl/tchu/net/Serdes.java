package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public final class Serdes {

    private Serdes(){};

    public static final Serde<Integer> INTEGER_SERDE = Serde.of(x -> x.toString(), Integer::parseInt);

    public static final Serde<String> STRING_SERDE = Serde.of(

            toEncode -> Base64.getEncoder().encodeToString(toEncode.getBytes(StandardCharsets.UTF_8)),

            toDecode -> new String(Base64.getDecoder().decode(toDecode), StandardCharsets.UTF_8));

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

            toSerialize -> Serde.listOf(STRING_SERDE, ';').serialize(List.of(CARD_LIST_SERDE.serialize(toSerialize.faceUpCards()), INTEGER_SERDE.serialize(toSerialize.deckSize()), INTEGER_SERDE.serialize(toSerialize.discardsSize()))),

            toDeserialize -> new PublicCardState(

                    CARD_LIST_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialize).get(0)),

                    INTEGER_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialize).get(1)),

                    INTEGER_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialize).get(2))
            ));

    public static final Serde<PublicPlayerState> PUBLIC_PLAYER_STATE_SERDE = Serde.of(

            toSerialize -> Serde.listOf(STRING_SERDE, ';').serialize(List.of(INTEGER_SERDE.serialize(toSerialize.ticketCount()), INTEGER_SERDE.serialize(toSerialize.cardCount()), ROUTE_LIST_SERDE.serialize(toSerialize.routes()))),

            toDeserialize -> new PublicPlayerState(

                    INTEGER_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialize).get(0)),

                    INTEGER_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialize).get(1)),

                    ROUTE_LIST_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialize).get(2))
            )
    );

    public static final Serde<PlayerState> PLAYER_STATE_SERDE = Serde.of(

            toSerialise -> Serde.listOf(STRING_SERDE, ';').serialize(List.of(

                    TICKET_SORTEDBAG_SERDE.serialize(toSerialise.tickets()),
                    CARD_SORTEDBAG_SERDE.serialize(toSerialise.cards()),
                    ROUTE_LIST_SERDE.serialize(toSerialise.routes())
            )),

            toDeserialise -> new PlayerState(

                    TICKET_SORTEDBAG_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialise).get(0)),
                    CARD_SORTEDBAG_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialise).get(1)),
                    ROUTE_LIST_SERDE.deserialize(Serde.listOf(STRING_SERDE, ';').deserialize(toDeserialise).get(2))
            )
    );

    public static final Serde<PublicGameState> PUBLIC_GAME_STATE_SERDE = Serde.of(

            toSerialise -> Serde.listOf(STRING_SERDE, ':').serialize(List.of(

                    INTEGER_SERDE.serialize(toSerialise.ticketsCount()),
                    PUBLIC_CARD_STATE_SERDE.serialize(toSerialise.cardState()),
                    PLAYER_ID_SERDE.serialize(toSerialise.currentPlayerId()),
                    PUBLIC_PLAYER_STATE_SERDE.serialize(toSerialise.playerState(PlayerId.PLAYER_1)),
                    PUBLIC_PLAYER_STATE_SERDE.serialize(toSerialise.playerState(PlayerId.PLAYER_2)),
                    PLAYER_ID_SERDE.serialize(toSerialise.lastPlayer())
            )),

            toDeserialize -> new PublicGameState(

                    INTEGER_SERDE.deserialize(Serde.listOf(STRING_SERDE, ':').deserialize(toDeserialize).get(0)),
                    PUBLIC_CARD_STATE_SERDE.deserialize(Serde.listOf(STRING_SERDE, ':').deserialize(toDeserialize).get(1)),
                    PLAYER_ID_SERDE.deserialize(Serde.listOf(STRING_SERDE, ':').deserialize(toDeserialize).get(2)),

                    Map.of(
                            PlayerId.PLAYER_1, PUBLIC_PLAYER_STATE_SERDE.deserialize(Serde.listOf(STRING_SERDE, ':').deserialize(toDeserialize).get(3)),

                            PlayerId.PLAYER_2, PUBLIC_PLAYER_STATE_SERDE.deserialize(Serde.listOf(STRING_SERDE, ':').deserialize(toDeserialize).get(4))
                    ),

                    PLAYER_ID_SERDE.deserialize(Serde.listOf(STRING_SERDE, ':').deserialize(toDeserialize).get(5))

            )

    );
}
