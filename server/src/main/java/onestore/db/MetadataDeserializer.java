package onestore.db;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import nf.fr.k49.shielddb.gson.JsonListDeserializer;

import onestore.core.model.UserMetadata;
import onestore.core.model.UserRight;

public class MetadataDeserializer implements JsonListDeserializer<UserMetadata> {

	@Override
	public UserMetadata deserializeOne(JsonElement json, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonO = json.getAsJsonObject();
		final UserMetadata res = new UserMetadata();
		if (jsonO.get("dataContext") != null) {
			res.setDataContext(jsonO.get("dataContext").getAsString());
		}
		if (jsonO.get("owner") != null) {
			res.setOwner(jsonO.get("owner").getAsString());
		}
		if (jsonO.get("connectedUserRights") != null) {
			res.setConnectedUserRights(getUserRights(jsonO.get("connectedUserRights")));
		}
		if (jsonO.get("anonymousUserRights") != null) {
			res.setAnonymousUserRights(getUserRights(jsonO.get("anonymousUserRights")));
		}
		return res;
	}

	public List<UserRight> getUserRights(JsonElement json) {
		var rights = new ArrayList<UserRight>();
		if(json.isJsonArray()) {
			for(var right : json.getAsJsonArray()) {
				rights.add(UserRight.valueOf(right.getAsString()));
			}
		}
		return rights;
	}
}
