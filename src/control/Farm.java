package control;

import model.Lager;
import model.Resource;
import model.Tier;
import model.abitur.datenbanken.mysql.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Farm {

    private Statement stmt;


    public Farm(){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
            stmt.execute("INSERT INTO "+StaticData.farm+"(farmGeld,farmName,farmFelder) VALUES(500,'TESTFARM',100);");
        }catch(Exception err){err.printStackTrace();}
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
}
