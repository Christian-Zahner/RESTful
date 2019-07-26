package de.thro.inf.restful.client.jsonCreaters;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.thro.inf.restful.models.XChange;

public class XchangeToJson {

    public static JsonObject xChangeToJson(XChange change) {
        JsonObject clothes1JsonObject = new JsonObject();
        JsonObject clothes2JsonObject = new JsonObject();
        clothes1JsonObject.addProperty("id", change.getXChangeClothes().get(0).getId());
        clothes2JsonObject.addProperty("id", change.getXChangeClothes().get(1).getId());

        // to array
        JsonArray exChangePartnersJsonArray = new JsonArray();
        exChangePartnersJsonArray.add(clothes1JsonObject);
        exChangePartnersJsonArray.add(clothes2JsonObject);

        // finalize
        JsonObject xChangeJsonObject = new JsonObject();
        xChangeJsonObject.addProperty("exchangeDate", change.getExchangeDate().toString());
        xChangeJsonObject.addProperty("id", change.getId());
        xChangeJsonObject.add("xchangeClothes", exChangePartnersJsonArray);

        return xChangeJsonObject;
    }
}