package ch.epl.tchu;

public final class Preconditions {

    //some random BS idk
    private Preconditions(){}

    //method to make sure everything goes well
    public static void checkArgument(boolean shouldBeTrue){

        if(!shouldBeTrue){
            throw new IllegalArgumentException();
        }

    }
}
