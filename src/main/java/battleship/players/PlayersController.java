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
        Player p = Players.getPlayer(ctx);
        if (p.getGame().getState()<4){
            setBoat(ctx);
        }
        if(p.getGame().getState()>=4 && p.getGame().getState()<6){
            lookatfield(ctx);
        }


        //TODO check if gamestatus ready
    };

    private static void lookatfield(Context ctx) {
        Player p = Players.getPlayer(ctx);
        Player p0 = p.getGame().otherPlayer(p);
        if (p0.canilookatthisfield(ctx.queryParam("Cordinate", Integer.class).get())) {
            p0.changeMyTurn();
            p.changeMyTurn();
            p.getClient().sendEvent("UpdateEnemyships", "UpdateEnemyships");
            p0.getClient().sendEvent("UpdateMyships", "UpdateMyships");
            p0.getClient().sendEvent("UpdateEnemyboard", "UpdateEnemyboard");
            ctx.header("Content-ID", "6");
            ctx.result(p0.getfield(true, false));
            if(p.checkifend() || p0.checkifend()) {
                p.getClient().sendEvent("Finish","Finish");
                p0.getClient().sendEvent("Finish","Finish");
                p.beendegame();
                p0.beendegame();
            }
            return;
        }
        ctx.header("Content-ID", "10");
        throw new BadRequestResponse("Du bist nicht dran!");
    }

    private static void setBoat(Context ctx) {
        ctx.header("Content-ID", "6");
        Player p = Players.getPlayer(ctx);

        if (p.setships(ctx.queryParam("Cordinate", Integer.class).get())) {
            ctx.result(p.getfield(true, true));
            p.getClient().sendEvent("UpdateMyships","UpdateMyships");
            Player pO = p.getGame().otherPlayer(p);
            if(pO != null && pO.getClient() != null)
                pO.getClient().sendEvent("UpdateEnemyships", "UpdateEnemyships");
            if(p.getshipslength()==0) {
                p.getGame().Stateadd();
                p.getClient().sendEvent("UpdateEnemyships", "UpdateEnemyships");
                if(p.getGame().getState()==4){
                    p.getClient().sendEvent("Updateboard","Updateboard");
                    pO.getClient().sendEvent("Updateboard", "Updateboard");
                    p.changeMyTurn();
                }
            }

            return;
        }

        throw new BadRequestResponse("Invalide Placement!");
    }

    public static Handler myShips = ctx -> {
        ctx.header("Content-ID", "7");
        Player p = Players.getPlayer(ctx);
        ctx.result(p.getshipstatus());
    };
    public static Handler myField = ctx -> {
        ctx.header("Content-ID", "6");
        Player p = Players.getPlayer(ctx);
        ctx.result(p.getGame().otherPlayer(p).getfield(true, false));
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
        ctx.result(p.getfield(false, true));
    };

    public static Handler removePlayer = ctx -> {
        Players.removeWithGame(ctx);
        ctx.header("Content-ID", "0");
        ctx.render(Path.Pages.START);
    };
}