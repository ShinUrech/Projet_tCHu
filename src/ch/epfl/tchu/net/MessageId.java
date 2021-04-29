package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PublicGameState;

import javax.swing.text.PlainDocument;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ch.epfl.tchu.game.Player;

/**
 * This Enum represents all types of informations clients will exchange with the game server
 *
 * @author Shin Urech (327245)
 */
public enum MessageId {


    /**
     * enumeration of all types of message
     */
    INIT_PLAYERS,
    RECEIVE_INFO,
    UPDATE_STATE,
    SET_INITIAL_TICKETS,
    CHOOSE_INITIAL_TICKETS,
    NEXT_TURN,
    CHOOSE_TICKETS,
    DRAW_SLOT,
    ROUTE,
    CARDS,
    CHOOSE_ADDITIONAL_CARDS;

    /**
     * complete list containing all attributes of this enum
     */
    public static final List<MessageId> ALL = List.of(MessageId.values());

    /**
     * getter method for the number of elements in this list
     */
    public static final int COUNT = ALL.size();

}
