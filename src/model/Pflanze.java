package model;

import model.framework.GraphicalObject;

public class Pflanze extends GraphicalObject {

    private String art;
    private double wachstumsRate,wachstum;

    public Pflanze(String art, double wachstumsRate){
        this.art = art;
        this.wachstumsRate = wachstumsRate;
    }
}
