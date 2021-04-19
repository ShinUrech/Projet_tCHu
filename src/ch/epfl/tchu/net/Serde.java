package ch.epfl.tchu.net;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

   static <T extends Enum> Serde<List<T>> oneOf(List<T> list){
       return new Serde<List<T>>() {
           @Override
           public String serialize(List<T> ts) {
               return String.join(",", Serde.listToStringForEnum(ts));
           }

           @Override
           public List<T> deserialize(String string) {
               return List.of(T.values());
           }
       };
   }

   private static <T extends Enum>String[] listToStringForEnum(List<T> list){
       String[] toStringList = new String[list.size()];

       for(T element: list){
           toStringList[element.ordinal()] = Integer.toString(element.ordinal());
       }

       return toStringList;
   }




}
