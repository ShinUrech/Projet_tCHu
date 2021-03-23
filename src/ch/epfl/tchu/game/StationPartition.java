package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.Random;

/**
 * This class represents a set of connected stations that can be reached by a particular player.
 *
 * @author Shin Urech (327245)
 * @author Aidas Venckunas (325464)
 */

public final class StationPartition implements StationConnectivity {


    private final int[] stationPartition;

    /**
     * Override of the connected method of the interface StationConnectivity.
     *
     * @param s1 first station to check
     * Station one.
     * @param s2 second station to check
     * Station two.
     *
     * @return a boolean statement that states whether or not the two stations are reachable by a single player (returns
     * true if both inputs are the same station but out of bound of the partition array)
     */
    @Override
    public boolean connected(Station s1,Station s2){

       if((s1.id() >= stationPartition.length || s2.id() >= stationPartition.length )) {
           System.out.println(s1.name() + " " + s2.name());
           return s1.id() == s2.id();
       }

        return stationPartition[s1.id()] == stationPartition[s2.id()];
    }

    //this is the private constructor that initialises the station partition
    private StationPartition(int[] stationPartition){
        this.stationPartition = stationPartition.clone();
    }

    public static final class Builder {

        private int[] partitionBuilder;

        /**
         * Public constructor for the station partition builder.
         *
         * @param stationCount is the number of stations that are contained in the station partition
         * @throws IllegalArgumentException if the number of stations is negative
         */
        public Builder(int stationCount){

            Preconditions.checkArgument(stationCount >= 0);

            partitionBuilder = new int[stationCount];

            for(int i = 0; i < stationCount; ++i){
                partitionBuilder[i] = i;
            }
        }

        private int representative(int stationId){
            return partitionBuilder[stationId];
        }

        /**
         * This method "connects" two stations together.
         *
         * @param s1 station 1
         * @param s2 station 2
         *
         * @return the builder after modification
         */
        public Builder connect(Station s1, Station s2) {

            partitionBuilder[representative(s1.id())] = representative(s2.id());

            boolean flat = false;

            while(!flat){
                flat = true;
                for(int i = 0; i < partitionBuilder.length; ++i){
                    if(representative(representative(i)) != representative(i)) {
                        flat = false;
                        partitionBuilder[i] = representative(representative(i));
                    }
                }
            }

            return this;
        }

        /**
         * This method returns a new Station partition made out of the builder.
         *
         * @return a new station partition
         */
        public StationPartition build(){

            boolean flat = false;

            while(!flat){
                flat = true;
                for(int i = 0; i < partitionBuilder.length; ++i){
                    if(representative(representative(i)) != representative(i)) {
                        flat = false;
                        partitionBuilder[i] = representative(representative(i));
                    }
                }
            }

            return new StationPartition(partitionBuilder);
        }
    }

}
