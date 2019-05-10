package battleship.game;

import java.util.UUID;

public class Game {
    private String id;
    protected Player player1;
    protected Player player2;
    private int state=0;

    public Game() {
        id= UUID.randomUUID().toString();
        this.player1 = new Player("player1");
        this.player2 = new Player("player2");
    }

    public void joingame(Player p1){
        if(state==0)
            player1=p1;
        else
            player2=p1;
        state++;
    }

    public String getId() {
        return id;
    }
}
