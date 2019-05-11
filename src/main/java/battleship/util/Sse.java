package battleship.util;

import java.util.function.Consumer;

import battleship.players.Players;
import io.javalin.serversentevent.SseClient;

public class Sse {
    private static Runnable close = () -> {
        //TODO
    };
    
    public static Consumer<SseClient> init = client -> {
        //Client zum Player Hinzufügen (Durch AccesManager ist Sichergestellt das Player-id da und valide)
        Players.connect(client);
        client.sendEvent("conection", "conected");
        client.onClose(close);
    };
}