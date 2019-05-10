package battleship.game;

import battleship.players.Players;
import battleship.util.Path;
import io.javalin.Handler;

public class GameController {
    public static Handler newGame = ctx -> {
        Players.newPlayer(ctx);
        ctx.header("Content-ID", "1");
        //TODO new Game usw.
        ctx.render(Path.Pages.LOGIN);
    };
}