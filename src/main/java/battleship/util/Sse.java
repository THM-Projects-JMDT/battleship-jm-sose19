package battleship.util;

import java.util.function.Consumer;

import battleship.game.Player;
import battleship.players.Players;
import io.javalin.serversentevent.SseClient;

public class Sse {
    private static Runnable close = () -> {
        //TODO
    };
    
    public static Consumer<SseClient> init = client -> {
        //Client zum Player Hinzufügen (Durch AccesManager ist Sichergestellt das Player-id da und valide)
        Players.connect(client);
        client.sendEvent("Conection", "Conected");
        client.onClose(close);
    };

    public static void playerConect(SseClient client, String name) {
        client.sendEvent("PlayerConect", name + " conected to the Game!");
    }

    public static void boardChanged(SseClient client, SseClient clientOther) {
        client.sendEvent("UpdateEnemyships", "UpdateEnemyships");
        clientOther.sendEvent("UpdateMyships", "UpdateMyships");
        clientOther.sendEvent("UpdateEnemyboard", "UpdateEnemyboard");
    }

    public static void finish(SseClient clientWinner, SseClient clientOther) {
        clientWinner.sendEvent("YouWon", "You Won the Game");
        clientOther.sendEvent("YouLose", "You lose the Game");
    }

    public static void deletetGame(SseClient client) {
        client.sendEvent("QuitGame", "Other Player quit the Game!");
    }

    public static void closeConection(SseClient client) {
        client.sendEvent("Conection", "Disconnected");
    }
}