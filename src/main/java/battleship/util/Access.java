package battleship.util;

import java.util.HashSet;
import java.util.Set;

import io.javalin.security.AccessManager;
import io.javalin.security.Role;

public class Access {
    public static Set<Role> anyone = roleSet(AccessRole.ANYONE);
    public static Set<Role> registered = roleSet(AccessRole.REGISTERED);

    private enum AccessRole implements Role {
        ANYONE, REGISTERED;
    }

    private static Set<Role> roleSet(Role role) {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        return roleSet;
    }

    public static AccessManager manager = (handler, ctx, permittedRoles) -> {
        if (permittedRoles.contains(AccessRole.ANYONE) /* || TODO Check if Registered Player*/)
            handler.handle(ctx);
        else
            ctx.status(401).result("Unauthorized");
    };
}