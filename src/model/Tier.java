package model;

import control.StaticData;
import model.abitur.datenbanken.mysql.DatabaseConnector;
import model.abitur.datenstrukturen.List;
import model.framework.GraphicalObject;
import view.framework.DrawTool;

import java.sql.*;

public class Tier extends GraphicalObject {

    private Statement stmt;
    private int id = 1;
    private String fleischArt,besonderheiten;
    private double wachstum;

    public Tier(String fleischArt, String besonderheiten,double wachstumsRate, double wachstum, int farmID){

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
            stmt.execute("INSERT INTO "+StaticData.tier+"(fleischArt, besonderheiten, wachstumsRate,wachstum,farmID) VALUES ('"+fleischArt+"','"+besonderheiten+"',"+wachstumsRate+","+wachstum+","+farmID+");");
            System.out.println("Got new animal");
            ResultSet result = stmt.executeQuery("SELECT * FROM "+StaticData.tier+";");
            while(result.next()){
                int i=1;
                System.out.println(result.getString(i));
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void draw(DrawTool drawTool){

    }

    @Override
    public void update(double dt){
        try {
            stmt.execute("UPDATE "+ StaticData.tier+" SET wachstum = wachstum + wachstumsRate*" +dt+" WHERE tierID=="+id+";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
