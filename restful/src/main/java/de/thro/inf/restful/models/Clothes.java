package de.thro.inf.restful.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Clothes {
    public enum Size {S, M, L, XL}

    public enum Sex {M, F, U}

    public enum Type {TROUSERS, PANTS, DRESS, SHIRT, TEE, BLOUSE, SKIRT, UNDERWEAR}

    @Id
    private Long id;

    private String name;

    @NotNull
    private Long originalPriceInEuroCent;

    private Size size;

    private Sex sex;

    private Type type;

    private String brand;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private Member ownerCl;

    @ManyToOne
    private XChange xChangeTransaction;

    @Version
    private Long version;

    private Long exchangeValue;

    public Clothes() {
    }

    public Clothes(Long id, String name, @NotNull Long originalPriceInEuroCent, Size size, Sex sex, Type type,
                   String brand, Member ownerCl, XChange xChangeTransaction, Long version, Long exchangeValue) {
        this.id = id;
        this.name = name;
        this.originalPriceInEuroCent = originalPriceInEuroCent;
        this.size = size;
        this.sex = sex;
        this.type = type;
        this.brand = brand;
        this.ownerCl = ownerCl;
        this.xChangeTransaction = xChangeTransaction;
        this.version = version;
        this.exchangeValue = exchangeValue;
    }

    public void setOriginalPriceInEuroCent(Long originalPriceInEuroCent) {
        this.originalPriceInEuroCent = originalPriceInEuroCent;
    }

    public Long getExchangeValue() {
        return exchangeValue;
    }

    public void setExchangeValue(Long exchangeValue) {
        this.exchangeValue = exchangeValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginalPriceInEuroCent() {
        return originalPriceInEuroCent;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @JsonIgnore
    public Member getOwnerCl() {
        return ownerCl;
    }

    @JsonProperty
    public void setOwnerCl(Member ownerCl) {
        this.ownerCl = ownerCl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @JsonIgnore
    public XChange getXChangeTransaction() {
        return xChangeTransaction;
    }

    @JsonProperty
    public void setXChangeTransaction(XChange xChangeTransaction) {
        this.xChangeTransaction = xChangeTransaction;
    }

    @Override
    public String toString() {
        return "Clothes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", originalPriceInEuroCent=" + originalPriceInEuroCent +
                ", size=" + size +
                ", sex=" + sex +
                ", type=" + type +
                ", brand='" + brand + '\'' +
                ", ownerCl=" + ownerCl +
                ", xChangeTransaction=" + xChangeTransaction +
                ", version=" + version +
                ", exchangeValue=" + exchangeValue +
                '}';
    }
}