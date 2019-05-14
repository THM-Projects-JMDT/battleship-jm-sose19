package battleship.game;

import java.util.UUID;
import battleship.players.Players;

public class Game {
    private String id;
    protected Player player1;
    protected Player player2;
    private int state = 0;

    public Game(Player p) {
        player1 = p;
        state++;
        id = UUID.randomUUID().toString();
    }

    public int getState() { 
        return this.state; 
    }

    public String getId() {
        return id;
    }

    public Player otherPlayer(Player p) {
        if(player1.equals(p))
            return player2;
        else if(player2.equals(p))
            return player1;
            
        return null;
    }
    
    public boolean joingame(Player p) {
        if(state == 1){
            player2 = p;
            state++;
            return true;
        }
        return false;
    }

    public boolean delete(Player player) {
        Player p = otherPlayer(player);
        if(p == null)
            return false;
        
        return Players.remove(p);
    }
}
