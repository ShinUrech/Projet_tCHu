package ch.epl.tchu;

public final class Preconditions {

    /**
     * method that simplifies the testing of preconditions
     *
     * @author Shin Urech (327245)
     *
     */
    private Preconditions(){}

    /**
     * this method throws an exception if the inputted boolean statement returns false
     * @param shouldBeTrue
     */
    public static void checkArgument(boolean shouldBeTrue){

        if(!shouldBeTrue){
            throw new IllegalArgumentException();
        }

    }
}
