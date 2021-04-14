package ch.epfl.tchu;

/**
 * A class that simplifies the testing of preconditions.
 *
 * @author Shin Urech (327245)
 */
public final class Preconditions {

    private Preconditions(){}

    /**
     * This method throws an exception if the inputted boolean statement returns false.
     *
     * @param shouldBeTrue
     * the indication that should be correct.
     * @throws IllegalArgumentException if the given indication is false
     */
    public static void checkArgument(boolean shouldBeTrue){

        if(!shouldBeTrue){
            throw new IllegalArgumentException();
        }
    }
}
