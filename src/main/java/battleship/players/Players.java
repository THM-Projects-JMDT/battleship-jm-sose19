package battleship.players;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import battleship.game.Game;
import battleship.game.Player;
import battleship.util.Sse;
import io.javalin.Context;

public class Players {
    private static Set<Player> players = new HashSet<>();

    //Add new Player
    public static Player newPlayer(Context ctx) {
        String id = generateID();
        Player p = new Player(id);
        players.add(p);
        ctx.sessionAttribute("Player-ID", id);
        return p;
    }

    //Remove Player close SSe and delet Game
    public static boolean removeWithGame(Context ctx) {
        if(!isPlayer(ctx) || !hasGame(ctx))
            return false;
        
        Player p = getPlayer(ctx);
        
        Sse.deleteGameID(p);
        Sse.closeConection(p);
        
        players.remove(p);
        return p.getGame().delete(p);
        //TODO sessionAttribute löschen ? 
        //TODO SSE benenden?
    }

    //Remove Player and Close Sse
    public static boolean remove(Player p) {
        Sse.deletetGame(p);
        Sse.deleteGameID(p);
        Sse.closeConection(p);
        
        return players.remove(p);
        //TODO sessionAttribute löschen ? 
        //TODO SSE benenden?
    }

    //Get Player by id
    public static Player getPlayer(Context ctx)  throws NoSuchElementException {
        return players.stream()
            .filter(p -> p.getID().equals(ctx.sessionAttribute("Player-ID")))
            .findFirst()
            .orElseThrow();
    }

    //Test if player has a game
    public static boolean hasGame(Player p) {
        return p.getGame() != null;
    }
    public static boolean hasGame(Context ctx) {
        return getPlayer(ctx).getGame() != null;
    }
    //Test if Player has a game with status
    public static boolean hasGameState(Player p, int state) {
        if(!hasGame(p))
            return false;
        return p.getGame().getState() == state;
    }


    public static Game getGame(Context ctx) throws NoSuchElementException {
        return players.stream()
                .filter(p -> p.getGame() != null)
                .map(p -> p.getGame())
                .filter(g -> g.getId().equals(ctx.queryParam("Game")))
                .findFirst()
                .orElseThrow();
    }

    //ID Generation
    private static String generateID() {
        return UUID.randomUUID().toString();
    }
    //Test is Player
    public static boolean isPlayer(Context ctx) {
        //Noch besser lösen?
        try {
            getPlayer(ctx);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private Players() {};
}