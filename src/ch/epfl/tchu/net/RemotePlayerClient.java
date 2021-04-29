package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;

import java.io.*;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class RemotePlayerClient {

    private Player player;
    private String hostName;
    private int port;

    public  RemotePlayerClient(Player player, String hostName, int port){
        this.player = player;
        this.hostName = hostName;
        this.port = port;
    }

    public void run(){
        try (Socket s = new Socket(hostName, port);
             BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream(), US_ASCII));
             BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), US_ASCII))){

             String message = r.readLine();
             String data[] = message.split(Pattern.quote(" "), -1);
             MessageId messageType = MessageId.valueOf(data[0]);

             switch(messageType) {

                 case INIT_PLAYERS:

                     PlayerId ownId = Serdes.PLAYER_ID_SERDE.deserialize(data[1]);
                     List<String> namesList = Serdes.STRING_LIST_SERDE.deserialize(data[2]);
                     Map<PlayerId, String> names = new EnumMap<PlayerId, String>(PlayerId.class);

                     names.put(PlayerId.PLAYER_1, namesList.get(0));
                     names.put(PlayerId.PLAYER_2, namesList.get(1));

                     player.initPlayers(ownId, names);
             }
        } catch (IOException e) {
            throw  new UncheckedIOException (e);
        }
    }
}
