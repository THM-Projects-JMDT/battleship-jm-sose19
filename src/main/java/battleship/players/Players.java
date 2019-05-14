package battleship.players;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import battleship.game.Game;
import battleship.game.Player;
import io.javalin.Context;
import io.javalin.serversentevent.SseClient;

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

    public static void removePlayer(Context ctx) {
        players.remove(getPlayer(ctx));
        //TODO sessionAttribute löschen ? 
    }

    //Get Player by id
    public static Player getPlayer(Context ctx)  throws NoSuchElementException {
        return players.stream()
            .filter(p -> p.getID().equals(ctx.sessionAttribute("Player-ID")))
            .findFirst()
            .orElseThrow();
    }

    //Test if has a Game that has one Player
    public static boolean hasGame(Context ctx) {
        return getPlayer(ctx).getGame() != null;
    }

    public static Game getGame(Context ctx) throws NoSuchElementException {
        return players.stream()
                .filter(p -> p.getGame() != null)
                .map(p -> p.getGame())
                .filter(g -> g.getId().equals(ctx.queryParam("Game")))
                .findFirst()
                .orElseThrow();
    }

    //Server Send Events
    public static void connect(SseClient client) {
        getPlayer(client.ctx).setClient(client);
    }
    
    public static SseClient getClient(Context ctx) throws NoSuchElementException{
        Player p = getPlayer(ctx);
        if(p.getClient() == null)
            throw new NoSuchElementException();
        return p.getClient();
    
    }
    public static void disconect(Context ctx) {
        getPlayer(ctx).setClient(null);;
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