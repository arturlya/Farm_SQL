package control;

import control.framework.UIController;
import model.*;
import model.framework.GraphicalObject;
import view.framework.DrawTool;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Farm extends GraphicalObject {

    private static Statement stmt;
    public static Lager lager;
    private BufferedImage grassImage;
    private static UIController uiController;


    private boolean farmWindowOpened;
    private Rectangle2D.Double farmWindowFrame;
    private int id;
    private double farmGeld;
    private ResultSet result;

    public Farm(UIController uiController){
        this.uiController = uiController;
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
        lager = new Lager(id);
    }

    @Override
    public void draw(DrawTool drawTool) {
        drawTool.drawImage(grassImage,0,0);
        drawTool.setCurrentColor(0,0,0,100);
        drawTool.drawFilledRectangle(farmWindowFrame);
        drawTool.setCurrentColor(255,255,255,255);
        drawTool.drawText(x+80,y+40,"FARM");
        drawTool.drawText(x+20,y+120,"Geld : "+farmGeld);
        drawTool.drawText(x+20,y+160,"Lagerplatz: "+lager.getBelegterPlatz()+"/"+lager.getLagerplatz());
        drawTool.drawText(x+20,y+200,"Pflanzenmitarbeiter: "+Mitarbeiter.getNumberOfPflanzenMitarbeiter());
        drawTool.drawText(x+20,y+240,"Tiermitarbeiter: "+Mitarbeiter.getNumberOfTierMitarbeiter());
    }

    @Override
    public void update(double dt) {
        farmWindowFrame.setFrame(x,y,200,Config.WINDOW_HEIGHT);
        if(farmWindowOpened){
            x = 0;
        }else{
            x = -150;
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

    public static void kill(Tier tier){
        int fleischMenge;
        String fleischArt;
        fleischArt = tier.getFleischart();
        if(tier.getWachstum() >= tier.getFullyGrown()){
            fleischMenge = (int)(Math.random()*4+4);
            lager.storeResource(new Resource(fleischArt,fleischMenge));
            System.out.println("Tier für "+fleischMenge+" Fleisch getötet");
        }else if(tier.getWachstum() >= tier.getFullyGrown() / 2){
            fleischMenge = (int)(Math.random()*3+1);
            lager.storeResource(new Resource(fleischArt,fleischMenge));
            System.out.println("Tier für "+fleischMenge+" Fleisch getötet");
        }else{
            System.out.println("Das Tier ist zu jung um es zu töten");
        }
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
            stmt.execute("DELETE FROM "+StaticData.tier+" WHERE tierID = "+tier.getId()+";");
            uiController.removeObject(tier);
            System.out.println("Deleted animal");
        }catch(SQLException e){e.printStackTrace();}
    }

    public static void loot(Lootable lootable){
        if(lootable instanceof Tier){
            if(!((Tier) lootable).getBesonderheiten().equals("leer")){
                if(((Tier) lootable).isGrown() && ((Tier) lootable).getCooldown() <= 0){
                    ((Tier) lootable).setCooldown(GameTime.tag+7);
                    lager.storeResource(new Resource(((Tier) lootable).getBesonderheiten(),1));
                    System.out.println(((Tier) lootable).getBesonderheiten()+" vom Tier bekommen");
                }
            }
        }else if(lootable instanceof Pflanze){
            if(((Pflanze) lootable).isReadyToHarvest() && ((Pflanze) lootable).getCooldown() <= 0) {
                ((Pflanze) lootable).setToStart(GameTime.tag+7);
                lager.storeResource(new Resource(((Pflanze) lootable).getPflanzenArt(), 1));
                System.out.println("Ernte 1 " + ((Pflanze) lootable).getPflanzenArt());
            }
        }
    }

    public int getId() {
        return id;
    }

    //  public static Lager getLager(){return lager;}

    public boolean isFarmWindowOpened() {
        return farmWindowOpened;
    }

    public double getFarmGeld() {
        return farmGeld;
    }

    public void removeFarmGeld(double ammount){farmGeld = farmGeld-ammount;}
}
