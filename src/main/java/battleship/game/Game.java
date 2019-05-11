package battleship.game;

import java.util.UUID;

public class Game {
    private String id;
    protected Player player1;
    protected Player player2;
    private int state = 0;

    public Game() {
        id = UUID.randomUUID().toString();
    }

    public int getState() { return this.state; }

    public void joingame(Player p){
        if(state == 0)
            player1 = p;
        else
            player2 = p;
        state++;
    }

    public String getId() {
        return id;
    }
}
