package battleship;

import battleship.game.GameController;
import battleship.pages.PageController;
import battleship.players.PlayersController;
import battleship.util.*;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create()
        .enableStaticFiles("/public")
        .accessManager(Access.manager)
        .start(7000);

        app.routes(() -> {
            get(Path.Web.GETPAGE, PageController.getPage, Access.REGISTERED);
            get(Path.Web.NEWGAME, GameController.newGame, Access.ANYONE);
            get(Path.Web.JOINGAME, GameController.joinGame, Access.ANYONE);
            get(Path.Web.ABOUTGAME, GameController.aboutGame, Access.ANYONE);
            get(Path.Web.GETGAMEID, GameController.getGameid, Access.REGISTERED);
            get(Path.Web.STARTGAME, PlayersController.startGame, Access.REGISTERED);
            get(Path.Web.MOVE, PlayersController.move, Access.REGISTERED);
            get(Path.Web.REMOVEPLAYER, PlayersController.removePlayer, Access.REGISTERED);
        });

        app.sse(Path.Web.SSE, Sse.init, Access.REGISTERED);
    }
} 