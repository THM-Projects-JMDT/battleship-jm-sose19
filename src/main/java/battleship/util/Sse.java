package battleship.util;

import java.util.function.Consumer;

import battleship.game.Player;
import battleship.players.Players;
import io.javalin.serversentevent.SseClient;

//TO send ctx.render with sse: client.sendEvent(Key, client.ctx.render(path).resultString());

public class Sse {
    private static Runnable close = () -> {
        //TODO
    };
    
    public static Consumer<SseClient> init = client -> {
        //Client zum Player Hinzufügen (Durch AccesManager ist Sichergestellt das Player-id da und valide)
        Players.getPlayer(client.ctx).setClient(client);
        client.sendEvent("Conection", "Conected");
        client.onClose(close);
    };

    public static void playerConect(Player p, String name) {
        p.getClient().sendEvent("PlayerConect", name + " conected to the Game!");
    }

    public static void updateMyships(Player p) {
        p.getClient().sendEvent("UpdateMyships", p.getshipstatus());
    }

    public static void updateEnemyships(Player po) {
        po.getClient().sendEvent("UpdateEnemyships", po.getshipstatus());
    }

    public static void wait(Player p) {
        p.getClient().sendEvent("Wait", "Wait");
    }

    public static void changeBoards(Player p, Player po) {
        p.getClient().sendEvent("UpdateEnemyboard", p.getfield(false, true));
        po.getClient().sendEvent("StartGame", "StartGame");
        po.getClient().sendEvent("UpdateEnemyboard", po.getfield(false, true));
    }

    public static void boardChanged(Player p, Player po) {
        p.getClient().sendEvent("UpdateEnemyships", po.getshipstatus());
        po.getClient().sendEvent("UpdateMyships", po.getshipstatus());
        po.getClient().sendEvent("UpdateEnemyboard", po.getfield(false, true));
    }

    public static void finish(Player winner, Player po) {
        winner.getClient().sendEvent("YouWon", "You Won the Game");
        po.getClient().sendEvent("YouLose", "You lose the Game");
    }

    public static void deletetGame(Player p) {
        p.getClient().sendEvent("QuitGame", "Other Player quit the Game!");
    }

    public static void closeConection(Player p) {
        p.getClient().sendEvent("Conection", "Disconnected");
    }
}