package battleship.players;

import io.javalin.Handler;

public class PlayersController {
    public static Handler setName = ctx -> {
        ctx.sessionAttribute("Name", ctx.formParam("Name"));
        //TODO
        ctx.result("wait for player Two");
    };
}