package ch.epl.tchu.game;

import ch.epl.tchu.Preconditions;

/**
 * A class to represent a Station.
 *
 * @author Aidas Venckunas (325464)
 * @author Shin Urech (327245)
 */
public final class Station {

    private final int id;
    private final String name;


    /**
     * Constructs a Station with its' following identification.
     *
     * @param id
     * The ID of a station.
     * @throws IllegalArgumentException if the ID is negative
     * @param name
     * The name of a station.
     */
    public Station(int id, String name){

        Preconditions.checkArgument(id >= 0);
        this.id = id;
        this.name = name;

    }

    /**
     * Returns the ID of a station.
     * @return the ID of a station
     */
    public int id(){
        return id;
    }

    /**
     * Returns the name of a station.
     * @return the name of a station
     */
    public String name(){
        return name;
    }

    /**
     * Overrides the toString method so that it
     * returns the name of a station.
     * @return the name of a station
     */
    @Override
    public String toString(){
        return name();
    }

}
