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
    
    public boolean joingame(Player p) {
        if(state == 1){
            player2 = p;
            state++;
            return true;
        }
        return false;
    }

    public boolean delete(Player p) {
        if(player1.equals(p))
            return Players.remove(player2);
        else if(player2.equals(p))
            return Players.remove(player1);
        return false;
    }
}
