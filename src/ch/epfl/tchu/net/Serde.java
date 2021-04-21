package ch.epfl.tchu.net;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;

public interface Serde<T> {


    abstract String serialize(T t);


    abstract T deserialize(String string);


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

  static <T> Serde<T> oneOf(List<T> list){

       return new Serde<T>() {

           @Override
           public String serialize(T t) {
               return Integer.toString(list.indexOf(t));
           }

           @Override
           public T deserialize(String string) {
               return list.get(Integer.parseInt(string));
           }
       };
   }

   static <T> Serde<List<T>> listOf(Serde<T> serde, Character separator) {

       return new Serde<List<T>>() {

           @Override
           public String serialize(List<T> ts) {

               ArrayList<String> elementsArray = new ArrayList<>();

                   for (T element : ts) {
                       elementsArray.add(serde.serialize(element));
                   }
                   return String.join(separator.toString(), elementsArray);

           }


           @Override
           public List<T> deserialize(String string) {

               ArrayList<T> deserialized = new ArrayList<>();

               String[] splittedString = string.split(Pattern.quote(separator.toString()), -1);

               for (String element : splittedString) {
                   deserialized.add(serde.deserialize(element));
               }

               return deserialized;
           }
       };

   }

   static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, Character separator){
       return new Serde<SortedBag<T>>() {
           @Override
           public String serialize(SortedBag<T> sortedBag) {
               return Serde.listOf(serde, separator).serialize(sortedBag.toList());
           }

           @Override
           public SortedBag<T> deserialize(String string) {
               return SortedBag.of(Serde.listOf(serde, separator).deserialize(string));
           }
       };
   }
}
