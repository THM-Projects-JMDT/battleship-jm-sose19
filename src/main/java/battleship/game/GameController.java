package battleship.game;

import battleship.players.Players;
import battleship.util.Path;
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

    public static Handler aboutGame = ctx -> {
        ctx.header("Content-ID", "5");
        ctx.render(Path.Pages.ABOUT);
    };

    public static Handler getGameid = ctx -> {
        Player p = Players.getPlayer(ctx);
        if(Players.hasGame(p)) {
            ctx.header("Content-ID", "1");
            ctx.result(p.getGame().getId());
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