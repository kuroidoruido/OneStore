package onestore.rest;

import java.security.Principal;
import java.util.Optional;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import onestore.auth.AuthRoles;
import onestore.core.UserInfosService;
import onestore.rest.mixin.OwnDataAccess;
import onestore.rest.model.UserInfo;

@Controller()
@Secured(SecurityRule.IS_AUTHENTICATED)
public class RestUserController implements OwnDataAccess {

    private UserInfosService userInfosService;

    public RestUserController(UserInfosService userInfosService) {
        this.userInfosService = userInfosService;
    }

    @Get("/users/{user}")
    public Optional<UserInfo> getUserInfo(@Nullable Principal principal, @PathVariable String user) {
        if (checkAccesToOwnData(principal, user)) {
            return userInfosService.getUserInfoForOwner(user).map(UserInfo::fromCoreModel);
        } else {
            return userInfosService.getUserInfoOnOtherUser(user).map(UserInfo::fromCoreModel);
        }
    }

    @Post("/users/register")
    @Secured(AuthRoles.ROLE_ADMIN)
    public void createUser(@Nullable Principal principal, @Body UserInfo user) {
        userInfosService.createUser(UserInfo.toCoreModel(user));
    }
}