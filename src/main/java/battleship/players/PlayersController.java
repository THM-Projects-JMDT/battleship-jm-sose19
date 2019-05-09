package battleship.players;

import io.javalin.Handler;

public class PlayersController {
    public static Handler setName = ctx -> {
        ctx.sessionAttribute(ctx.formParam("name"));
        //TODOD
        ctx.result("wait for player Two");
    };
}