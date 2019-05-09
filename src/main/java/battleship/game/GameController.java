package battleship.game;

import battleship.players.Players;
import io.javalin.Handler;

public class GameController {
    public static Handler newGame = ctx -> {
        String id = Players.newPlayer();
        ctx.sessionAttribute("player-id", id);
        //TODO new Game usw.
        //TODO Redirect oder Render to game Login?
        ctx.result("newGame");
    };
}