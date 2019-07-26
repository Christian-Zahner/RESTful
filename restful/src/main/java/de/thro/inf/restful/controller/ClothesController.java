package de.thro.inf.restful.controller;

import de.thro.inf.restful.models.Clothes;
import de.thro.inf.restful.models.Member;
import de.thro.inf.restful.repositorys.ClothesRepo;
import de.thro.inf.restful.repositorys.MemberRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(description = "Second Project RESTFUL Webservices",
        produces = MediaType.APPLICATION_JSON_VALUE,
        basePath = "api/v1")
@RestController
@RequestMapping(value = "api/v1")
public class ClothesController {

    @Autowired
    private ClothesRepo clothesRepo;

    @Autowired
    private MemberRepo memberRepo;

    @ApiOperation(value = "create a new clothes", response = Clothes.class)
    @ApiResponses(
            @ApiResponse(code = 201, message = "New clothing created")
    )
    @RequestMapping(

            value = "clothes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<Clothes> createClothes(@RequestBody Clothes clothes) {
        Member member = memberRepo.findById(clothes.getOwnerCl().getNickName()).orElse(null);

        if ((clothes.getExchangeValue() >= clothes.getOriginalPriceInEuroCent() * 0.1)
                || (clothes.getExchangeValue() <= clothes.getOriginalPriceInEuroCent() * 0.5)
                || member.getClothes().size() <= 10) {
            clothes.setOwnerCl(member);
            clothesRepo.save(clothes);
            return new ResponseEntity<>(
                    clothes, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(
                    HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE
            );
        }
    }

    @ApiOperation(value = "find clothing by its id", response = Clothes.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Clothes found"),
            @ApiResponse(code = 404, message = "No Clothes found")}
    )
    @RequestMapping(
            value = "clothes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    //Response Entity
    public Clothes findClothes(@PathVariable("id") String id) {

        Clothes result = clothesRepo.findById(Long.parseLong(id)).orElse(null);
        return result;
    }

    @ApiOperation(value = "update a given Clothing", response = Clothes.class)
    @ApiResponses(
            @ApiResponse(code = 200, message = "Clothing updated")
    )
    @RequestMapping(
            value = "clothes/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<Clothes> updateClothes(@PathVariable("id") String id,
                                                 @RequestBody Clothes clothes) {
        Clothes tmp = clothesRepo.findById(Long.parseLong(id)).orElse(null);

        if (tmp == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        tmp = changeClothesAttr(tmp, clothes);

        clothesRepo.save(tmp);
        return new ResponseEntity<>(
                tmp, HttpStatus.OK);
    }

    @ApiOperation(value = "delete clothing", response = Clothes.class)
    @ApiResponses(
            @ApiResponse(code = 202, message = "Clothing was deleted")
    )
    @RequestMapping(
            value = "clothes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<Clothes> deleteClothes(@PathVariable("id") Long id) {

        Clothes clothes = clothesRepo.findById(id).orElse(null);
        Member member = memberRepo.findById(clothes.getOwnerCl().getNickName()).orElse(null);
        if (clothes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (member == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        member.getClothes().remove(clothes);
        memberRepo.save(member);
        clothesRepo.delete(clothes);
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT);
    }

    private Clothes changeClothesAttr(Clothes actual, Clothes update) {

        if (update.getName() != null) {
            actual.setName(update.getName());
        }
        if (update.getOriginalPriceInEuroCent() != null) {
            actual.setOriginalPriceInEuroCent(update.getOriginalPriceInEuroCent());
        }
        if (update.getSize() != null) {
            actual.setSize(update.getSize());
        }
        if (update.getSex() != null) {
            actual.setSex(update.getSex());
        }
        if (update.getType() != null) {
            actual.setType(update.getType());
        }
        if (update.getBrand() != null) {
            actual.setBrand(update.getBrand());
        }
        if (update.getExchangeValue() != null) {
            if ((actual.getExchangeValue() >= actual.getOriginalPriceInEuroCent() * 0.1)
                    || (actual.getExchangeValue() < actual.getOriginalPriceInEuroCent() * 0.5)) {
                actual.setExchangeValue(update.getExchangeValue());
            }
        }
        return actual;
    }
}