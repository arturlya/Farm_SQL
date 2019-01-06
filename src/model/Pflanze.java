package model;

import control.Farm;
import control.ProgramController;
import control.StaticData;
import model.abitur.datenbanken.mysql.DatabaseConnector;
import model.abitur.datenstrukturen.List;
import model.framework.GraphicalObject;
import view.framework.DrawTool;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.sql.*;

public class Pflanze extends Lootable{

    private Statement stmt;
    private int id,feldbelegung,fullyGrown;
    private double wachstum,wachstumsRate;
    private boolean readyToHarvest;
    private ProgramController pc;
    private String pflanzenArt;
    private Rectangle2D.Double hitBox;
    private int cooldown;

    public Pflanze(String pflanzenArt, int farmID, ProgramController pc){
        this.pc = pc;
        this.pflanzenArt = pflanzenArt;
        datenErstellung(pflanzenArt,farmID);
        hitBox = new Rectangle2D.Double(((id - 1) % 8) * 50 + 200, ((id - 1) / 8) * 50 + 20, 50, 50);
    }

    @Override
    public void draw(DrawTool drawTool){
        if (pc.getCurrentPanel() == 0) {
            drawTool.setCurrentColor(100, 50, 5, 220);
            drawTool.drawFilledRectangle(((id - 1) % 8) * 50 + 200, ((id - 1) / 8) * 50 + 20, 50, 50);
            drawTool.setCurrentColor(255, 255, 255, 255);
            drawTool.drawText(((id - 1) % 8) * 50 + 200, ((id - 1) / 8) * 50 + 33, "" + Math.round((wachstum/fullyGrown)*100) + "%");
            drawTool.drawImage(getMyImage(), ((id - 1) % 8) * 50 + 200, ((id - 1) / 8) * 50 + 20, 50, 50);
        }
    }

    @Override
    public void update(double dt){
        if (wachstum < fullyGrown && cooldown == 0) wachstum += wachstumsRate * dt * GameTime.deltaMultiplier;
        if (wachstum % 2 > 0 && wachstum % 2 < 0.1 && cooldown == 0) updateDatenbank(0);
        if (wachstum >= fullyGrown & !readyToHarvest){
            updateDatenbank(1);
            readyToHarvest = true;
        }
        if (cooldown == GameTime.tag) {
            cooldown = 0;
        }
        if(kuemmert_sich.hasRelation(id)){
            Farm.loot(this);
            try{
                stmt.execute("UPDATE "+StaticData.pflanze+" SET readyToHarvest = 0 WHERE "+id+" = pflanzenID;");
            }catch(Exception e){System.err.println(e);}
            wachstum = 5;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if (pc.getCurrentPanel() == 0) {
            if (hitBox.intersects(e.getX(), e.getY(), 1, 1)) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Farm.loot(this);
                    try{
                        stmt.execute("UPDATE "+StaticData.pflanze+" SET readyToHarvest = 0 WHERE "+id+" = pflanzenID;");
                    }catch(Exception err){System.err.println(err);}
                    wachstum = 5;
                }
            }
        }
    }

    private void datenErstellung(String pflanzenArt, int farmID){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
        }catch (Exception e) {System.out.println("Keine Connection");}

        if (pflanzenArt.equalsIgnoreCase("Weizen")) {
            feldbelegung = 1;
            fullyGrown = 15;
            wachstumsRate = 1/(Math.random()*3+13.5);
            setMyImage(StaticData.weizen);
        }else if (pflanzenArt.equalsIgnoreCase("Mais")) {
            feldbelegung = 1;
            fullyGrown = 20;
        }else if (pflanzenArt.equalsIgnoreCase("Apfel")) {
            feldbelegung = 8;
            fullyGrown = 40;
        }else if (pflanzenArt.equalsIgnoreCase("Tomate")) {
            feldbelegung = 2;
            fullyGrown = 25;
        }else if (pflanzenArt.equalsIgnoreCase("Reis")) {
            feldbelegung = 1;
            fullyGrown = 20;
        }
        else feldbelegung = 0;

        try {
            stmt.execute("INSERT INTO "+StaticData.pflanze+" (pflanzenArt,wachstumsRate,wachstum,feldBelegung,farmID,readyToHarvest,cooldown) VALUES ('" + pflanzenArt + "','" + wachstumsRate + "'," + 0 + "," + feldbelegung + "," + farmID + ",0,0);");
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

    private void updateDatenbank(int fullyGrown){
        try {
            stmt.execute("UPDATE "+ StaticData.pflanze+" SET wachstum = "+Math.round(wachstum)+", readyToHarvest = "+fullyGrown+" WHERE pflanzenID="+id+";");
        } catch (SQLException e) {
            System.out.println("Die Daten von Pflanze wurden nicht updatet");
        }
    }

    public void setToStart(int cooldown){
        wachstum = 0;
        readyToHarvest = false;
        this.cooldown = cooldown;
        updateDatenbank(0);
    }

    public boolean isReadyToHarvest() {
        return readyToHarvest;
    }

    public String getPflanzenArt(){return pflanzenArt;}

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
    public int getID(){return id;}
}
