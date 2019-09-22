package com.finplant.cryptoharvester.cryptoharvester.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties
@Configuration
public class Instruments {

    public Instruments(){}

    public List<Instrument> instruments = new ArrayList<>();

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    @Override
    public String toString() {
        if(instruments==null){return "Instruments null";}
        StringBuilder builder = new StringBuilder();
        for (Instrument i:instruments) {
            builder.append("Instrument "+i.getName()+"\n"+i.toString());
        }

        return builder.toString();
    }

}
