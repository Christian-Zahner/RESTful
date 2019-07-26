package de.thro.inf.restful.client;


import de.thro.inf.restful.models.Address;
import de.thro.inf.restful.models.Clothes;
import de.thro.inf.restful.models.Member;
import de.thro.inf.restful.models.XChange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static de.thro.inf.restful.models.Clothes.Sex.M;
import static de.thro.inf.restful.models.Clothes.Size.S;
import static de.thro.inf.restful.models.Clothes.Type.PANTS;
import static de.thro.inf.restful.models.Clothes.Type.TROUSERS;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class XchangeProxyControllerTest {

    XChange xChange;
    XChange xChange2;
    XChange xChange3;
    LocalDate date;

    Member hans;
    Member hans2;
    Member karl;
    Member karl2;
    Member gerald;
    Member gerald2;

    Clothes nike;
    Clothes nike2;
    Clothes nike3;
    Clothes adidas;
    Clothes adidas2;
    Clothes adidas3;

    private ProxyController proxy = null;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws Exception {

        proxy = new ProxyController(port);

        karl = new Member("pizzaeater123", "Karl", "Pizza",
                new Address("Stachus 5", "80331", "Munich"),
                Long.parseLong("5"), "password", "karl@pizza.de",
                Long.parseLong("500"));

        karl2 = new Member("pizzaeater87", "Karl", "Pizza2",
                new Address("Stachus 5", "80331", "Munich"),
                Long.parseLong("3"), "password", "karl87@pizza.de",
                Long.parseLong("500"));

        hans = new Member("pommeseater123", "Hans", "Pommes",
                new Address("Stachus 1", "80331", "Munich"),
                Long.parseLong("6"), "password", "hans@pommes.de",
                Long.parseLong("500"));

        hans2 = new Member("pommeseater87", "Hans", "Pommes2",
                new Address("Stachus 1", "80331", "Munich"),
                Long.parseLong("1"), "password", "hans87@pommes.de",
                Long.parseLong("500"));


        gerald = new Member("Gerald123", "Gerald", "Spargel",
                new Address("Teplitzer Straße 7a", "84478", "Waldkraiburg"),
                Long.parseLong("1"), "password", "gerald@Spargel.de",
                Long.parseLong("500"));

        gerald2 = new Member("Gerald67", "Gerald2", "Spargel",
                new Address("Teplitzer Straße 7a", "84478", "Waldkraiburg"),
                Long.parseLong("1"), "password", "gerald@Spargel.de",
                Long.parseLong("500"));

        nike = new Clothes(Long.parseLong("5"), "Pants", Long.parseLong("500"),
                S, M, PANTS, "Nike", hans, null, Long.parseLong("1"),
                Long.parseLong("125"));

        nike2 = new Clothes(Long.parseLong("3"), "Pants2", Long.parseLong("500"),
                S, M, PANTS, "Nike", karl2, null, Long.parseLong("1"),
                Long.parseLong("125"));

        nike3 = new Clothes(Long.parseLong("45"), "Pants3", Long.parseLong("500"),
                S, M, PANTS, "Nike", hans2, null, Long.parseLong("1"),
                Long.parseLong("125"));

        adidas = new Clothes(Long.parseLong("6"), "Trousers", Long.parseLong("400"),
                S, M, TROUSERS, "Adidas", karl, null, Long.parseLong("1"),
                Long.parseLong("100"));

        adidas2 = new Clothes(Long.parseLong("4"), "Trousers2", Long.parseLong("400"),
                S, M, TROUSERS, "Adidas", gerald, null, Long.parseLong("1"),
                Long.parseLong("100"));

        adidas3 = new Clothes(Long.parseLong("123"), "Trousers2", Long.parseLong("400"),
                S, M, TROUSERS, "Adidas", gerald2, null, Long.parseLong("1"),
                Long.parseLong("100"));

        date = LocalDate.of(2019, 6, 8);

        xChange = new XChange(Long.parseLong("17"), date, nike, adidas);
        xChange2 = new XChange(Long.parseLong("12"), LocalDate.now(), nike2, adidas2);
        xChange3 = new XChange(Long.parseLong("22"), LocalDate.now(), nike3, adidas3);
    }

    @Test
    public void findXChangeById() {
        // No XChange exist
        assertEquals(null, proxy.findXChangeById(xChange2.getId()));

        proxy.createMember(gerald);
        proxy.createMember(karl2);

        proxy.createClothes(nike2, karl2);
        proxy.createClothes(adidas2, gerald);

        proxy.createXChange(xChange2);

        // XChange exist
        assertEquals(xChange2.getId(), proxy.findXChangeById(xChange2.getId()).getId());

        // XChange exist second search
        assertEquals(xChange2.getId(), proxy.findXChangeById(xChange2.getId()).getId());
    }

    @Test
    public void createXChange() {
        proxy.createMember(hans);
        proxy.createMember(karl);

        proxy.createClothes(nike, hans);
        proxy.createClothes(adidas, karl);

        proxy.createXChange(xChange);

        Member result = proxy.findMemberByNickName(hans.getNickName());
        Member result2 = proxy.findMemberByNickName(karl.getNickName());
        Clothes change = result.getClothes().get(0);
        Clothes change2 = result2.getClothes().get(0);

        assertEquals(adidas.getId(), change.getId());
        assertEquals(nike.getId(), change2.getId());

        assertEquals(xChange.getId(), proxy.createXChange(xChange).getId());
    }

    @Test
    public void deleteXChange() {
        proxy.createMember(hans2);
        proxy.createMember(gerald2);

        proxy.createClothes(nike3, hans2);
        proxy.createClothes(adidas3, gerald2);

        proxy.createXChange(xChange3);

        // Creation correct?
        assertEquals(xChange3.getId(), proxy.findXChangeById(xChange3.getId()).getId());

        // First Delete should return null
        proxy.deleteXChange(xChange3.getId());

        assertEquals(null, proxy.findXChangeById(xChange3.getId()));

        // Second Delete should be null as well
        proxy.deleteXChange(xChange3.getId());
        assertEquals(null, proxy.findXChangeById(xChange3.getId()));
    }
}