package ch.epfl.tchu.game;

import java.util.List;

/**
 * This class represents all player's ID (2 for this game).
 *
 * @author Shin Urech (327245)
 */

public enum PlayerId {

    PLAYER_1,
    PLAYER_2;

    /**
     * List that consists of all player IDs.
     */
    public static final List<PlayerId> ALL = List.of(PlayerId.values());

    /**
     * Number of players that are involved in a game.
     */
    public static final int COUNT = ALL.size();

    /**
     * Returns next player's ID.
     * @return next player's ID.
     */
    public PlayerId next(){
        if(this.ordinal() == 0) {
            return ALL.get(1);
        }else{
           return ALL.get(0);
        }
    }
}
