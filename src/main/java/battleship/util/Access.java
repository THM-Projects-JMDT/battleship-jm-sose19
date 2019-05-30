package battleship.util;

import java.util.Set;

import battleship.players.Players;
import io.javalin.security.AccessManager;
import io.javalin.security.Role;

public class Access {
    public static final Set<Role> ANYONE = Set.of(AccessRole.ANYONE);
    public static final Set<Role> REGISTERED = Set.of(AccessRole.REGISTERED);
    public static final Set<Role> INGAME = Set.of(AccessRole.INGAME);

    private enum AccessRole implements Role {
        ANYONE, REGISTERED, INGAME;
    }

    public static AccessManager manager = (handler, ctx, permittedRoles) -> {
        if (permittedRoles.contains(AccessRole.ANYONE))
            handler.handle(ctx);
        else if(Players.isPlayer(ctx))
            if(permittedRoles.contains(AccessRole.REGISTERED))
                handler.handle(ctx);
            else if(permittedRoles.contains(AccessRole.INGAME) && Players.hasGame(ctx))
                handler.handle(ctx);
        else
            ctx.status(401).result("Unauthorized");
    };
}