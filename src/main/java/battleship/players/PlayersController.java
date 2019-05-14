package battleship.players;

import java.util.NoSuchElementException;

import battleship.util.Path;
import io.javalin.BadRequestResponse;
import io.javalin.Handler;

public class PlayersController {
    public static Handler setName = ctx -> {
        ctx.sessionAttribute("Name", ctx.queryParam("Name"));
        if(!Players.hasGame(ctx)) {
            try {
                if(!Players.getPlayer(ctx).newGame(Players.getGame(ctx))) {
                    //TODO Hadle if Player has Already a game (vtl nich nötig wenn überprüft von Acces manager)
                    return;
                }
            } catch(NoSuchElementException ex) {
                ctx.header("Content-ID", "2");
                throw new BadRequestResponse("Invalide Game-ID");
            }
        }
        ctx.header("Content-ID", "4");
        ctx.render(Path.Pages.GAME);
    };

    public static Handler setBoat = ctx -> {
        //TODO
    };

    public static Handler move = ctx -> {
        //TODO 
    };

    public static Handler removePlayer = ctx -> {
        Players.removeWithGame(ctx);
        ctx.header("Content-ID", "0");
        ctx.render(Path.Pages.START);
    };
}