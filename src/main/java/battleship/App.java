package battleship;

import battleship.game.GameController;
import battleship.util.Access;
import battleship.util.Path;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create()
        .enableStaticFiles("/public")
        .accessManager(Access.manager)
        .start(7000);

        app.routes(() -> {
            get(Path.NEWGAME, GameController.createGame, Access.anyone);
        });
    }
}
