package battleship.pages;

import battleship.util.Path;
import io.javalin.Handler;

public class PageController {
    public static Handler getPage = ctx -> {
        ctx.header("Content-ID", "0");
        //TODO vlt. nicht mit Name sonder mit game status? 
        if(ctx.sessionAttribute("Name") == null) {
            ctx.render(Path.Pages.LOGIN);
            return;
        }
        ctx.render(Path.Pages.GAME);
    };
}