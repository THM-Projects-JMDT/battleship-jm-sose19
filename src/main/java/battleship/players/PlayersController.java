package battleship.players;

import io.javalin.Handler;

public class PlayersController {
    public static Handler setName = ctx -> {
        ctx.sessionAttribute(ctx.formParam("Name"));
        //TODOD
        ctx.result("wait for player Two");
    };
}