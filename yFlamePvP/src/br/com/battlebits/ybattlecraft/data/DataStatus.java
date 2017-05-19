package br.com.battlebits.ybattlecraft.data;

import java.util.UUID;

import org.bson.Document;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import br.com.battlebits.commons.BattlebitsAPI;
import br.com.battlebits.commons.core.data.Data;
import br.com.battlebits.ybattlecraft.Battlecraft;
import br.com.battlebits.ybattlecraft.constructors.Status;

public class DataStatus extends Data {

	public static Status getStatus(UUID uuid) {
		MongoDatabase database = Battlecraft.getInstance().getMongoBackend().getClient().getDatabase("battlecraft");
		MongoCollection<Document> collection = database.getCollection("status");

		Document found = collection.find(Filters.eq("uniqueId", uuid.toString())).first();
		if (found == null) {
			return null;
		}
		return BattlebitsAPI.getGson().fromJson(BattlebitsAPI.getGson().toJson(found), Status.class);
	}

	public static Status createIfNotExistMongo(UUID uuid) {
		MongoDatabase database = Battlecraft.getInstance().getMongoBackend().getClient().getDatabase("battlecraft");
		MongoCollection<Document> collection = database.getCollection("status");

		Document found = collection.find(Filters.eq("uniqueId", uuid.toString())).first();
		Status status = null;
		if (found == null) {
			status = new Status(uuid);
			found = Document.parse(BattlebitsAPI.getGson().toJson(status));
			collection.insertOne(found);
		} else {
			status = BattlebitsAPI.getGson().fromJson(BattlebitsAPI.getGson().toJson(found), Status.class);
		}
		return status;
	}

	public static void saveStatusField(Status status, String fieldName) {
		JsonObject jsonObject = BattlebitsAPI.getParser().parse(BattlebitsAPI.getGson().toJson(status))
				.getAsJsonObject();
		if (!jsonObject.has(fieldName))
			return;
		JsonElement element = jsonObject.get(fieldName);
		Object value = null;
		if (!element.isJsonPrimitive()) {
			value = Document.parse(element.toString());
		} else {
			if (element.getAsJsonPrimitive().isBoolean()) {
				value = element.getAsBoolean();
			} else if (element.getAsJsonPrimitive().isNumber()) {
				try {
					value = Long.parseLong(element.getAsString());
				} catch (Exception e2) {
					try {
						value = Byte.parseByte(element.getAsString());
					} catch (Exception e3) {
						try {
							value = Short.parseShort(element.getAsString());
						} catch (Exception e4) {
							try {
								value = Integer.parseInt(element.getAsString());
							} catch (Exception e5) {
								try {
									value = Double.parseDouble(element.getAsString());
								} catch (Exception e) {
									try {
										value = Float.parseFloat(element.getAsString());
									} catch (Exception e1) {
									}
								}
							}
						}
					}
				}
			} else {
				value = element.getAsString();
			}
		}
		MongoDatabase database = Battlecraft.getInstance().getMongoBackend().getClient().getDatabase("battlecraft");
		MongoCollection<Document> collection = database.getCollection("status");
		collection.updateOne(Filters.eq("uniqueId", status.getUniqueId().toString()),
				new Document("$set", new Document(fieldName, value)));
	}
}
