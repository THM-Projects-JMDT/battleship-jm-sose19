package battleship.pages;

import battleship.game.Player;
import battleship.players.Players;
import battleship.util.Path;
import battleship.util.Sse;
import io.javalin.Handler;

public class PageController {
    public static Handler getPage = ctx -> {
        if(!Players.isPlayer(ctx)) {
            ctx.header("Content-ID", "0").render(Path.Pages.START);
            return;
        }

        Player p = Players.getPlayer(ctx);

        if(!Players.hasGame(p)) {
            ctx.header("Content-ID", "3");
            ctx.render(Path.Pages.LOGIN);
            return;
        }
        
        //If Player has no SSe client force to Conect 
        if(p.getClient() == null)
            ctx.header("Content-ID", "4");
        else
            ctx.header("Content-ID", "0");
        
        ctx.render(Path.Pages.GAME);
        Sse.gameInit(p, p.getGame().otherPlayer(p));
    };
}