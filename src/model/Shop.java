package model;

import control.Config;
import control.StaticData;
import control.framework.UIController;
import model.abitur.datenstrukturen.List;
import model.framework.GraphicalObject;
import view.framework.DrawTool;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.sql.*;

public class Shop extends GraphicalObject{

    private Statement stmt;
    private Rectangle2D.Double shopFrame,rec;
    private boolean shopOpened;
    private UIController uiController;
    private List<ShopElement> buttons;
    private double cooldown;
    private int id;
    private int farmID;

    public Shop(UIController uiController,int farmID){
        this.uiController = uiController;
        x = Config.WINDOW_WIDTH-50;
        y = 0;
        width = 200;
        height = Config.WINDOW_HEIGHT;
        shopFrame = new Rectangle2D.Double(x,y,width,height);
        cooldown = 0;
        this.farmID = farmID;
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();

            ResultSet result = stmt.executeQuery("SELECT * FROM "+StaticData.shop+";");
            int i=0;
            while(result.next()){
                i++;
            }
            id = i;
        }catch(SQLException e){
            e.printStackTrace();
        }

        buttons = new List<>();
        addShopElement(new ShopElement("Tier","Huhn",19.99,x+15,y+50));
        addShopElement(new ShopElement("Pflanze","Weizen",0.5,x+115,y+50));
    }

    @Override
    public void draw(DrawTool drawTool) {
        drawTool.setCurrentColor(0,0,0,100);
        drawTool.drawFilledRectangle(shopFrame);
        drawTool.setCurrentColor(0,0,0,255);
        if(shopOpened){
            drawTool.drawText(x+80,y+20,"SHOP");
            for(buttons.toFirst();buttons.hasAccess();buttons.next()){
                drawTool.drawImage(buttons.getContent().getMyImage(),buttons.getContent().getX(),buttons.getContent().getY(),75,75);
                drawTool.drawText(buttons.getContent().getX(),buttons.getContent().getY()+90,"Preis:"+buttons.getContent().getPreis()+"$");
            }
        }
    }

    @Override
    public void update(double dt) {
        shopFrame.setFrame(x,y,width,height);
        if(shopOpened ){
            x = Config.WINDOW_WIDTH-width;
            buttons.toFirst();
            for(int i=0;buttons.hasAccess();buttons.next()) {
                buttons.getContent().setX(x + 15 +i*100);
                i++;
            }
        }else{
            x = Config.WINDOW_WIDTH-50;
            buttons.toFirst();
            for(int i=0;buttons.hasAccess();buttons.next()) {
                buttons.getContent().setX(x + 15 +i*100);
                i++;
            }
        }
        if(cooldown > 0){
            cooldown = cooldown - 1*dt;
        }
    }


    public void addShopElement(/*String element,String unterart,double preis*/ShopElement shopElement){
        String element = shopElement.getElement();
        String unterart = shopElement.getUnterart();
        double preis = shopElement.getPreis();
        // System.out.println("Farm Schlüssel ist "+farmID);
        try { stmt.execute("INSERT INTO "+ StaticData.shop+" (entitaet,preis,entitaetsArt,farmID) VALUES ('"+element+"',"+preis+",'"+unterart+"',"+farmID+");");
            buttons.append(shopElement);
            uiController.drawObject(buttons.getContent());
        } catch (SQLException e) { e.printStackTrace(); }
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        if(shopFrame.intersects(e.getX(),e.getY(),1,1)){
            shopOpened = true;
        }else {shopOpened = false;}
    }

    public void mouseClicked(MouseEvent e){
        for (buttons.toFirst(); buttons.hasAccess(); buttons.next()) {
            rec = new Rectangle2D.Double(buttons.getContent().getX(),buttons.getContent().getY(),75,75);
            if(rec.intersects(e.getX(),e.getY(),1,1) &&  cooldown<=0){
                try {
                    ResultSet resultSet = stmt.executeQuery("SELECT farmGeld FROM "+StaticData.farm+" WHERE farmID = "+farmID);
                    double farmGeld = 0;
                    while(resultSet.next()) {
                        farmGeld = resultSet.getDouble("farmGeld");
                    }
                    if(farmGeld>= buttons.getContent().getPreis()){
                        //System.out.println("BUY");
                        switch(buttons.getContent().getElement()){
                            case "Tier":
                                Tier tier = new Tier(buttons.getContent().getUnterart(),farmID);
                                uiController.drawObjectOnPanel(tier,1);
                                break;
                            case "Pflanze":
                                Pflanze pflanze = new Pflanze(buttons.getContent().getUnterart(),farmID);
                                uiController.drawObjectOnPanel(pflanze,0);
                                break;
                            case "Mitarbeiter":

                                break;
                            case "Upgrade":

                                break;
                        }
                        stmt.execute("UPDATE "+StaticData.farm+" SET farmGeld="+(farmGeld-buttons.getContent().getPreis())+" WHERE farmID="+farmID);
                    }
                } catch (SQLException e1) { e1.printStackTrace(); }

                cooldown = 1;

            }
        }
    }

    public int getId(){return id;}

    private class ShopElement extends GraphicalObject{
        String element;
        String unterart;
        double preis;
        Rectangle2D.Double rec;


        //Jedes ShopElement benötigt ein Bild für das Icon!
        public ShopElement(String element,String unterart,double preis,double x, double y){
            this.element = element;
            this.preis = preis;
            this.unterart = unterart;
            this.x = x;
            this.y =y;
            rec = new Rectangle2D.Double(x,y,50,50);
            createAndSetNewImage("assets/"+unterart+".png");
        }



        public double getPreis() { return preis;        }

        public String getElement() { return element;        }

        public String getUnterart() { return unterart; }

        public Rectangle2D.Double getRec() { return rec; }
    }
}
