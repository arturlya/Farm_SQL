package model;

import control.StaticData;

import java.sql.*;

public class kuemmert_sich {

    private static Connection con;
    private static Statement stmt;

    {
        try {
            con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    public kuemmert_sich() {
        /*Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            Statement stmt = con.createStatement();
        } catch(SQLException e) {
            e.printStackTrace();
        }*/
    }

    public static void add(int mitarbeiterID, int pflanzenID){
        try {
            con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
            stmt.execute("INSERT INTO "+ StaticData.kuemmert_sich+" (mitarbeiterID,pflanzenID) VALUES ("+mitarbeiterID+","+ pflanzenID+");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void remove(int mitarbeiterID){
        try{
            con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
            stmt.execute("DELETE FROM "+ StaticData.kuemmert_sich+" WHERE mitarbeiterID = "+mitarbeiterID+";");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean hasRelation(int pflanzenID){
        ResultSet result;
        try{

            result = stmt.executeQuery("SELECT kuemmert_sichID FROM "+ StaticData.kuemmert_sich+" WHERE EXISTS (SELECT * FROM "+ StaticData.kuemmert_sich+" WHERE pflanzenID = "+pflanzenID+")");
            while(result.next()){
                return true;
            }
        }catch(SQLException e){e.printStackTrace();}
        catch(NullPointerException e){return false;}
        return false;
    }
}
