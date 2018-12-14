package model;

import control.StaticData;
import model.abitur.datenbanken.mysql.DatabaseConnector;
import model.abitur.datenstrukturen.List;
import model.framework.GraphicalObject;
import view.framework.DrawTool;

import java.sql.*;

public class Pflanze extends GraphicalObject implements Lootable{

    private Statement stmt;
    private int id,feldbelegung;
    private double wachstum,wachstumsRate;
    private boolean readyToHarvest;

    public Pflanze(String pflanzenArt, int farmID){
        datenErstellung(pflanzenArt,farmID);
    }



    @Override
    public void draw(DrawTool drawTool){
        drawTool.drawText(200,200+(id*15),""+wachstum);
    }

    @Override
    public void update(double dt){
        if (wachstum < 15) wachstum += wachstumsRate * dt;
        if (wachstum % 2 > 0 && wachstum % 2 < 0.1) updateDatenbank();
        if (wachstum >= 15 & !readyToHarvest){
            updateDatenbank();
            readyToHarvest = true;
        }

    }

    private void datenErstellung(String pflanzenArt, int farmID){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
        }catch (Exception e) {System.out.println("Keine Connection");}

        if (pflanzenArt.equalsIgnoreCase("Weizen")) feldbelegung = 1;
        else if (pflanzenArt.equalsIgnoreCase("Mais")) feldbelegung = 1;
        else if (pflanzenArt.equalsIgnoreCase("Apfel")) feldbelegung = 8;
        else if (pflanzenArt.equalsIgnoreCase("Tomate")) feldbelegung = 2;
        else if (pflanzenArt.equalsIgnoreCase("Reis")) feldbelegung = 1;
        else feldbelegung = 0;

        if (feldbelegung != 0) {
            wachstumsRate = 1/(Math.random()*3+13.5);
        }

        try {
            stmt.execute("INSERT INTO "+StaticData.pflanze+" (pflanzenArt,wachstumsRate,wachstum,feldBelegung,farmID) VALUES ('" + pflanzenArt + "','" + wachstumsRate + "'," + 0 + "," + feldbelegung + "," + farmID + ");");
        }catch (Exception e) {
            System.out.println("Keine Werte für Pflanze");
        }
        try {
            ResultSet result = stmt.executeQuery("SELECT * FROM " + StaticData.pflanze + ";");
            int i = 0;
            while (result.next()) {
                i++;
            }
            id = i;
        }catch (Exception e) {
            System.out.println("Keine ID für Pflanze");
        }
    }

    private void updateDatenbank(){
        try {
            stmt.execute("UPDATE "+ StaticData.pflanze+" SET wachstum = "+Math.round(wachstum)+" WHERE pflanzenID="+id+";");
        } catch (SQLException e) {
            System.out.println("Die Daten von Pflanze wurden nicht updatet");
        }
    }

    public boolean isReadyToHarvest() {
        return readyToHarvest;
    }
}
