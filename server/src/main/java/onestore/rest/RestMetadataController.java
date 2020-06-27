package onestore.rest;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import onestore.core.MetadataService;
import onestore.rest.mixin.OwnDataAccess;
import onestore.rest.model.UserMetadata;

@Controller()
@Secured(SecurityRule.IS_AUTHENTICATED)
public class RestMetadataController implements OwnDataAccess {

    private MetadataService metadataService;

    public RestMetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @Get("/metadata/{owner}")
    public Optional<List<UserMetadata>> getAllUserMetadata(@Nullable Principal principal, @PathVariable String owner) {
        if (checkAccesToOwnData(principal, owner)) {
            return this.metadataService.getAllMetadata(owner)
                    .map(list -> list.stream().map(UserMetadata::fromCoreModel).collect(Collectors.toList()));
        } else {
            return Optional.empty();
        }
    }

    @Get("/metadata/{owner}/{context}")
    public Optional<UserMetadata> getOneUserMetadata(@Nullable Principal principal, @PathVariable String owner,
            @PathVariable String context) {
        if (checkAccesToOwnData(principal, owner)) {
            return this.metadataService.getMetadata(owner, context).map(UserMetadata::fromCoreModel);
        } else {
            return Optional.empty();
        }
    }

    @Post("/metadata")
    @Secured({ AuthRoles.ROLE_USER })
    public Optional<UserMetadata> insertDataInOneContext(@Nullable Principal principal, @Body UserMetadata metadata) {
        if (checkAccesToOwnData(principal, metadata.getOwner())) {
            return this.metadataService.createData(UserMetadata.toCoreModel(metadata)).map(UserMetadata::fromCoreModel);
        } else {
            return Optional.empty();
        }
    }

    @Delete("/metadata/{owner}/{context}")
    public Optional<UserMetadata> deleteOneMetadata(@Nullable Principal principal, @PathVariable String owner,
            @PathVariable String context) {
        if (checkAccesToOwnData(principal, owner)) {
            var serviceOut = this.metadataService.deleteData(context, owner);
            return serviceOut.map(UserMetadata::fromCoreModel);
        } else {
            return Optional.empty();
        }
    }
}