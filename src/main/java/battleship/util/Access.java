package battleship.util;

import java.util.Set;

import io.javalin.security.AccessManager;
import io.javalin.security.Role;

public class Access {
    public static final Set<Role> ANYONE = Set.of(AccessRole.ANYONE);
    public static final Set<Role> REGISTERED = Set.of(AccessRole.REGISTERED);

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