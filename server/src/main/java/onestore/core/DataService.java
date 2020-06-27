package onestore.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Singleton;

import onestore.core.model.UserData;
import onestore.core.model.UserRight;
import onestore.db.DataDB;

@Singleton
public class DataService {
    private DataDB dataDB;
    private MetadataService metadataService;

    public DataService(MetadataService metadataService, DataDB dataDB) {
        this.dataDB = dataDB;
        this.metadataService = metadataService;
    }

    private boolean hasAccess(String currentUser, String owner, String context, UserRight right) {
        var maybeMetadata = this.metadataService.getMetadata(owner, context);
        if(!maybeMetadata.isPresent()) {
            return false;
        }
        var metadata = maybeMetadata.get();
        if(currentUser.equals(metadata.getOwner())) {
            return true;
        }
        if(currentUser.equals("ANONYMOUS")) {
            return metadata.getAnonymousUserRights().contains(right);
        }
        return metadata.getConnectedUserRights().contains(right);
    }

    public Optional<List<UserData>> getAllData(String currentUser, String owner, String context) {
        try {
            if(this.hasAccess(currentUser, owner, context, UserRight.READ)) {
                return Optional.ofNullable(this.dataDB.getAll(owner, context));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<UserData> getData(String currentUser, String owner, String context, String id) {
        return this.getAllData(currentUser, owner, context)
            .flatMap(allData -> allData.stream()
                .filter(data -> data != null)
                .filter(data -> data.getId() != null)
                .filter(data -> data.getId().equals(id))
                .findAny()
            );
    }

    public Optional<UserData> deleteData(String currentUser, String owner, String context, String id) {
        try {
            if(this.hasAccess(currentUser, owner, context, UserRight.DELETE)) {
                var userDataToDelete = this.getData(currentUser, owner, context, id);
                if(userDataToDelete.isPresent()) {
                    var data = userDataToDelete.get();
                    var success = this.dataDB.getAll(owner, context).remove(data);
                    if(success) {
                        return Optional.ofNullable(data);
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<UserData> createData(String currentUser, String owner, String context, Map<String, Object> data) {
        try {
            if(this.hasAccess(currentUser, owner, context, UserRight.CREATE)) {
                var userData = new UserData().content(data).id(UUID.randomUUID().toString());
                this.dataDB.insert(owner, context, userData);
                return Optional.ofNullable(userData);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}