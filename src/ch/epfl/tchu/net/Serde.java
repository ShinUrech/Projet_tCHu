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

                if(t == null){
                    return "";
                } else return serializer.apply(t);
            }

            @Override
            public T deserialize(String string) {

                if(string.isEmpty()){
                    return null;
                }else return deserializer.apply(string);
            }
        };
   }

  static <T> Serde<T> oneOf(List<T> list){
       return new Serde<T>() {
           @Override
           public String serialize(T t) {
               if(t != null && list.contains(t)){
                   return Integer.toString(list.indexOf(t));
               }else return "";
           }

           @Override
           public T deserialize(String string) {
               if(string.isEmpty()){
                   return null;
               } else return list.get(Integer.parseInt(string));
           }
       };
   }

   static <T> Serde<List<T>> listOf(Serde<T> serde, Character separator) {

       return new Serde<List<T>>() {

           @Override
           public String serialize(List<T> ts) {

               if(!ts.isEmpty()) {
                   ArrayList<String> elementsArray = new ArrayList<>();

                   for (T element : ts) {
                       elementsArray.add(serde.serialize(element));
                   }

                   return String.join(separator.toString(), elementsArray);
               } else return "";

           }


           @Override
           public List<T> deserialize(String string) {

               if(!string.isEmpty()) {
                   ArrayList<T> deserialized = new ArrayList<>();

                    String[] splittedString = string.split(Pattern.quote(separator.toString()), -1);

                     for (String element : splittedString) {
                        deserialized.add(serde.deserialize(element));
                     }

                     return deserialized;
               } else return List.of();

           }
       };

   }

   static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde<T> serde, Character separator){
       return Serde.of(
               sortedBag -> Serde.listOf(serde, separator).serialize(sortedBag.toList()),
               string -> SortedBag.of(Serde.listOf(serde, separator).deserialize(string))
       );
   }
}
