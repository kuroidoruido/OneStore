package onestore.db;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import nf.fr.k49.shielddb.gson.JsonListDeserializer;

import onestore.core.model.UserData;

public class UserDataDeserializer implements JsonListDeserializer<UserData> {

	private Gson gson;

	public UserDataDeserializer() {
		this.gson = new Gson();
	}

	@Override
	public UserData deserializeOne(JsonElement json, JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonO = json.getAsJsonObject();
		final UserData res = new UserData();

		if (jsonO.get("id") != null) {
			res.setId(jsonO.get("id").getAsString());
		}
		if (jsonO.get("content") != null) {
			var strContent = jsonO.get("content").toString();
			res.setContent(this.gson.fromJson(strContent, Object.class));
		}
		return res;
	}

}
