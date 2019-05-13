package battleship.players;

import battleship.util.Path;
import io.javalin.Handler;

public class PlayersController {
    public static Handler setName = ctx -> {
        ctx.sessionAttribute("Name", ctx.formParam("Name"));
        if(!Players.hasGame(ctx)) {
            //TODO joing game wit formParam("Game-ID")
        }
        //TODO render game(set boats)
        ctx.result("wait for player Two");
    };

    public static Handler setBoat = ctx -> {
        //TODO
    };

    public static Handler move = ctx -> {
        //TODO 
    };

    public static Handler removePlayer = ctx -> {
        Players.removePlayer(ctx);
        ctx.header("Content-ID", "0");
        ctx.render(Path.Pages.START);
    };
}