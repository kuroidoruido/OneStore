package onestore.db;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import nf.fr.k49.shielddb.gson.JsonListDeserializer;

import onestore.core.model.UserInfo;

public class UserInfoDeserializer implements JsonListDeserializer<UserInfo> {

	@Override
	public UserInfo deserializeOne(JsonElement json, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonO = json.getAsJsonObject();
		final UserInfo res = new UserInfo();

		if (jsonO.get("username") != null) {
			res.setUsername(jsonO.get("username").getAsString());
		}
		if (jsonO.get("firstname") != null) {
			res.setFirstname(jsonO.get("firstname").getAsString());
		}
		if (jsonO.get("lastname") != null) {
			res.setLastname(jsonO.get("lastname").getAsString());
		}
		if (jsonO.get("password") != null) {
			res.setPassword(jsonO.get("password").getAsString());
		}
		if (jsonO.get("birthday") != null) {
			res.setBirthday(jsonO.get("birthday").getAsString());
		}
		return res;
	}

}
