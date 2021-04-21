package ch.epfl.tchu.net;

import ch.epfl.tchu.net.Serde;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Serdes {

    private Serdes(){};

    public static final Serde<Integer> INTEGER_SERDE = Serde.of((Integer x, Byte[] y) -> Base64.getEncoder().encodeToString(x.toString().getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(new String(y, StandardCharsets.UTF_8))));
    

}
