package de.thro.inf.restful.client.jsonCreaters;

import de.thro.inf.restful.models.Address;
import de.thro.inf.restful.models.Member;
import top.jfunc.json.impl.JSONObject;

public class MemberToJson {

    public static JSONObject addressToJson(Address address) {
        JSONObject addressJsonObject = new JSONObject();

        addressJsonObject.put("city", address.getCity());
        addressJsonObject.put("postcode", address.getPostcode());
        addressJsonObject.put("street", address.getStreet());
        return addressJsonObject;
    }

    public static JSONObject memberToJson(Member member) {
        JSONObject memberJsonObject = new JSONObject();
        JSONObject addressJsonObject = addressToJson(member.getAddress());

        memberJsonObject.put("address", addressJsonObject);

        memberJsonObject.put("credit", member.getCredit());
        memberJsonObject.put("firstName", member.getFirstName());
        memberJsonObject.put("lastName", member.getLastName());
        memberJsonObject.put("mail", member.getMail());
        memberJsonObject.put("nickName", member.getNickName());
        memberJsonObject.put("password", member.getPassword());
        memberJsonObject.put("version", member.getVersion());

        return memberJsonObject;
    }
}