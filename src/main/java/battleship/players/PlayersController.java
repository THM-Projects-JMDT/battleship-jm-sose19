package battleship.players;

import java.util.NoSuchElementException;

import battleship.game.Player;
import battleship.util.Path;
import battleship.util.Sse;
import io.javalin.BadRequestResponse;
import io.javalin.Context;
import io.javalin.Handler;

public class PlayersController {
    public static Handler startGame = ctx -> {
        ctx.sessionAttribute("Name", ctx.queryParam("Name"));
        Player p = Players.getPlayer(ctx);
        //Join game with Game-ID
        if (!Players.hasGame(p)) {
            try {
                if (!p.newGame(Players.getGameByID(ctx))) {
                    //if Player has Already a game
                    throw new BadRequestResponse("Player has Already a Game!");
                }
            } catch (NoSuchElementException ex) {
                ctx.header("Content-ID", "2");
                throw new BadRequestResponse("Invalide Game-ID");
            }

            //Send Message to other player
            Sse.playerConect(p.getGame().otherPlayer(p), ctx.sessionAttribute("Name"));
        }

        ctx.header("Content-ID", "4");
        ctx.render(Path.Pages.GAME);
    };

    public static Handler move = ctx -> {
        int cordinate = ctx.queryParam("Cordinate", Integer.class).get();
        Player p = Players.getPlayer(ctx);
        Player po = p.getGame().otherPlayer(p);

        //setboats
        if (p.getGame().getState() < 4){
            setBoat(ctx, cordinate);
        // move at Position
        } else if(p.getGame().move(p, cordinate)) {
            Sse.boardChanged(p, po);

            if(p.getGame().isFinished()) {
                Player winner = p.getGame().winner();
                Sse.finish(winner, winner.getGame().otherPlayer(winner));
            } 

            ctx.header("Content-ID", "6");
            ctx.result(po.getfield(true, false));
        } else {
            ctx.header("Content-ID", "10");
            throw new BadRequestResponse("Du bist nicht dran!");
        }

    };

    private static void setBoat(Context ctx, int cordinate) {
        ctx.header("Content-ID", "6");
        Player p = Players.getPlayer(ctx);

        if (p.setships(cordinate)) {
            ctx.result(p.getfield(true, true));
            Sse.updateMyships(p);
            Player pO = p.getGame().otherPlayer(p);
            if(pO != null)
                Sse.updateEnemyships(pO, p);
            if(p.getshipslength() == 0) {
                p.getGame().Stateadd();
                if(p.getGame().getState() == 4) {
                    ctx.result(pO.getfield(true, false));
                    Sse.changeBoards(p, pO);
                    p.changeMyTurn();
                } else 
                    Sse.wait(p);
            }

            return;
        }

        throw new BadRequestResponse("Invalide Placement!");
    }

    public static Handler removePlayer = ctx -> {
        Players.removeWithGame(ctx);
        ctx.header("Content-ID", "0");
        ctx.render(Path.Pages.START);
    };
}