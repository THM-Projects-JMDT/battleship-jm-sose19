package battleship.players;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import battleship.game.Player;
import io.javalin.Context;
import io.javalin.serversentevent.SseClient;

public class Players {
    private static Set<Player> players = new HashSet<>();

    //Add new Player
    public static void newPlayer(Context ctx) {
        String id = generateID();
        players.add(new Player(id));
        ctx.sessionAttribute("Player-ID", id);
    }

    //Get Player by id
    private static Player getPlayer(Context ctx)  throws NoSuchElementException {
        return players.stream()
            .filter(p -> p.getID().equals(ctx.sessionAttribute("Player-ID")))
            .findFirst()
            .orElseThrow();
    }

    //Test if has a Game that has one Player
    public static boolean hasGame(Context ctx) {
        return getPlayer(ctx).getGame().getState() <= 1;
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