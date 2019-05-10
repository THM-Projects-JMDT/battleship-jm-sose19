package battleship.pages;

import battleship.util.Path;
import io.javalin.Handler;

public class PageController {
    public static Handler getPage = ctx -> {
        ctx.header("Content-ID", "1");
        //TODO if not have a Game
        if(ctx.sessionAttribute("name") == null)
            ctx.render(Path.Pages.LOGIN);
        ctx.render(Path.Pages.GAME);
    };
}