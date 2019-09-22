package com.finplant.cryptoharvester.cryptoharvester.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.List;

@Table(name = "QUOTES")
@Entity
public class Quote {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private
    Integer id;
    @NotNull
    private
    Time time;
    @NotNull
    private
    BigDecimal bid;
    @NotNull
    private
    BigDecimal ask;
    @NotNull
    private
    String exchange;
    @NotNull
    private
    String name;


    @Transient
    private
    List<String> depends;

    public Quote() {
    }

    public List<String> getDepends() {
        return depends;
    }

    public void setDepends(List<String> depends) {
        this.depends = depends;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
