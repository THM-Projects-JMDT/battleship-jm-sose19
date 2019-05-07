package battleship.util;

import java.util.Set;

import io.javalin.security.AccessManager;
import io.javalin.security.Role;

public class Access {
    public static Set<Role> anyone = Set.of(AccessRole.ANYONE);
    public static Set<Role> registered = Set.of(AccessRole.REGISTERED);

    private enum AccessRole implements Role {
        ANYONE, REGISTERED;
    }

    public static AccessManager manager = (handler, ctx, permittedRoles) -> {
        if (permittedRoles.contains(AccessRole.ANYONE) /* || TODO Check if Registered Player*/)
            handler.handle(ctx);
        else
            ctx.status(401).result("Unauthorized");
    };
}