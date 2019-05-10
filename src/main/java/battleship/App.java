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
            get(Path.Web.SETNAME, PlayersController.setName, Access.REGISTERED);
        });

        app.sse(Path.Web.SSE, Sse.init, Access.REGISTERED);
    }
}