package ch.epfl.tchu.net;

import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class Serdes {

    private Serdes(){};

    public static final Serde<Integer> INTEGER_SERDE = Serde.of(x -> x.toString(), Integer::parseInt);

    public static final Serde<String> STRING_SERDE = Serde.of(x -> Base64.getEncoder().encodeToString(x.getBytes(StandardCharsets.UTF_8)), y -> new String(Base64.getDecoder().decode(y), StandardCharsets.UTF_8));

    public static final Serde<PlayerId> PLAYER_ID_SERDE = Serde.oneOf(PlayerId.ALL);

    public static final Serde<Player.TurnKind> TURN_KIND_SERDE = Serde.oneOf(Player.TurnKind.ALL);

    public static final Serde<Card> CARD_SERDE = Serde.oneOf(Card.ALL);

    public static final Serde<Route> ROUTE_SERDE = Serde.oneOf(ChMap.routes());

    public static final Serde<Ticket> TICKET_SERDE = Serde.oneOf(ChMap.tickets());

    public static final Serde<List<String>> STRING_LIST_SERDE = Serde.listOf(STRING_SERDE, ',');

    public static final Serde<List<Card>> CARD_LIST_SERDE = Serde.listOf(Serde.bagOf(CARD_SERDE,',' ), ';')
}
