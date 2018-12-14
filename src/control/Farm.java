package control;

import model.*;
import model.framework.GraphicalObject;
import view.framework.DrawTool;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Farm extends GraphicalObject {

    private Statement stmt;
    BufferedImage grassImage;


    private boolean farmWindowOpened;
    private Rectangle2D.Double farmWindowFrame;
    private int id;
    private double farmGeld;
    private ResultSet result;

    public Farm(){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
            stmt.execute("INSERT INTO "+StaticData.farm+"(farmGeld,farmName,farmFelder) VALUES(500,'TESTFARM',100);");
            result = stmt.executeQuery("SELECT * FROM "+StaticData.farm+";");
            int i=0;
            while(result.next()){
                i++;
            }
            id = i;

            result = stmt.executeQuery("SELECT farmGeld FROM "+StaticData.farm+" WHERE farmID = "+id);
            while(result.next()){
                farmGeld = result.getDouble("farmGeld");
            }
        }catch(Exception err){err.printStackTrace();}

        x = -150;
        y = 0;
        farmWindowFrame = new Rectangle2D.Double(x,y,200,Config.WINDOW_HEIGHT);
        try {
            grassImage = ImageIO.read(new File("assets/grassTile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(DrawTool drawTool) {
        drawTool.drawImage(grassImage,0,0);
        drawTool.setCurrentColor(0,0,0,100);
        drawTool.drawFilledRectangle(farmWindowFrame);
        drawTool.setCurrentColor(0,0,0,255);
        drawTool.drawText(x+80,y+40,"FARM");
        drawTool.drawText(x+20,y+120,"Geld : "+farmGeld);
    }

    @Override
    public void update(double dt) {
        farmWindowFrame.setFrame(x,y,200,Config.WINDOW_HEIGHT);
        if(farmWindowOpened){
            if(x<0) {
                x = x + 70 * dt;

            }else{
                x = 0;
            }
        }else{
            if(x>-150) {
                x = x - 70 * dt;
            }else{
                x = -150;
            }
        }

        try {
            result = stmt.executeQuery("SELECT farmGeld FROM "+StaticData.farm+" WHERE farmID = "+id);
            while(result.next()){
                farmGeld = result.getDouble("farmGeld");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void mouseMoved(MouseEvent e) {
        if(farmWindowFrame.intersects(e.getX(),e.getY(),1,1)){
            farmWindowOpened = true;
        }else {
            farmWindowOpened = false;}
    }

    public void kill(Tier tier, Lager lager){
        int fleischMenge;
        String fleischArt;
        fleischArt = "Rind";
        if(tier.getFleischArt().equals(fleischArt)){
            if(tier.getWachstum() >= 15){
                fleischMenge = (int)(Math.random()*5+1);
                lager.storageResource(new Resource(fleischArt,fleischMenge));
                System.out.println("Tier für "+fleischMenge+" Fleisch getötet");
            }else if(tier.getWachstum() >= 7){

                fleischMenge = (int)(Math.random()*3+1);
                lager.storageResource(new Resource(fleischArt,fleischMenge));
                System.out.println("Tier für "+fleischMenge+" Fleisch getötet");
            }
            try{
                Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
                stmt = con.createStatement();
                stmt.execute("DELETE FROM "+StaticData.tier+" WHERE tierID = "+tier.getId()+";");
            }catch(SQLException e){e.printStackTrace();}
        }
    }

    public void loot(Lootable lootable, Lager lager){
        if(lootable instanceof Tier){
            if(!((Tier) lootable).getBesonderheiten().equals("leer")){
                if(((Tier) lootable).isGrown() && ((Tier) lootable).getCooldown() <= 0){
                    ((Tier) lootable).setCooldown(GameTime.tag+7);
                    lager.storageResource(new Resource(((Tier) lootable).getBesonderheiten(),1));
                }
            }
        }
    }

    public int getId() {
        return id;
    }
}
