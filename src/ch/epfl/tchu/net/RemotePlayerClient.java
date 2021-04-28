package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;

import java.net.Socket;

public class RemotePlayerClient {

    private Player player;
    private String hostName;
    private int port;

    public  RemotePlayerClient(Player player, String hostName, int port){
        this.player = player;
        this.hostName = hostName;
        this.port = port;
    }
}
