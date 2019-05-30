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
        Player p = Players.getPlayer(client.ctx);
        Player po = p.getGame().otherPlayer(p);
        p.setClient(client);
        client.sendEvent("Conection", "Conected");
        gameInit(p, po);
        client.onClose(close);
    };

    public static void gameInit(Player p, Player po) {
        //TODO chek if in second game phase and do then other things 
        if(hasClient(p)) {
            updateMyships(p);
            getSetBoard(p);
            sendGameID(p);
        }
        if(po != null && hasClient(po)) {
            updateEnemyships(p, po);
            updateEnemyships(po, p);
        }
    }

    public static void playerConect(Player po, String name) {
        if(hasClient(po))
            po.getClient().sendEvent("PlayerConect", name + " conected to the Game!");
    }

    public static void sendGameID(Player p) {
        if(hasClient(p))
            p.getClient().sendEvent("GameID", p.getGame().getId());
    }

    public static void deleteGameID(Player p) {
        if(hasClient(p))
        //TODo vtl mit ids 
            p.getClient().sendEvent("delGameID", "delGameID");
    }

    public static void getSetBoard(Player p) {
        if(hasClient(p))
            p.getClient().sendEvent("updateMyBoard", p.getfield(true, true));
    }

    public static void updateMyBoard(Player p, Player po) {
        if(hasClient(p))
            p.getClient().sendEvent("updateMyBoard", po.getfield(true, false));
    }

    public static void updateEnemyBoard(Player p) {
        if(hasClient(p))
            p.getClient().sendEvent("UpdateEnemyboard", p.getfield(false, true));
    }

    public static void updateMyships(Player p) {
        if(hasClient(p))
            p.getClient().sendEvent("UpdateMyships", p.getshipstatus());
    }

    public static void updateEnemyships(Player p, Player po) {
        if(hasClient(p))
            p.getClient().sendEvent("UpdateEnemyships", po.getshipstatus());
    }

    public static void wait(Player p) {
        if(hasClient(p))
            p.getClient().sendEvent("Wait", p.getClient().ctx.render(Path.Pages.WAIT).resultString());
    }

    public static void changeBoards(Player p, Player po) {
        updateEnemyBoard(p);
        updateEnemyBoard(po);
        updateMyBoard(po, p);
        if(hasClient(po))
            po.getClient().sendEvent("StartGame", "StartGame");
    }

    public static void boardChanged(Player p, Player po) {
        updateEnemyships(p, po);
        updateMyships(po);
        updateEnemyBoard(po);
    }

    public static void finish(Player winner, Player po) {
        if(hasClient(winner))
            winner.getClient().sendEvent("YouWon", "You Won the Game");
        if(hasClient(po))
            po.getClient().sendEvent("YouLose", "You lose the Game");
    }

    public static void deletetGame(Player p) {
        if(hasClient(p))
            p.getClient().sendEvent("QuitGame", "Other Player quit the Game!");
    }

    public static void closeConection(Player p) {
        if(hasClient(p))
            p.getClient().sendEvent("Conection", "Disconnected");
    }
    private static boolean hasClient(Player p) {
        return p.getClient() != null;
    }
}