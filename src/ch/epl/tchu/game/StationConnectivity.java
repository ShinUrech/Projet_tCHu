package ch.epl.tchu.game;

/**
 * An interface to evaluate if two stations are connected.
 *
 * @author Aidas Venckunas (325464)
 * @author Shin Urech (327245)
 */
public interface StationConnectivity {

    /**
     * a method to check the connectivity between two stations.
     *
     * @param s1
     * Station one.
     * @param s2
     * Station two.
     * @return
     * True if stations are connected and false otherwise.
     */
    public abstract boolean connected(Station s1, Station s2);
}
