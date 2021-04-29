package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;

public interface Serde<T> {


    /**
     * abstract method that serializes an object of type T into a string
     * @param t is an instance of a generic type T
     * @return a string that represents the variable t in a certain encoding
     */
    abstract String serialize(T t);


    /**
     * abstract method that takes a string as an imput and returns the corresponding object of type T
     * @param string is the serialized object that need to be deserialized
     * @return the corresponding object of type T
     */
    abstract T deserialize(String string);


    /**
     * this method creates an object called a serde that is used to contain a serialization method and a deserialization method
     * for a certain type T
     * @param serializer is the serialization method for objects of type T (a function from T to String)
     * @param deserializer is the deserialization method for objects of type T (a function from String to T)
     * @param <T> is the type of the objects we want to serialize/deserialize
     * @return either a serialization method or a deserialisation method depending on the caller's need
     */
   static <T> Serde<T> of(Function<T, String> serializer, Function<String, T> deserializer){

        return new Serde<T>() {

            @Override
            public String serialize(T t) {
              return serializer.apply(t);
            }

            @Override
            public T deserialize(String string) {
                return deserializer.apply(string);
            }
        };
   }

    /**
     * this method is used to make a Serde for enum values
     * @param list is the list that contains all attributes of an enum
     * @param <T> is the type of objects contained in the list of all enum attributes
     * @return a Serde for Enum values (the serializer returns the string representation of the object's index where as
     * the deserializer returns the object at the index given in the inputted String)
     */
  static <T> Serde<T> oneOf(List<T> list){

       return Serde.of(
               //serial
              anObject -> {
                  return (anObject != null && list.contains(anObject))
                       ? Integer.toString(list.indexOf(anObject))
                       : "";

               },
               //deserialize
               deserialize -> {
                  return (!deserialize.isEmpty())
                          ? list.get(Integer.parseInt(deserialize))
                          : null;
              }


       );
   }

    /**
     * This method returns a Serde for entire lists. The serlialized list is a single String that contains all objects in the list,
     * each of them separated from eachother with a separation character.
     * @param serde is the serde that will be used to serialize a list containing a certain type of objects
     * @param separator is the seperation character that will separate each element of the list when serialized into a string
     * @param <T> is the generic type of the objects we will serialize
     * @return a serde for a list that contains a certain type (a list is serialized into a single string with elements
     * split by the separation Character separator)
     */
   static <T> Serde<List<T>> listOf(Serde<T> serde, Character separator) {

       return Serde.of(list -> {

           if(!list.isEmpty()){
               ArrayList<String> elementsArray = new ArrayList<>();

               for (T element : list) {
                   elementsArray.add(serde.serialize(element));
               }
               return String.join(separator.toString(), elementsArray);

           } else return "";

           },
               string -> {

           if(!string.isEmpty()) {

               ArrayList<T> deserialized = new ArrayList<>();

               String[] splittedString = string.split(Pattern.quote(separator.toString()), -1);

               for (String element : splittedString) {
                   deserialized.add(serde.deserialize(element));
               }

               return deserialized;
           } else return List.of();

       });

   }

    /**
     * This method makes a Serde for SortedBags containing a certain type of objects.
     * @param serde is the serde that will be used to serialize each individual element of the SortedBag
     * @param separator is the character that will be used to separate each element of the SortedBag when serialized into a single string
     * @param <T> generic type of objects contained in the SortedBag
     * @return a Serde for sortedBags of a specific type T (a serialized sortedBag is a string that contains every element
     * in the sortedBag split by a separation character)
     */
   static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, Character separator){
       return Serde.of(

               sortedBag -> {
                  return (sortedBag.isEmpty())
                          ? ""
                          : Serde.listOf(serde, separator).serialize(sortedBag.toList());
                  },

               string -> {
                   return (string.isEmpty())
                           ? SortedBag.of()
                           : SortedBag.of(Serde.listOf(serde, separator).deserialize(string));

               }

       );
   }


}
