package battleship;

import battleship.util.Access;
import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin app = Javalin.create()
        .enableStaticFiles("/public")
        .accessManager(Access.manager)
        .start(7000);
    }
}
