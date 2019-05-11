package battleship.players;

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
}