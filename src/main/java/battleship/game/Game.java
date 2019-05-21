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
        if(player2 == null){
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

    public boolean move(Player p, int position) {
        if(state < 4 || state > 6)
            return false;

        Player po = otherPlayer(p);
        if(po.move(position)) {
            if(!po.hitBoat()) {
                changeTurn();
            } 
            return true;
        }
        return false;
    }

    private void changeTurn() {
        player1.changeMyTurn();
        player2.changeMyTurn();
    }

    public boolean isFinished() {
		if(player1.checkifend() || player2.checkifend()) {
            state = 7;
            player1.beendegame();
            player2.beendegame();
            return true;
        }
        
        return false;
    }
    
    public Player winner() {
        if(player1.checkifend())
            return player2;
        
        if(player2.checkifend())
            return player1;
        
        return null;
    }

    public void Stateadd(){
        state++;
    }
}
