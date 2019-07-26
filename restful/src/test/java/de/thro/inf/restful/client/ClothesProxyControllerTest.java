package de.thro.inf.restful.client;

import de.thro.inf.restful.models.Address;
import de.thro.inf.restful.models.Clothes;
import de.thro.inf.restful.models.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static de.thro.inf.restful.models.Clothes.Sex.F;
import static de.thro.inf.restful.models.Clothes.Sex.M;
import static de.thro.inf.restful.models.Clothes.Size.S;
import static de.thro.inf.restful.models.Clothes.Size.XL;
import static de.thro.inf.restful.models.Clothes.Type.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClothesProxyControllerTest {

    Member hans;
    Member karl;
    Member hans2;
    Member hansupdate;
    Member karl2;
    Member gerald;

    Clothes nike;
    Clothes nike2;
    Clothes nikeUpdate;
    Clothes nikeUpdated;
    Clothes adidas;
    Clothes adidas2;

    private ProxyController proxy = null;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws Exception {

        proxy = new ProxyController(port);

        // For First Test findClothesById

        hans = new Member("pommeseater123", "Hans", "Pommes",
                new Address("Stachus 1", "80331", "Munich"),
                Long.parseLong("1"), "password", "hans@pommes.de",
                Long.parseLong("500"));

        karl = new Member("pizzaeater123", "Karl", "Pizza",
                new Address("Stachus 5", "80331", "Munich"),
                Long.parseLong("1"), "password", "karl@pizza.de",
                Long.parseLong("500"));

        nike = new Clothes(Long.parseLong("1"), "Pants", Long.parseLong("500"),
                S, M, PANTS, "Nike", hans, null, Long.parseLong("1"),
                Long.parseLong("125"));

        adidas = new Clothes(Long.parseLong("2"), "Trousers", Long.parseLong("400"),
                S, M, TROUSERS, "Adidas", karl, null, Long.parseLong("1"),
                Long.parseLong("100"));

        // For Second Test create

        hans2 = new Member("pommeseater87", "Hans", "Pommes2",
                new Address("Stachus 1", "80331", "Munich"),
                Long.parseLong("1"), "password", "hans87@pommes.de",
                Long.parseLong("500"));

        karl2 = new Member("pizzaeater87", "Karl", "Pizza2",
                new Address("Stachus 5", "80331", "Munich"),
                Long.parseLong("3"), "password", "karl87@pizza.de",
                Long.parseLong("500"));

        nike2 = new Clothes(Long.parseLong("3"), "Pants2", Long.parseLong("500"),
                S, M, PANTS, "Nike", hans, null, Long.parseLong("1"),
                Long.parseLong("125"));

        // For Delete Test

        gerald = new Member("Gerald123", "Gerald", "Spargel",
                new Address("Teplitzer Straße 7a", "84478", "Waldkraiburg"),
                Long.parseLong("1"), "password", "gerald@Spargel.de",
                Long.parseLong("500"));

        adidas2 = new Clothes(Long.parseLong("4"), "Trousers2", Long.parseLong("400"),
                S, M, TROUSERS, "Adidas", gerald, null, Long.parseLong("1"),
                Long.parseLong("100"));

        // For Update Test

        hansupdate = new Member("false123", "Hans", "NotExist",
                new Address("Stachus 1", "80331", "Munich"),
                Long.parseLong("1"), "password", "hans@pommes.de",
                Long.parseLong("500"));

        nikeUpdate = new Clothes(Long.parseLong("5"), "Pants2", Long.parseLong("500"),
                S, M, PANTS, "Nike", hansupdate, null, Long.parseLong("1"),
                Long.parseLong("125"));

        nikeUpdated = new Clothes(Long.parseLong("5"), "Shirt", Long.parseLong("500"),
                XL, F, SHIRT, "Puma", hansupdate, null, Long.parseLong("1"),
                Long.parseLong("250"));
    }

    @Test
    public void findClothesById() {
        proxy.createMember(hans);
        proxy.createMember(karl);

        proxy.createClothes(nike, proxy.findMemberByNickName(hans.getNickName()));
        proxy.createClothes(adidas, karl);

        Clothes tmp1 = proxy.findClothesById(nike.getId());
        Clothes tmp2 = proxy.findClothesById(adidas.getId());

        assertEquals(nike, tmp1);
        assertEquals(adidas, tmp2);
    }

    @Test
    public void createClothes() {
        proxy.createMember(hans2);
        proxy.createClothes(nike2, proxy.findMemberByNickName(hans2.getNickName()));
        Clothes tmp1 = proxy.findClothesById(nike2.getId());

        // First Creation
        assertEquals(nike2, tmp1);

        // Second Creation
        proxy.createClothes(nike2, hans2);
    }

    @Test
    public void deleteClothes() {
        proxy.createMember(gerald);
        proxy.createClothes(adidas2, gerald);
        Clothes tmp = proxy.findClothesById(adidas2.getId());

        assertEquals(adidas2, tmp);

        // First Delete
        proxy.deleteClothes(adidas2.getId());
        assertEquals(null, proxy.findClothesById(adidas2.getId()));

        // Second Delete
        proxy.deleteClothes(adidas2.getId());
        assertEquals(null, proxy.findClothesById(adidas2.getId()));
    }

    @Test
    public void updateClothes() {
        proxy.createMember(hansupdate);
        proxy.createClothes(nikeUpdate, proxy.findMemberByNickName(hansupdate.getNickName()));
        proxy.updateClothes(nikeUpdated, hansupdate);
        Clothes tmp = proxy.findClothesById(nikeUpdate.getId());

        assertEquals(nikeUpdated, tmp);
    }
}