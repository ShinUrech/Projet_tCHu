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
