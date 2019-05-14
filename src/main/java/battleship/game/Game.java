package battleship.game;

import java.util.UUID;

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

    public int getState() { return this.state; }

    public boolean joingame(Player p){
        if(state == 1){
            player2 = p;
            state++;
            return true;
        }
        return false;
    }

    public String getId() {
        return id;
    }
}
