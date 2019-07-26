package de.thro.inf.restful.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
public class XChange {

    @Id
    private Long id;

    @DateTimeFormat
    private LocalDate exchangeDate;

    @OneToMany(mappedBy = "xChangeTransaction")
    private List<Clothes> XChangeClothes = new LinkedList<>();

    public XChange(Long id, LocalDate exchangeDate, Clothes cloth1, Clothes cloth2) {
        this.id = id;
        this.exchangeDate = exchangeDate;
        this.XChangeClothes.add(cloth1);
        this.XChangeClothes.add(cloth2);
    }

    public List<Clothes> getXChangeClothes() {
        return XChangeClothes;
    }

    public void setXChangeClothes(List<Clothes> XChangeClothes) {
        this.XChangeClothes = XChangeClothes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(LocalDate exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public XChange() {
    }
}