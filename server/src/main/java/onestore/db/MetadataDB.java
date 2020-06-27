package onestore.db;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nf.fr.k49.shielddb.core.ShieldDB;
import nf.fr.k49.shielddb.core.storage.FileStorage;
import nf.fr.k49.shielddb.core.shield.InMemoryArrayListReadCacheShield;
import nf.fr.k49.shielddb.gson.ShieldDBGson;
import nf.fr.k49.shielddb.gson.GsonTypeUtils;
import onestore.config.AppConfig;
import onestore.core.model.UserMetadata;

@Singleton
public class MetadataDB {
    
    public static final String METADATA_COLLECTION = "metadata";
    
    private List<UserMetadata> db;

    public MetadataDB(AppConfig appConfig) throws IOException {
        Type type = GsonTypeUtils.getType();
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(type, new MetadataDeserializer())
				.create();
        this.db = ShieldDB.<UserMetadata>builder()
            .mapper(new ShieldDBGson<UserMetadata>(gson, type))
            .shield(new InMemoryArrayListReadCacheShield<>())
            .storage(new FileStorage(appConfig.dbPath + File.separator + METADATA_COLLECTION+".json"))
            .build();
    }
    
    public List<UserMetadata> getAllMetadata() {
        return this.db;
    }

    public void insertMetadata(UserMetadata metadata) {
        this.db.add(metadata);
    }
}