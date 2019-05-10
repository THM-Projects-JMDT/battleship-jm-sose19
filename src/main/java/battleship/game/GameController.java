package battleship.game;

import battleship.players.Players;
import battleship.util.Path;
import io.javalin.Context;
import io.javalin.Handler;

public class GameController {
    public static Handler newGame = ctx -> {
        Players.newPlayer(ctx);
        //TODO new Game usw.
        loadLogin(ctx);
    };

    public static Handler joinGame = ctx -> {
        Players.newPlayer(ctx);
        System.out.println(ctx.sessionAttribute("Player-ID") + "");
        loadLogin(ctx);
    };

    private static void loadLogin(Context ctx) {
        ctx.header("Content-ID", "1");
        ctx.render(Path.Pages.LOGIN);
    }
}