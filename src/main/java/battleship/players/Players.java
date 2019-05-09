package battleship.players;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import battleship.game.Player;
import io.javalin.serversentevent.SseClient;

public class Players {
    private static Map<String, Player> players = new HashMap<>();

    //Add new Player
    public static String newPlayer() {
        String id = generateID();
        players.put(id, new Player());
        return id;
    }

    //Get Player by id
    private static Player getPlayer(String id) {
        return players.get(id);
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
        return getPlayer(id) != null;
    }

    private Players() {};
}