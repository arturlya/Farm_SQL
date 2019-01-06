package model;

import control.Farm;
import control.StaticData;
import model.framework.GraphicalObject;
import view.framework.DrawTool;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.sql.*;

public class Kunde extends GraphicalObject {

    private int id,anzahl,geld,nummer;
    private String name,wunsch;
    private Statement stmt;
    private boolean abgeschlossen,neuerKunde;
    private Rectangle2D.Double hitboxKauf,hitboxAblehnen;
    private Farm farm;

    public Kunde(Farm farm, int nummer){
        this.farm = farm;
        this.nummer = nummer;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        kundenErstellung();
        hitboxKauf = new Rectangle2D.Double(0,535+(50*nummer),160,40);
        hitboxAblehnen = new Rectangle2D.Double(160,535+(50*nummer),40,40);
        createAndSetNewImage("assets/müll.png");
    }

    @Override
    public void draw(DrawTool drawTool) {
        if (farm.isFarmWindowOpened()) {
            drawTool.setCurrentColor(50,50,50,255);
            drawTool.fill(hitboxKauf);
            drawTool.fill(hitboxAblehnen);
            drawTool.setCurrentColor(255,255,255,255);
            drawTool.drawText(10,550+(50*nummer),""+name);
            drawTool.drawText(10,570+(50*nummer),""+wunsch+"  x"+anzahl);
            drawTool.drawText(100,570+(50*nummer),""+geld*anzahl+"€");
            drawTool.drawImage(getMyImage(),164,539+(50*nummer),32,32);
        }
    }

    @Override
    public void update(double dt) {
        if (abgeschlossen) {
            abgeschlossen = false;
            int random1 = (int)(Math.random()*10);
            if (random1 == 0) wunsch = "Rind";
            if (random1 == 1) wunsch = "Schwein";
            if (random1 == 2) wunsch = "Geflügel";
            if (random1 == 3) wunsch = "Milch";
            if (random1 == 4) wunsch = "Eier";
            if (random1 == 5) wunsch = "Weizen";
            if (random1 == 6) wunsch = "Apfel";
            if (random1 == 7) wunsch = "Tomate";
            if (random1 == 8) wunsch = "Reis";
            if (random1 == 9) wunsch = "Mais";
            anzahl = (int)(Math.random()*10)+1;
            geld = (int)(Math.random()*11)+10;
            updateDatenbank();
            neuerKunde = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (hitboxKauf.intersects(e.getX(),e.getY(),1,1) && farm.lager.getResourceAmmount(wunsch) >= anzahl) {
            abgeschlossen = true;
            farm.lager.removeResource(wunsch,anzahl);
            try {
                stmt.execute("UPDATE "+StaticData.farm+" SET farmGeld = "+(farm.getFarmGeld()+(geld*anzahl))+" WHERE farmID = "+farm.getId()+";");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        if (hitboxAblehnen.intersects(e.getX(),e.getY(),1,1)) {
            abgeschlossen = true;
        }
    }

    public int getId() {
        return id;
    }

    public void setAbgeschlossen(boolean abgeschlossen) {
        this.abgeschlossen = abgeschlossen;
    }

    public void kundenErstellung(){
        id = (int)(Math.random()*20)+1;
        neuerKunde = false;
        try {
            ResultSet result = stmt.executeQuery("SELECT * FROM " + StaticData.kunde + " WHERE wunschID = " + id + ";");
            while (result.next()) {
                name = result.getString(2);
                wunsch = result.getString(3);
                anzahl = result.getInt(4);
                geld = result.getInt(5);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDatenbank(){
        try {
            stmt.execute("UPDATE "+StaticData.kunde+" SET kundenWunsch = '"+wunsch+"', anzahl = "+anzahl+", kundenGeld = "+geld+" WHERE wunschID = "+id+";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isNeuerKunde() {
        return neuerKunde;
    }
}
