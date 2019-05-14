package battleship.pages;

import battleship.players.Players;
import battleship.util.Path;
import io.javalin.Handler;

public class PageController {
    public static Handler getPage = ctx -> {
        if(!Players.hasGame(ctx) || Players.hasGameState(ctx, 1)) {
            ctx.header("Content-ID", "3");
            ctx.render(Path.Pages.LOGIN);
            return;
        }
        
        ctx.header("Content-ID", "0");
        ctx.render(Path.Pages.GAME);
    };
}