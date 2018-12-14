package model;

import control.StaticData;
import model.abitur.datenbanken.mysql.DatabaseConnector;
import model.abitur.datenstrukturen.List;
import model.framework.GraphicalObject;
import view.framework.DrawTool;

import java.sql.*;

public class Tier extends GraphicalObject implements Lootable{

    private Statement stmt;
    private int id = 1,fullyGrown,cooldown;
    private String fleischArt = "",besonderheiten="";
    private double wachstum,wachstumsRate;
    private boolean grown;

    public Tier(String unterart, int farmID){

        switch(unterart){
            case "Kuh":
                fleischArt = "Rind";
                besonderheiten = "Milch";
                fullyGrown = 30;
                break;
            case "Huhn":
                fleischArt = "Geflügel";
                besonderheiten = "Eier";
                fullyGrown = 20;
                break;
            case "Schwein":
                fleischArt = "Schwein";
                besonderheiten = "leer";
                fullyGrown = 25;
                break;
        }
        wachstum = 0;
        wachstumsRate = 1/(Math.random()*3+13.5);

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
            stmt.execute("INSERT INTO "+StaticData.tier+"(fleischArt, besonderheiten, wachstumsRate,wachstum,farmID) VALUES ('"+fleischArt+"','"+besonderheiten+"',"+wachstumsRate+","+wachstum+","+farmID+");");
            System.out.println("Got new animal");
            ResultSet result = stmt.executeQuery("SELECT * FROM "+StaticData.tier+";");
            int i=0;
            while(result.next()){
                i++;
            }
            id = i;
            // System.out.println(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void draw(DrawTool drawTool){

    }

    @Override
    public void update(double dt){
        if (wachstum < fullyGrown) wachstum += wachstumsRate * dt;
        if (wachstum % 2 > 0 && wachstum % 2 < 0.1) updateDatenbank();
        if (wachstum >= fullyGrown & !grown){
            updateDatenbank();
            grown = true;
        }
        if (cooldown == GameTime.tag) {
            cooldown = 0;
        }
    }

    private void updateDatenbank(){
        try {
            stmt.execute("UPDATE "+ StaticData.tier+" SET wachstum = "+Math.round(wachstum)+" WHERE tierID="+id+";");
        } catch (SQLException e) {
            System.out.println("Die Daten von Tier wurden nicht updatet");
        }
    }

    public String getBesonderheiten(){return besonderheiten;}

    public String getFleischArt(){return fleischArt;}

    public double getWachstum(){return wachstum;}


    public int getId(){return id;}

    public boolean isGrown() {
        return grown;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
