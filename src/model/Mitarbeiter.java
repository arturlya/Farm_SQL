package model;

import control.Farm;
import control.StaticData;
import model.framework.GraphicalObject;

import java.sql.*;

public class Mitarbeiter extends GraphicalObject {

    private Statement stmt;
    private String job;
    private int arbeitsPlatz;
    private int id;
    private double lohn;
    private boolean working;
    private double workCooldown;

    private ResultSet resultSet;


    public Mitarbeiter(String job,int arbeitsPlatz){
        this.job = job;
        this.arbeitsPlatz = arbeitsPlatz;
        lohn = Math.random()*4+5;
        datenErstellung(job,arbeitsPlatz,lohn);
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

    private void datenErstellung(String job, int arbeitsplatz, double lohn){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
        }catch (Exception e) {System.out.println("Keine Connection");}

        try {
            stmt.execute("INSERT INTO "+ StaticData.mitarbieter+" (job,farmID,lohn) VALUES ('" + job + "'," + arbeitsplatz + ","+lohn+");");
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

}
