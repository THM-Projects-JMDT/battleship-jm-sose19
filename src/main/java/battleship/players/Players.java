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
        ctx.sessionAttribute("player-id", id);
    }

    //Get Player by id
    private static Player getPlayer(String id)  throws NoSuchElementException {
        return players.stream()
            .filter(p -> p.getID().equals(id))
            .findFirst()
            .orElseThrow();
    }

    //Server Send Events
    public static void connect(String id, SseClient client) {
        getPlayer(id).SetClient(client);
    }
    
    public static SseClient getClient(String id) throws NoSuchElementException{
        Player p = getPlayer(id);
        if(p.getClient() == null)
            throw new NoSuchElementException();
        return p.getClient();
    
    }
    public static void disconect(String id) {
        getPlayer(id).SetClient(null);;
    }

    //ID Generation
    private static String generateID() {
        return UUID.randomUUID().toString();
    }
    //Test is Player
    public static boolean isPlayer(String id) {
        //Noch besser lösen?
        try {
            getPlayer(id);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private Players() {};
}