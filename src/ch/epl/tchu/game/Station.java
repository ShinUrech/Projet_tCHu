package ch.epl.tchu.game;

import ch.epl.tchu.Preconditions;

public final class Station {

    private final int id;
    private final String name;


    public Station(int id, String name){

        Preconditions.checkArgument(id > 0);
        this.id = id;
        this.name = name;

    }

    public int id(){
        return id;
    }

    public String name(){
        return name;
    }

    @Override
    public String toString(){
        return name();
    }

}
