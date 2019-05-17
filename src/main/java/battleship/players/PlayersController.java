package battleship.players;

import java.util.NoSuchElementException;

import battleship.game.Player;
import battleship.util.Path;
import battleship.util.Sse;
import io.javalin.BadRequestResponse;
import io.javalin.Context;
import io.javalin.Handler;
import io.javalin.serversentevent.SseClient;

public class PlayersController {
    public static Handler startGame = ctx -> {
        ctx.sessionAttribute("Name", ctx.queryParam("Name"));
        //Join game with Game-ID
        if (!Players.hasGame(ctx)) {
            try {
                if (!Players.getPlayer(ctx).newGame(Players.getGame(ctx))) {
                    //TODO Hadle if Player has Already a game (vtl nich nötig wenn überprüft von Acces manager)
                    return;
                }
            } catch (NoSuchElementException ex) {
                ctx.header("Content-ID", "2");
                throw new BadRequestResponse("Invalide Game-ID");
            }

            //Send Message to other player
            SseClient client = Players.playWith(ctx).getClient();

            if (client != null)
                Sse.playerConect(Players.playWith(ctx).getClient(), ctx.sessionAttribute("Name"));
        }

        ctx.header("Content-ID", "4");
        ctx.render(Path.Pages.GAME);
    };

    public static Handler move = ctx -> {
        //TODO check if gamestatus ready 
        setBoat(ctx);
    };

    private static void setBoat(Context ctx) {
        ctx.header("Content-ID", "6");
        Player p = Players.getPlayer(ctx);

        if (p.setships(ctx.queryParam("Cordinate", Integer.class).get())) {
            ctx.result(p.getfield(false));
            p.getClient().sendEvent("UpdateMyships","UpdateMyships");
            Player pO = p.getGame().otherPlayer(p);
            if(pO != null && pO.getClient() != null)
                pO.getClient().sendEvent("UpdateEnemyboard", "UpdateEnemyboard");
            return;
        }

        throw new BadRequestResponse("Invalide Placement!");
    }

    public static Handler myShips = ctx -> {
        ctx.header("Content-ID", "7");
        Player p = Players.getPlayer(ctx);
        ctx.result(p.getshipstatus());
    };

    public static Handler enemyShips = ctx -> {
        ctx.header("Content-ID", "8");
        Player p = Players.getPlayer(ctx);
        Player pO = p.getGame().otherPlayer(p);

        if(pO == null)
            throw new BadRequestResponse("Enemy has no Board!");
        ctx.result(pO.getshipstatus());
    };

    public static Handler enemyField = ctx -> {
        ctx.header("Content-ID", "9");
        Player p = Players.getPlayer(ctx);
        ctx.result(p.getshipstatus());
    };

    public static Handler removePlayer = ctx -> {
        Players.removeWithGame(ctx);
        ctx.header("Content-ID", "0");
        ctx.render(Path.Pages.START);
    };
}