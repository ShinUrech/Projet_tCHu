package ch.epfl.tchu.game;

import java.util.List;

public final class StationPartition implements StationConnectivity {


    private final int[] stationPartition;

    @Override
    public boolean connected(Station s1,Station s2){
       return stationPartition[s1.id()]== stationPartition[s2.id()];
    }

    private StationPartition(int[] stationPartition){
        this.stationPartition = stationPartition.clone();
    }

    public static final class Builder {

        public Builder(int stationCount){

        }
    }

}
