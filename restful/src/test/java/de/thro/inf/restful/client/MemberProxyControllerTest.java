package de.thro.inf.restful.client;

import de.thro.inf.restful.models.Address;
import de.thro.inf.restful.models.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberProxyControllerTest {

    Member hans;
    Member hansDontExist;
    Member karl;
    Member karlUpdate;
    Member gerald;

    private ProxyController proxy = null;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws Exception {

        proxy = new ProxyController(port);

        hans = new Member("pommeseater123", "Hans", "Pommes",
                new Address("Stachus 1", "80331", "Munich"),
                Long.parseLong("1"), "password", "hans@pommes.de",
                Long.parseLong("500"));

        hansDontExist = new Member("false123", "Hans", "NotExist",
                new Address("Stachus 1", "80331", "Munich"),
                Long.parseLong("1"), "password", "hans@pommes.de",
                Long.parseLong("500"));

        karl = new Member("karl123", "Karl", "Steak",
                new Address("Stachus 5", "80331", "Munich"),
                Long.parseLong("1"), "password", "karl@steak.de",
                Long.parseLong("500"));

        karlUpdate = new Member("karl123", "Bert", "Kartoffel",
                new Address("Graslitzer Straße 1", "84478", "Waldkraiburg"),
                Long.parseLong("1"), "password", "Bert@Kartoffel.de",
                Long.parseLong("500"));

        gerald = new Member("Gerald123", "gerald", "Spargel",
                new Address("Teplitzer Straße 7a", "84478", "Waldkraiburg"),
                Long.parseLong("1"), "password", "gerald@Spargel.de",
                Long.parseLong("500"));

    }

    @Test
    public void findMemberByNickName() {
        proxy.createMember(hans);
        Member tmp = proxy.findMemberByNickName(hans.getNickName());

        // Case for first find
        assertEquals(hans.getNickName(), tmp.getNickName());
        assertTrue(hans.equals(tmp));

        // Case for already in MemMap
        assertEquals(tmp, proxy.findMemberByNickName(hans.getNickName()));

        // Case for Not Exist
        assertEquals(null, proxy.findMemberByNickName(hansDontExist.getNickName()));
    }

    @Test
    public void createMember() {
        proxy.createMember(hans);
        Member tmp = proxy.findMemberByNickName(hans.getNickName());

        // Case for first Creation
        assertEquals(hans.toString(), tmp.toString());
        assertTrue(hans.equals(tmp));

        // Case for Second Creation already in HashMap
        assertEquals(tmp, proxy.createMember(hans));
    }

    @Test
    public void deleteMember() {
        proxy.createMember(gerald);
        Member tmp = proxy.findMemberByNickName(gerald.getNickName());
        assertEquals(gerald.getNickName(), tmp.getNickName());

        // First Delete
        proxy.deleteMember(gerald.getNickName());
        assertEquals(null, proxy.findMemberByNickName(gerald.getNickName()));

        // Second Delete
        proxy.deleteMember(gerald.getNickName());
        assertEquals(null, proxy.findMemberByNickName(gerald.getNickName()));
    }

    @Test
    public void updateMember() {
        proxy.createMember(karl);
        proxy.updateMember(karlUpdate);
        Member tmp = proxy.findMemberByNickName(karlUpdate.getNickName());

        assertEquals(karlUpdate.toString(), tmp.toString());
        assertTrue(karlUpdate.equals(tmp));

        assertEquals(null, proxy.updateMember(gerald));
    }
}