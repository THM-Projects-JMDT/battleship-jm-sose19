package battleship.pages;

import battleship.util.Path;
import io.javalin.Handler;

public class PageController {
    public static Handler getPage = ctx -> {
        //TODO vlt. nicht mit Name sonder mit game status? 
        if(ctx.sessionAttribute("Name") == null) {
            ctx.header("Content-ID", "3");
            ctx.render(Path.Pages.LOGIN);
            return;
        }
        ctx.header("Content-ID", "0");
        ctx.render(Path.Pages.GAME);
    };
}