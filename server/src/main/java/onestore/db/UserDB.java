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
import onestore.core.model.UserInfo;

@Singleton
public class UserDB {
    
    public static final String USER_COLLECTION = "users";
    
    private List<UserInfo> db;

    public UserDB(AppConfig appConfig) throws IOException {
        Type type = GsonTypeUtils.getType();
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(type, new UserInfoDeserializer())
				.create();
        this.db = ShieldDB.<UserInfo>builder()
            .mapper(new ShieldDBGson<UserInfo>(gson, type))
            .shield(new InMemoryArrayListReadCacheShield<>())
            .storage(new FileStorage(appConfig.dbPath + File.separator + USER_COLLECTION+".json"))
            .build();
    }
    
    public List<UserInfo> getUsers() {
        return this.db;
    }

    public void createUser(UserInfo userInfo) {
        this.db.add(userInfo);
    }
}