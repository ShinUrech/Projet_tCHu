package ch.epfl.tchu.net;

import java.time.chrono.IsoEra;
import java.util.List;
import java.util.function.Function;

public interface Serde<E extends MessageId> {
    /**
     * this method serializes a message from the client to the server
     * @param e is a parameter of type E
     * @param <E> is a type of message from the enum MessageId that represents a specific type of message a server needs
     *           to play a game of tCHu
     * @return a String that contains the desired information but serialized
     */
    abstract String serialize(E e);

    /**
     * This method deserializes a message send from the server to the client
     * @param string is the serialized piece of information send by the server to the client
     * @param <E> is the type of return depending on what information has been emitted from the server to the client
     *           (is a type from the enum MessageId)
     * @return a MessageId with informations provided by the server
     */
    abstract E deserialize(String string);

    /**
     * This method creates a Serde (Serializer-Deserializer).
     * @param serializer is a method that serializes a message into a string
     * @param deserializer is a method that deserializes a String into a message
     * @param <T> is the type of message from MessageId that is going to be serialized or deserialized
     * @return a Serde that implements both methods described above to serialize/deserialize a message from the server
     */
    static <T extends MessageId> Serde of(Function<T, String> serializer, Function<String, T> deserializer){
        return new Serde() {

            @Override
            public String serialize(MessageId messageId) {
                return serializer.apply((T) messageId);
            }

            @Override
            public MessageId deserialize(String string) {
                return deserializer.apply(string);
            }

        };
    }

    static <T extends Enum> Serde oneOf(List<T> list){
        return new Serde() {
            @Override
            public String serialize(MessageId messageId) {
                return null;
            }

            @Override
            public MessageId deserialize(String string) {
                return null;
            }
        }
    }


}
