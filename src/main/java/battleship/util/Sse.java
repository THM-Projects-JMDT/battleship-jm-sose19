package battleship.util;

import java.util.function.Consumer;

import io.javalin.serversentevent.SseClient;

public class Sse {
    private static Runnable close = () -> {
        //TODO
    };
    
    public static Consumer<SseClient> init = client -> {
        //TODO
        client.onClose(close);
    };
}