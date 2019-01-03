package model;

import control.StaticData;
import model.framework.GraphicalObject;

import java.sql.*;

public class Mitarbeiter extends GraphicalObject {

    private Statement stmt;
    private String job;
    private int arbeitsPlatz;
    private int id;
    private double lohn;

    public Mitarbeiter(String job,int arbeitsPlatz){
        this.job = job;
        this.arbeitsPlatz = arbeitsPlatz;
        lohn = Math.random()*4+5;
        datenErstellung(job,arbeitsPlatz,lohn);
    }

    @Override
    public void update(double dt){

    }

    private void datenErstellung(String job, int arbeitsplatz, double lohn){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
        }catch (Exception e) {System.out.println("Keine Connection");}

        try {
            stmt.execute("INSERT INTO "+ StaticData.mitarbieter+" (job,arbeitsplatz,lohn) VALUES ('" + job + "'," + arbeitsplatz + ","+lohn+");");
        }catch (Exception e) {
            System.out.println("Keine Werte für Mitarbeiter");
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

    private void updateDatenbank(){

    }
}
