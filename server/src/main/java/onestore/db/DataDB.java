package onestore.db;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.inject.Singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nf.fr.k49.shielddb.core.ShieldDB;
import nf.fr.k49.shielddb.core.json.ShieldDBJsonMapper;
import nf.fr.k49.shielddb.core.shield.InMemoryArrayListReadCacheShield;
import nf.fr.k49.shielddb.core.storage.FileStorage;
import nf.fr.k49.shielddb.gson.ShieldDBGson;
import nf.fr.k49.shielddb.gson.GsonTypeUtils;
import onestore.config.AppConfig;
import onestore.core.model.UserData;

@Singleton
public class DataDB {
    private AppConfig appConfig;
    private Map<String, List<UserData>> databases;
    private ShieldDBJsonMapper<UserData> mapper;

    public DataDB(AppConfig appConfig) throws IOException {
        this.appConfig = appConfig;
        this.databases = new WeakHashMap<>();
        Type type = GsonTypeUtils.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(type, new UserDataDeserializer())
                .create();
        this.mapper = new ShieldDBGson<UserData>(gson, type);
    }

    private List<UserData> getDb(String owner, String context) throws IOException {
        var db = this.databases.get(context);
        if (db == null) {
            var dbFilePath = this.appConfig.dbPath + File.separator + owner + File.separator + context + ".json";
            System.out.println("Creating ShieldDB instance for " + owner + "/" + context + " with path " + dbFilePath);
            var newDb = ShieldDB.<UserData>builder().mapper(this.mapper)
                    .shield(new InMemoryArrayListReadCacheShield<>()).storage(new FileStorage(dbFilePath)).build();
            this.databases.put(context, newDb);
            return newDb;
        } else {
            return db;
        }
    }

    public List<UserData> getAll(String owner, String context) throws IOException {
        return this.getDb(owner, context);
    }

    public void insert(String owner, String context, UserData data) throws IOException {
        this.getDb(owner, context).add(data);
    }
}