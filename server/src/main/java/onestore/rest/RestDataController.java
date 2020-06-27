package onestore.rest;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import onestore.auth.AuthRoles;
import onestore.core.DataService;
import onestore.rest.mixin.OwnDataAccess;
import onestore.rest.model.UserData;

@Controller()
@Secured(SecurityRule.IS_AUTHENTICATED)
public class RestDataController implements OwnDataAccess {

    private DataService dataService;

    public RestDataController(DataService dataService) {
        this.dataService = dataService;
    }

    @Get("/data/{owner}/{context}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public Optional<List<UserData>> getUserDataForOneContext(@Nullable Principal principal, @PathVariable String owner,
            @PathVariable String context) {
        return this.dataService.getAllData(getCurrentUser(principal), owner, context)
                .map(list -> list.stream().map(UserData::fromCoreModel).collect(Collectors.toList()));
    }

    @Get("/data/{owner}/{context}/{id}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    public Optional<UserData> getOneUserData(@Nullable Principal principal, @PathVariable String owner,
            @PathVariable String context, @PathVariable String id) {
        return this.dataService.getData(getCurrentUser(principal), owner, context, id).map(UserData::fromCoreModel);
    }

    @Post("/data/{owner}/{context}")
    @RolesAllowed(AuthRoles.ROLE_USER)
    public Optional<UserData> insertDataInOneContext(@Nullable Principal principal, @PathVariable String owner,
            @PathVariable String context, @Body Map<String, Object> obj) {
        return this.dataService.createData(getCurrentUser(principal), owner, context, obj).map(UserData::fromCoreModel);
    }

    @Delete("/data/{owner}/{context}/{id}")
    @RolesAllowed(AuthRoles.ROLE_USER)
    public Optional<UserData> deleteOneDataInOneContext(@Nullable Principal principal, @PathVariable String owner,
            @PathVariable String context, @PathVariable String id) {
        return this.dataService.deleteData(getCurrentUser(principal), owner, context, id).map(UserData::fromCoreModel);
    }
}