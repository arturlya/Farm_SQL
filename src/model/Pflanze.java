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

public class Pflanze extends GraphicalObject implements Lootable{

    private Statement stmt;
    private int id,feldbelegung;
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
            drawTool.drawText(((id - 1) % 8) * 50 + 200, ((id - 1) / 8) * 50 + 33, "" + Math.round(wachstum));
            drawTool.drawImage(getMyImage(), ((id - 1) % 8) * 50 + 200, ((id - 1) / 8) * 50 + 20, 50, 50);
        }
    }

    @Override
    public void update(double dt){
        if (wachstum < 15) wachstum += wachstumsRate * dt;
        if (wachstum % 2 > 0 && wachstum % 2 < 0.1) updateDatenbank();
        if (wachstum >= 15 & !readyToHarvest){
            updateDatenbank();
            readyToHarvest = true;
        }
        if (cooldown == GameTime.tag) {
            cooldown = 0;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e){
        if (pc.getCurrentPanel() == 0) {
            if (hitBox.intersects(e.getX(), e.getY(), 1, 1)) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    Farm.loot(this);
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
            setMyImage(StaticData.weizen);
        }else if (pflanzenArt.equalsIgnoreCase("Mais")) {
            feldbelegung = 1;
        }else if (pflanzenArt.equalsIgnoreCase("Apfel")) {
            feldbelegung = 8;
        }else if (pflanzenArt.equalsIgnoreCase("Tomate")) {
            feldbelegung = 2;
        }else if (pflanzenArt.equalsIgnoreCase("Reis")) {
            feldbelegung = 1;
        }
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

    public String getPflanzenArt(){return pflanzenArt;}
    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
