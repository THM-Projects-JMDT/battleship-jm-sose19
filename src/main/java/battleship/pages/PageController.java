package battleship.pages;

import battleship.util.Path;
import io.javalin.Handler;

public class PageController {
    public static Handler getPage = ctx -> {
        ctx.header("Content-ID", "1");
        
        //TODO accesmanger für validierung und wenn anonym dann start
        if(ctx.sessionAttribute("name") != null)
            ctx.render(Path.Pages.LOGIN);
        //TODO Noch mit game
        ctx.render(Path.Pages.START);
    };
}