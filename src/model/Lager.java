package model;

import control.StaticData;
import model.abitur.datenstrukturen.List;

import java.sql.*;

public class Lager {

    private List<Resource> lager;

    private int lagerplatz;
    private int belegterPlatz;

    public Lager(int farmID){
        loadLagerData(farmID);
        lager = new List<>();
    }

    public void storageResource(Resource r){
        boolean added = false;
        lager.toFirst();
        while(lager.hasAccess()){
            if(lager.getContent().getName().equals(r.getName())){
                //while(lagerplatz-belegterPlatz < r.getAmmount()){
                 //   r.removeAmmount(1);
               // }
                lager.getContent().addAmmount(r.getAmmount());
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
                    Statement stmt = con.createStatement();

                    stmt.execute("UPDATE "+StaticData.lager+" SET belegterPlatz = belegterPlatz+"+r.getAmmount()+" WHERE lagerID = 1;");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                added = true;

            }
        }
        if(!added){
           // while(lagerplatz-belegterPlatz < r.getAmmount()){
            //    r.removeAmmount(1);
           // }
            lager.append(r);
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
                Statement stmt = con.createStatement();

                stmt.execute("UPDATE "+StaticData.lager+" SET belegterPlatz = belegterPlatz+"+r.getAmmount()+" WHERE lagerID = 1;");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void removeResource(Resource r,int ammount){
        lager.toFirst();
        while(lager.hasAccess()){
            if(lager.getContent().getName().equals(r.getName())){
                lager.getContent().removeAmmount(ammount);
                Connection con = null;
                try {
                    con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
                    Statement stmt = con.createStatement();
                    stmt.execute("UPDATE "+StaticData.lager+" SET belegterPlatz = belegterPlatz-"+ammount+" WHERE lagerID = 1;");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void loadLagerData(int farmID){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            Statement stmt = con.createStatement();

            stmt.execute("INSERT INTO "+ StaticData.lager+" (lagerPlatz,belegterPlatz,farmID) VALUES (50,0,"+farmID+");");


            ResultSet resultSet = stmt.executeQuery("SELECT lagerPlatz FROM "+StaticData.lager+" WHERE lagerID = 1;");
            if(resultSet.next())
            lagerplatz = resultSet.getInt("lagerPlatz");

            resultSet = stmt.executeQuery("SELECT belegterPlatz FROM "+StaticData.lager+";");
            if(resultSet.next()) {
                belegterPlatz = resultSet.getInt("belegterPlatz");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
