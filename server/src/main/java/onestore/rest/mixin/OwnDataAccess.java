package onestore.rest.mixin;

import java.security.Principal;

import io.micronaut.http.annotation.PathVariable;

public interface OwnDataAccess {
    default boolean checkAccesToOwnData(Principal principal, @PathVariable String requestUser) {
        var currentUser = getCurrentUser(principal);
        return requestUser != null && currentUser != null && currentUser.equals(requestUser);
    }

    default String getCurrentUser(Principal principal) {
        if (principal == null) {
            return "ANONYMOUS";
        }
        return principal.getName();
    }
}