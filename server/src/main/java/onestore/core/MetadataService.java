package onestore.core;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import onestore.core.model.UserMetadata;
import onestore.db.MetadataDB;

@Singleton
public class MetadataService {
    private MetadataDB metadataDB;

    public MetadataService(MetadataDB metadataDB) {
        this.metadataDB = metadataDB;
    }

    public Optional<List<UserMetadata>> getAllMetadata(String owner) {
        try {
            return Optional.ofNullable(this.metadataDB.getAllMetadata().stream()
                .filter(metadata -> metadata.getOwner() != null)
                .filter(metadata -> metadata.getOwner().equals(owner))
                .collect(Collectors.toList())
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<UserMetadata> getMetadata(String owner, String context) {
        return this.getAllMetadata(owner)
            .flatMap(allData -> allData.stream()
                .filter(data -> data != null)
                .filter(data -> data.getOwner() != null)
                .filter(data -> data.getOwner().equals(owner))
                .filter(data -> data.getDataContext() != null)
                .filter(data -> data.getDataContext().equals(context))
                .findAny()
            );
    }

    public Optional<UserMetadata> deleteData(String context, String owner) {
        var metadataToDelete = this.getMetadata(context, owner);
        try {
            if(metadataToDelete.isPresent()) {
                var metadata = metadataToDelete.get();
                var success = this.metadataDB.getAllMetadata().remove(metadata);
                if(success) {
                    return Optional.ofNullable(metadata);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<UserMetadata> createData(UserMetadata metadata) {
        try {
            this.metadataDB.insertMetadata(metadata);
            return Optional.ofNullable(metadata);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}