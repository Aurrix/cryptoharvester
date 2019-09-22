package com.finplant.cryptoharvester.cryptoharvester.config;

import java.util.ArrayList;
import java.util.List;

public class Instrument{

    public Instrument(){}

    public String name;
    public String instrument;
    public List<String> depends = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public List<String> getDepends() {
        return depends;
    }

    public void setDepends(ArrayList<String> depends) {
        this.depends = depends;
    }

    @Override
    public String toString(){
        return "Name: "+name+"\n"+
                "Instrument: "+instrument+"\n"+
                "Depends: "+depends.toString()+"\n"
                ;
    }

}