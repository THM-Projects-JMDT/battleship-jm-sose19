package battleship.game;

public class Game {
    protected Player player1;
    protected Player player2;

    public Game() {

        this.player1 = new Player("player1");
        this.player2 = new Player("player2");

    }
}
