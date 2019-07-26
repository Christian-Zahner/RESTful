package de.thro.inf.restful.controller;

import de.thro.inf.restful.models.Clothes;
import de.thro.inf.restful.models.Member;
import de.thro.inf.restful.models.XChange;
import de.thro.inf.restful.repositorys.ClothesRepo;
import de.thro.inf.restful.repositorys.XChangeRepo;
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
public class XChangeController {

    @Autowired
    private XChangeRepo xChangeRepo;

    @Autowired
    private ClothesRepo clothesRepo;

    @ApiOperation(value = "create a new exchange", response = XChange.class)
    @ApiResponses(
            @ApiResponse(code = 201, message = "New exchange created")
    )
    @RequestMapping(

            value = "xchange",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )


    public ResponseEntity<XChange> createXChange(@RequestBody XChange xchange) {  //http://localhost:8080/xchsnge/asdf

        xChangeRepo.save(xchange);

        Clothes tmpClothing1 = clothesRepo.findById(xchange.getXChangeClothes().get(0).getId()).orElse(null);
        Clothes tmpClothing2 = clothesRepo.findById(xchange.getXChangeClothes().get(1).getId()).orElse(null);

        tmpClothing1.setXChangeTransaction(xchange);
        tmpClothing2.setXChangeTransaction(xchange);

        xchange.getXChangeClothes().set(0, tmpClothing1);
        xchange.getXChangeClothes().set(1, tmpClothing2);

        clothesRepo.save(tmpClothing1);
        clothesRepo.save(tmpClothing2);

        xChangeRepo.save(xchange);

        Member tmpOwner = tmpClothing1.getOwnerCl();
        tmpClothing1.setOwnerCl(tmpClothing2.getOwnerCl());
        tmpClothing2.setOwnerCl(tmpOwner);

        tmpClothing1.getOwnerCl().setCredit(tmpClothing1.getOwnerCl().getCredit() - 50);
        tmpClothing2.getOwnerCl().setCredit(tmpClothing2.getOwnerCl().getCredit() - 50);

        clothesRepo.save(tmpClothing1);
        clothesRepo.save(tmpClothing2);

        return new ResponseEntity<>(
                xchange, HttpStatus.CREATED);
    }

    @ApiOperation(value = "find a exchange by its id", response = XChange.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "echange found"),
            @ApiResponse(code = 404, message = "No exchange found")}
    )
    @RequestMapping(
            value = "xchange/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    // Response Entity
    public XChange findXChange(@PathVariable("id") Long whichTransaction) {  //http://localhost:8080/xchsnge/asdf

        XChange result = xChangeRepo.findById(whichTransaction).orElse(null);
        return result;
    }

    // Update of Transaction is not allowed!!!

    //  @RequestMapping(
    //          value = "xchange/{id}",
    //          method = RequestMethod.PUT,
    //          produces = MediaType.APPLICATION_JSON_VALUE,
    //          consumes = MediaType.APPLICATION_JSON_VALUE
    //  )

    //  public ResponseEntity<XChange> updateXChange(@RequestBody XChange xchange) {  //http://localhost:8080/xchsnge/asdf

    //      xChangeRepo.save(xchange);
    //      return new ResponseEntity<>(
    //              xchange, HttpStatus.OK);
    //  }

    @ApiOperation(value = "delete exchange", response = XChange.class)
    @ApiResponses(
            @ApiResponse(code = 202, message = "Exchange was deleted")
    )
    @RequestMapping(
            value = "xchange/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<XChange> deleteXChange(@PathVariable("id") Long whichTransaction) {  //http://localhost:8080/xchsnge/asdf

        XChange xChange = xChangeRepo.findById(whichTransaction).orElse(null);

        Clothes tmpClothing1 = clothesRepo.findById(xChange.getXChangeClothes().get(0).getId()).orElse(null);
        Clothes tmpClothing2 = clothesRepo.findById(xChange.getXChangeClothes().get(1).getId()).orElse(null);

        tmpClothing1.setXChangeTransaction(null);
        tmpClothing2.setXChangeTransaction(null);

        clothesRepo.save(tmpClothing1);
        clothesRepo.save(tmpClothing2);

        if (xChange == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        xChangeRepo.delete(xChange);
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT);
    }
}