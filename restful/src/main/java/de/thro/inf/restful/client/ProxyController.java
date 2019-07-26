package de.thro.inf.restful.client;

import com.google.gson.JsonObject;
import de.thro.inf.restful.models.Clothes;
import de.thro.inf.restful.models.Member;
import de.thro.inf.restful.models.XChange;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import top.jfunc.json.impl.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import static de.thro.inf.restful.client.jsonCreaters.ClothesToJson.clothesToJson;
import static de.thro.inf.restful.client.jsonCreaters.MemberToJson.memberToJson;
import static de.thro.inf.restful.client.jsonCreaters.XchangeToJson.xChangeToJson;

public class ProxyController {
    private static final String baseURI = "http://localhost:";
    private static final String version = "api/v1/";
    private int port = 8080;

    private HashMap<String, Member> memberMap = new HashMap<>();
    private HashMap<Long, Clothes> clothesMap = new HashMap<>();
    private HashMap<Long, XChange> xChangeMap = new HashMap<>();

    public ProxyController(int port) {
        this.port = port;
    }

    public Member findMemberByNickName(String nickName) {
        RestTemplate template = new RestTemplate();
        Member tmp = memberMap.get(nickName);
        if (tmp != null) {
            return tmp;
        }
        Member result = template.getForObject(
                baseURI + port + version + "/member/" + nickName,
                Member.class);
        return result;
    }

    public Member createMember(Member member) {

        JSONObject memberJsonObject = memberToJson(member);
        RestTemplate restTemplate = new RestTemplate();

        if (memberMap.containsKey(member.getNickName())) {
            return memberMap.get(member.getNickName());
        } else {
            memberMap.put(member.getNickName(), member);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestBody = new HttpEntity<>(memberJsonObject.toString(), httpHeaders);

            return restTemplate.postForObject(baseURI + port + version + "/member", requestBody,
                    Member.class);
        }
    }

    public void deleteMember(String nickName) {
        RestTemplate template = new RestTemplate();
        Member tmp = findMemberByNickName(nickName);

        if (tmp == null) {
            return;
        }
        memberMap.remove(nickName);
        template.delete(baseURI + port + version + "/member/" + nickName);
    }

    public Member updateMember(Member member) {
        JSONObject memberJsonObject = memberToJson(member);

        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Member tmp = findMemberByNickName(member.getNickName());

        if (tmp == null) {
            return tmp;
        }

        HttpEntity<String> requestBody = new HttpEntity<>(memberJsonObject.toString(), headers);

        ResponseEntity<Member> result = template.exchange(
                baseURI + port + version + "member/" + member.getNickName(),
                HttpMethod.PUT, requestBody, Member.class);
        memberMap.replace(member.getNickName(), member);

        if (result.getStatusCode().equals(HttpStatus.OK)) {
            return result.getBody(); // Everything is alright
        }
        System.err.println("Conflict!");
        return null;
    }

    public Clothes findClothesById(Long Id) {
        RestTemplate template = new RestTemplate();
        Clothes tmp = clothesMap.get(Id);
        if (tmp != null) {
            return tmp;
        }
        Clothes result = template.getForObject(
                baseURI + port + version + "/clothes/" + Id.toString(),
                Clothes.class);
        return result;
    }

    public Clothes createClothes(Clothes clothes, Member member) {
        clothes.setOwnerCl(member);
        JSONObject clothesJsonObject = clothesToJson(clothes, clothes.getOwnerCl());
        RestTemplate restTemplate = new RestTemplate();

        if (clothesMap.containsKey(clothes.getId())) {
            return clothesMap.get(clothes.getId());
        } else {

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestBody = new HttpEntity<>(clothesJsonObject.toString(), httpHeaders);

            clothesMap.put(clothes.getId(), clothes);

            return restTemplate.postForObject(baseURI + port + version + "/clothes", requestBody,
                    Clothes.class);
        }
    }

    public void deleteClothes(Long Id) {
        RestTemplate template = new RestTemplate();
        Clothes tmp = findClothesById(Id);

        if (tmp == null) {
            return;
        }
        clothesMap.remove(Id);
        template.delete(baseURI + port + version + "/clothes/" + Id.toString());
    }

    public Clothes updateClothes(Clothes clothes, Member member) {
        JSONObject clothesJsonObject = clothesToJson(clothes, findClothesById(clothes.getId()).getOwnerCl());

        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Clothes tmp = findClothesById(clothes.getId());

        if (tmp == null) {
            return tmp;
        }
        HttpEntity<String> requestBody = new HttpEntity<>(clothesJsonObject.toString(), headers);

        ResponseEntity<Clothes> result = template.exchange(
                baseURI + port + version + "clothes/" + clothes.getId(),
                HttpMethod.PUT, requestBody, Clothes.class);
        clothesMap.replace(clothes.getId(), clothes);

        if (result.getStatusCode().equals(HttpStatus.OK)) {
            return result.getBody(); // OK Fall
        }
        System.err.println("Conflict!");
        return null;
    }

    public XChange findXChangeById(Long Id) {
        RestTemplate template = new RestTemplate();
        XChange tmp = xChangeMap.get(Id);
        if (tmp != null) {
            return tmp;
        }
        XChange result = template.getForObject(
                baseURI + port + version + "/xchange/" + Id.toString(),
                XChange.class);
        return result;
    }

    public XChange createXChange(XChange xChange) {
        RestTemplate restTemplate = new RestTemplate();
        JsonObject xChangeJsonObject = xChangeToJson(xChange);

        if (xChangeMap.containsKey(xChange.getId())) {
            return xChangeMap.get(xChange.getId());
        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestBody = new HttpEntity<>(xChangeJsonObject.toString(), httpHeaders);

            xChangeMap.put(xChange.getId(), xChange);
            memberMap.remove(xChange.getXChangeClothes().get(0).getOwnerCl().getNickName());
            memberMap.remove(xChange.getXChangeClothes().get(1).getOwnerCl().getNickName());
            clothesMap.remove(xChange.getXChangeClothes().get(0).getId());
            clothesMap.remove(xChange.getXChangeClothes().get(1).getId());
            return restTemplate.postForObject(baseURI + port + version + "/xchange", requestBody,
                    XChange.class);
        }
    }

    public void deleteXChange(Long Id) {
        RestTemplate template = new RestTemplate();
        XChange tmp = findXChangeById(Id);
        if (tmp == null) {
            return;
        }
        xChangeMap.remove(Id);
        template.delete(baseURI + port + version + "/xchange/" + Id.toString());
    }
}