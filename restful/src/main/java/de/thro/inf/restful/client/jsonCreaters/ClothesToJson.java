package de.thro.inf.restful.client.jsonCreaters;

import de.thro.inf.restful.models.Clothes;
import de.thro.inf.restful.models.Member;
import top.jfunc.json.impl.JSONObject;

public class ClothesToJson {

    public static JSONObject clothesToJson(Clothes clothes, Member member) {
        JSONObject clothesJsonObject = new JSONObject();
        clothes.setOwnerCl(member);
        JSONObject ownerJsonObject = ownerToJson(clothes.getOwnerCl());

        clothesJsonObject.put("brand", clothes.getBrand());
        clothesJsonObject.put("exchangeValue", clothes.getExchangeValue());
        clothesJsonObject.put("id", clothes.getId());
        clothesJsonObject.put("name", clothes.getName());
        clothesJsonObject.put("originalPriceInEuroCent", clothes.getOriginalPriceInEuroCent());

        clothesJsonObject.put("ownerCl", ownerJsonObject);

        clothesJsonObject.put("sex", clothes.getSex().toString());
        clothesJsonObject.put("size", clothes.getSize()).toString();
        clothesJsonObject.put("type", clothes.getType().toString());
        clothesJsonObject.put("version", clothes.getVersion());

        return clothesJsonObject;
    }

    public static JSONObject ownerToJson(Member owner) {
        JSONObject ownerJsonObject = new JSONObject();

        ownerJsonObject.put("nickName", owner.getNickName());

        return ownerJsonObject;
    }
}
