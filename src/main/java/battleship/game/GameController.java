package battleship.game;

import battleship.players.Players;
import battleship.util.Path;
import battleship.util.Sse;
import io.javalin.BadRequestResponse;
import io.javalin.Context;
import io.javalin.Handler;

public class GameController {
    public static Handler newGame = ctx -> {
        Players.newPlayer(ctx).newGame();
        loadLogin(ctx);
    };

    public static Handler joinGame = ctx -> {
        Players.newPlayer(ctx);
        loadLogin(ctx);
    };

    public static Handler getGameid = ctx -> {
        if(Players.hasGame(ctx)) {
            ctx.header("Content-ID", "1");
            ctx.result(Players.getPlayer(ctx).getGame().getId());
            return;
        }
        
        ctx.header("Content-ID", "1");
        throw new BadRequestResponse("Player has no Game");
    };

    private static void loadLogin(Context ctx) {
        ctx.header("Content-ID", "3");
        ctx.render(Path.Pages.LOGIN);
    }
}