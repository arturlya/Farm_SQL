package model;

import control.Farm;
import control.StaticData;
import model.framework.GraphicalObject;

import java.sql.*;

public class Mitarbeiter extends GraphicalObject {

    private static Statement stmt;
    private String job;
    private int arbeitsPlatz;
    private int id;
    private boolean working;
    private double workCooldown;

    private static ResultSet resultSet;


    public Mitarbeiter(String job,int arbeitsPlatz){
        this.job = job;
        this.arbeitsPlatz = arbeitsPlatz;
        datenErstellung(job,arbeitsPlatz);
        System.out.println("Added mitarbeiter");
        try {
            ResultSet result = stmt.executeQuery("SELECT * FROM " + StaticData.mitarbieter + ";");
            int i = 0;
            while (result.next()) {
                i++;
            }
            id = i;
        }catch (Exception e) {
            System.out.println("Keine ID für Mitarbeiter");
        }
    }

    @Override
    public void update(double dt){
        updateDatenbank();
        if(workCooldown >0){
            workCooldown = workCooldown-1*dt;
        }
        if(workCooldown <= 0 && working){
            if(job.equals("Pflanze")) {
                working = false;
                kuemmert_sich.remove(id);
            }else if(job.equals("Tier")){
                working = false;
                fuettert.remove(id);
            }
        }
    }

    private void datenErstellung(String job, int arbeitsplatz){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
        }catch (Exception e) {System.out.println("Keine Connection");}

        try {
            stmt.execute("INSERT INTO "+ StaticData.mitarbieter+" (job,farmID) VALUES ('" + job + "'," + arbeitsplatz +");");
        }catch (Exception e) {
            e.printStackTrace();
            //System.out.println("Keine Werte für Mitarbeiter");
        }

        try {
            ResultSet result = stmt.executeQuery("SELECT * FROM " + StaticData.mitarbieter + ";");
            int i = 0;
            while (result.next()) {
                i++;
            }
            id = i;
        }catch (Exception e) {
            System.out.println("Keine ID für Mitarbeiter");
        }
    }

    private void updateDatenbank() {
        if(!working && workCooldown<=0) {
            if (job.equals("Pflanze")) {
                try {
                    resultSet = stmt.executeQuery("SELECT pflanzenID FROM " + StaticData.pflanze + " WHERE readyToHarvest = 1;");
                    while (resultSet.next() && !working) {
                        if (!kuemmert_sich.hasRelation(resultSet.getInt("pflanzenID"))) {
                            kuemmert_sich.add(id, resultSet.getInt("pflanzenID"));
                            working = true;
                            workCooldown = 5;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (job.equals("Tier")) {
                try {
                    resultSet = stmt.executeQuery("SELECT tierID FROM " + StaticData.tier + " WHERE lootable = 1;");
                    while(resultSet.next() && !working){
                        if(!fuettert.hasRelation(resultSet.getInt("tierID"))){
                            fuettert.add(id,resultSet.getInt("tierID"));
                            working = true;
                            workCooldown = 5;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public int getID(){
        return id;
    }


    public static int getNumberOfPflanzenMitarbeiter(){

        try {
            resultSet = stmt.executeQuery("SELECT COUNT(*) FROM (SELECT * FROM "+StaticData.mitarbieter+" WHERE job = 'Pflanze') AS anzahl");
            while(resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getNumberOfTierMitarbeiter(){

        try {
            resultSet = stmt.executeQuery("SELECT COUNT(*) FROM (SELECT * FROM "+StaticData.mitarbieter+" WHERE job = 'Tier') AS anzahl");
            while(resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
