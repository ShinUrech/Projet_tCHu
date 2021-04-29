package ch.epfl.tchu.net;

import java.util.List;

/**
 * This Enum represents all types of information clients will exchange with the game server.
 *
 * @author Shin Urech (327245)
 */
public enum MessageId {

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
     * Complete list containing all attributes of this enum.
     */
    public static final List<MessageId> ALL = List.of(MessageId.values());

    /**
     * The number of elements in this list.
     */
    public static final int COUNT = ALL.size();

}