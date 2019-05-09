package battleship.game;

import battleship.players.Players;
import io.javalin.Handler;

public class GameController {
    public static Handler createGame = ctx -> {
        String id = Players.newPlayer();
        ctx.sessionAttribute("player-id", id);
        //TODO new Game usw.
        //TODO Redirect to game Login
        ctx.result("newGame");
    };
}