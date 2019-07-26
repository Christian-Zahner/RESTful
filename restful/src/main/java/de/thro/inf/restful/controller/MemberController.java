package de.thro.inf.restful.controller;

import de.thro.inf.restful.models.Address;
import de.thro.inf.restful.models.Member;
import de.thro.inf.restful.repositorys.MemberRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@Api(description = "Second Project RESTFUL Webservices",
        produces = MediaType.APPLICATION_JSON_VALUE,
        basePath = "api/v1")
@RestController
@RequestMapping(value = "api/v1")
public class MemberController {

    @Autowired
    private MemberRepo repository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @ApiOperation(value = "create a new customers", response = Member.class)
    @ApiResponses(
            @ApiResponse(code = 201, message = "New customer created")
    )
    @RequestMapping(

            value = "member",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )


    public ResponseEntity<Member> createMember(@RequestBody Member member) {  //http://localhost:8080/member/asdf

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        repository.save(member);
        return new ResponseEntity<>(
                member, HttpStatus.CREATED);
    }

    @ApiOperation(value = "find a customer by his or her id", response = Member.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Customer found"),
            @ApiResponse(code = 404, message = "No customer found")}
    )
    @RequestMapping(
            value = "member/{nickName}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE

    )

    // Response Entity
    public Member findMember(@PathVariable("nickName") String nickName) {  //http://localhost:8080/member/asdf

        Member result = repository.findById(nickName).orElse(null);
        return result;
    }

    @ApiOperation(value = "update a given Customer", response = Member.class)
    @ApiResponses(
            @ApiResponse(code = 200, message = "Customer updated")
    )
    @RequestMapping(
            value = "member/{nickName}",
            method = {RequestMethod.PUT, RequestMethod.PATCH},
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE

    )

    public ResponseEntity<Member> updateMember(@PathVariable("nickName") String nickName,
                                               @RequestBody Member member) {  //http://localhost:8080/member/asdf
        Member tmp = repository.findById(nickName).orElse(null);

        if (tmp == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tmp = changeMemberAttr(member, tmp);

        if(member.getPassword() != null) {
            tmp.setPassword(passwordEncoder.encode(tmp.getPassword()));
        }
        repository.save(tmp);
        return new ResponseEntity<>(
                member, HttpStatus.OK);
    }

    @ApiOperation(value = "delete customer", response = Member.class)
    @ApiResponses(
            @ApiResponse(code = 202, message = "Customer was deleted")
    )
    @RequestMapping(
            value = "member/{nickName}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<Member> deleteMember(@PathVariable("nickName") String nickName) {  //http://localhost:8080/member/asdf

        Member member = repository.findById(nickName).orElse(null);
        if (member == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.delete(member);
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT);
    }

    public Member changeMemberAttr(@RequestBody Member update, Member actual) {
        // Just set Attributes you get in the request
        if (update.getFirstName() != null) {
            actual.setFirstName(update.getFirstName());
        }
        if (update.getLastName() != null) {
            actual.setLastName(update.getLastName());
        }
        if (update.getVersion() != null) {
            actual.setVersion(update.getVersion());
        }
        if (update.getMail() != null) {
            actual.setMail(update.getMail());
        }
        if (update.getAddress() != null) {
            actual.setAddress(changeAddress(update, actual));
        }
        if (update.getPassword() != null) {
            actual.setPassword(update.getPassword());
        }
        return actual;
    }

    public Address changeAddress(Member update, Member actual) {
        Address tmpAddress = actual.getAddress();
        Address memAddress = update.getAddress();

        // Just set Attributes you get in the request
        if (memAddress.getCity() != null) {
            tmpAddress.setCity(memAddress.getCity());
        }
        if (memAddress.getPostcode() != null) {
            tmpAddress.setPostcode(memAddress.getPostcode());
        }
        if (memAddress.getStreet() != null) {
            tmpAddress.setStreet(memAddress.getStreet());
        }
        return tmpAddress;
    }
}